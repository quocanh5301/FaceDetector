package com.example.antispoofingfacerecognition;

import com.google.mlkit.vision.facemesh.FaceMeshPoint;

import java.util.ArrayList;
import java.util.List;

public class PersonFace {
    public static ArrayList<String> userName = new ArrayList<>();
    public static ArrayList<ArrayList<Double>> facePoints = new ArrayList<ArrayList<Double>>();

    // lấy ra tên người dùng ở vị trí nào đó
    public static String getUserName(int userIndex) {
        return userName.get(userIndex);
    }

    public static void clearData(){
        userName.clear();
        facePoints.clear();
    }

    public static ArrayList<Double> getLastPoint() {
        return facePoints.get(facePoints.size() - 1);
    }
    public static String getLastUserName() {
        return userName.get(userName.size() -1);
    }

    public static int getPointsSize() {
        return facePoints.size();
    }

    // trả về độ dài mảng user
    public static int size() {
        return userName.size();
    }

    // thêm user mới vào mảng
    public static void addUser(String name) {
        userName.add(name);
        facePoints.add(new ArrayList<>());
    }

    // thêm tọa độ điểm vào mảng khuôn mặt
    public static void addPoint(FaceMeshPoint p) {
        int lastIndex = userName.size() - 1;
        facePoints.get(lastIndex).add((double) p.getPosition().getX());
        facePoints.get(lastIndex).add((double) p.getPosition().getY());
        facePoints.get(lastIndex).add((double) p.getPosition().getZ());


    }


    // tính tam giác đồng dạng tại user thứ i

    private static double[] computeEdgeOfTriangle(int userIndex, int i) {
        double[] tmp = new double[3];
        int startIndex = i * 9;
        double x0 = facePoints.get(userIndex).get(startIndex + 0);
        double y0 = facePoints.get(userIndex).get(startIndex + 1);
        double z0 = facePoints.get(userIndex).get(startIndex + 2);
        double x1 = facePoints.get(userIndex).get(startIndex + 3);
        double y1 = facePoints.get(userIndex).get(startIndex + 4);
        double z1 = facePoints.get(userIndex).get(startIndex + 5);
        double x2 = facePoints.get(userIndex).get(startIndex + 6);
        double y2 = facePoints.get(userIndex).get(startIndex + 7);
        double z2 = facePoints.get(userIndex).get(startIndex + 8);

        tmp[0] = (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0) + (z1 - z0) * (z1 - z0);
        tmp[1] = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1);
        tmp[2] = (x2 - x0) * (x2 - x0) + (y2 - y0) * (y2 - y0) + (z2 - z0) * (z2 - z0);

        return tmp;
    }

    public static double compareCongruentTriangle(int userIndex, int i, FaceMeshPoint A, FaceMeshPoint B, FaceMeshPoint C) {
        double result = 0;

        double x0 = A.getPosition().getX();
        double y0 = A.getPosition().getY();
        double z0 = A.getPosition().getZ();
        double x1 = B.getPosition().getX();
        double y1 = B.getPosition().getY();
        double z1 = B.getPosition().getZ();
        double x2 = C.getPosition().getX();
        double y2 = C.getPosition().getY();
        double z2 = C.getPosition().getZ();

        double[] tmp = computeEdgeOfTriangle(userIndex, i);
        double[] tmp1 = new double[3];
        tmp1[0] = (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0) + (z1 - z0) * (z1 - z0);
        tmp1[1] = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1);
        tmp1[2] = (x2 - x0) * (x2 - x0) + (y2 - y0) * (y2 - y0) + (z2 - z0) * (z2 - z0);

        double r1 = tmp[0] / tmp1[0];
        double r2 = tmp[1] / tmp1[1];
        double r3 = tmp[2] / tmp1[2];
        result += Math.abs(r1 / r3 - 1);
        result += Math.abs(r2 / r3 - 1);

        return result;
    }

    public static int matchSingle(int userIndex, int i, FaceMeshPoint A, FaceMeshPoint B, FaceMeshPoint C) {
        if (compareCongruentTriangle(userIndex, i, A, B, C) <= 0.2) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int countMacht(int userIndex, List<FaceMeshPoint> facePoints) {
        int resultCount = 0;

        //1
        resultCount += matchSingle(userIndex, 0, facePoints.get(423), facePoints.get(9), facePoints.get(203));
        //2
        resultCount += matchSingle(userIndex, 1, facePoints.get(423), facePoints.get(5), facePoints.get(203));
        //3
        resultCount += matchSingle(userIndex, 2, facePoints.get(423), facePoints.get(6), facePoints.get(203));
        //4
        resultCount += matchSingle(userIndex, 3, facePoints.get(423), facePoints.get(336), facePoints.get(203));
        //5
        resultCount += matchSingle(userIndex, 4, facePoints.get(423), facePoints.get(107), facePoints.get(203));
        //6
        resultCount += matchSingle(userIndex, 5, facePoints.get(336), facePoints.get(4), facePoints.get(107));

        //7
        resultCount += matchSingle(userIndex, 6, facePoints.get(377), facePoints.get(18), facePoints.get(148));

        //8
        resultCount += matchSingle(userIndex, 7, facePoints.get(400), facePoints.get(43), facePoints.get(176));
        //9
        resultCount += matchSingle(userIndex, 8, facePoints.get(400), facePoints.get(273), facePoints.get(176));
        //10
        resultCount += matchSingle(userIndex, 9, facePoints.get(57), facePoints.get(203), facePoints.get(226));
        //11
        resultCount += matchSingle(userIndex, 10, facePoints.get(446), facePoints.get(423), facePoints.get(287));
        //12
        resultCount += matchSingle(userIndex, 11, facePoints.get(276), facePoints.get(334), facePoints.get(336));
        //13
        resultCount += matchSingle(userIndex, 12, facePoints.get(107), facePoints.get(105), facePoints.get(46));
        //14
        resultCount += matchSingle(userIndex, 13, facePoints.get(159), facePoints.get(145), facePoints.get(130));
        //15
        resultCount += matchSingle(userIndex, 14, facePoints.get(159), facePoints.get(145), facePoints.get(133));
        //16
        resultCount += matchSingle(userIndex, 15, facePoints.get(386), facePoints.get(374), facePoints.get(398));
        //17
        resultCount += matchSingle(userIndex, 16, facePoints.get(386), facePoints.get(374), facePoints.get(263));
        //18
        resultCount += matchSingle(userIndex, 17, facePoints.get(17), facePoints.get(95), facePoints.get(324));
        //19
        resultCount += matchSingle(userIndex, 18, facePoints.get(405), facePoints.get(95), facePoints.get(324));
        //20
        resultCount += matchSingle(userIndex, 19, facePoints.get(181), facePoints.get(95), facePoints.get(324));
        //21
        resultCount += matchSingle(userIndex, 20, facePoints.get(308), facePoints.get(0), facePoints.get(191));
        //22
        resultCount += matchSingle(userIndex, 21, facePoints.get(308), facePoints.get(269), facePoints.get(191));
        //23
        resultCount += matchSingle(userIndex, 22, facePoints.get(308), facePoints.get(39), facePoints.get(191));

        //24
        resultCount += matchSingle(userIndex, 23, facePoints.get(287), facePoints.get(2), facePoints.get(57));
        //25
        resultCount += matchSingle(userIndex, 24, facePoints.get(323), facePoints.get(446), facePoints.get(287));
        //26
        resultCount += matchSingle(userIndex, 24, facePoints.get(93), facePoints.get(226), facePoints.get(57));

        return resultCount;
    }


//    public static int checkEyes(FaceMeshPoint A, FaceMeshPoint B, FaceMeshPoint C, FaceMeshPoint D){
//        double x0 = A.getPosition().getX();
//        double y0 = A.getPosition().getY();
//        double z0 = A.getPosition().getZ();
//        double x1 = B.getPosition().getX();
//        double y1 = B.getPosition().getY();
//        double z1 = B.getPosition().getZ();
//        double x2 = C.getPosition().getX();
//        double y2 = C.getPosition().getY();
//        double z2 = C.getPosition().getZ();
//        double x3 = D.getPosition().getX();
//        double y3 = D.getPosition().getY();
//        double z3 = D.getPosition().getZ();
//
//        double eyeLeft = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0) + (z1 - z0) * (z1 - z0));
//        double eyeRight =Math.sqrt((x2 - x3) * (x2 - x3) + (y2 - y3) * (y2 - y3) + (z2 - z3) * (z2 - z3));
//        double tmp = eyeRight + eyeLeft;
//        if (tmp <= 2 && tmp > 0){
//            return  1;
//
//        }else{
//            return 0;
//        }
//
//    }
}
