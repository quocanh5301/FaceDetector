/*
 * Copyright 2022 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.antispoofingfacerecognition;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.example.antispoofingfacerecognition.Utils.PreferenceUtils;
import com.google.mlkit.vision.common.PointF3D;

import com.google.mlkit.vision.facemesh.FaceMesh;
import com.google.mlkit.vision.facemesh.FaceMesh.ContourType;
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions;
import com.google.mlkit.vision.facemesh.FaceMeshPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Graphic instance for rendering face position and mesh info within the associated graphic overlay
 * view.
 */
public class FaceMeshGraphic extends GraphicOverlay.Graphic {
  private static final int USE_CASE_CONTOUR_ONLY = 999;

  private static final float FACE_POSITION_RADIUS = 5.0f;
  private static final float BOX_STROKE_WIDTH = 5.0f;

  public  static List<FaceMeshPoint> meshPointList = null;
  float scaleX = 1184.0f / 352;
  float scaleY = 672.0f / 288;
  private final Paint positionPaint;
  private final Paint boxPaint;
  private volatile FaceMesh faceMesh;
  private final int useCase;
  private float zMin;
  private float zMax;

  @ContourType
  private static final int[] DISPLAY_CONTOURS = {
    FaceMesh.FACE_OVAL,
    FaceMesh.LEFT_EYEBROW_TOP,
    FaceMesh.LEFT_EYEBROW_BOTTOM,
    FaceMesh.RIGHT_EYEBROW_TOP,
    FaceMesh.RIGHT_EYEBROW_BOTTOM,
    FaceMesh.LEFT_EYE,
    FaceMesh.RIGHT_EYE,
    FaceMesh.UPPER_LIP_TOP,
    FaceMesh.UPPER_LIP_BOTTOM,
    FaceMesh.LOWER_LIP_TOP,
    FaceMesh.LOWER_LIP_BOTTOM,
    FaceMesh.NOSE_BRIDGE
  };

  FaceMeshGraphic(GraphicOverlay overlay, FaceMesh faceMesh) {
    super(overlay);

    this.faceMesh = faceMesh;
    final int selectedColor = Color.WHITE;

    positionPaint = new Paint();
    positionPaint.setColor(selectedColor);

    boxPaint = new Paint();
    boxPaint.setColor(selectedColor);
    boxPaint.setStyle(Style.STROKE);
    boxPaint.setStrokeWidth(BOX_STROKE_WIDTH);

    useCase = PreferenceUtils.getFaceMeshUseCase(getApplicationContext());
  }

  /** Draws the face annotations for position on the supplied canvas. */
  @Override
  public void draw(Canvas canvas) {
    if (faceMesh == null) {
      return;
    }

    // Draws the bounding box.
    RectF rect = new RectF(faceMesh.getBoundingBox());
    // If the image is flipped, the left will be translated to right, and the right to left.
    float x0 = translateX(rect.left);
    float x1 = translateX(rect.right);
    rect.left = min(x0, x1);
    rect.right = max(x0, x1);
    rect.top = translateY(rect.top);
    rect.bottom = translateY(rect.bottom);
    canvas.drawRect(rect, boxPaint);

    // Draw face mesh
//    List<FaceMeshPoint> points =
//        useCase == USE_CASE_CONTOUR_ONLY ? getContourPoints(faceMesh) : faceMesh.getAllPoints();
//    List<Triangle<FaceMeshPoint>> triangles = faceMesh.getAllTriangles();
//
//    zMin = Float.MAX_VALUE;
//    zMax = Float.MIN_VALUE;
//    for (FaceMeshPoint point : points) {
//      zMin = min(zMin, point.getPosition().getZ());
//      zMax = max(zMax, point.getPosition().getZ());
//    }
//
//    // Draw face mesh points
//    for (FaceMeshPoint point : points) {
//      updatePaintColorByZValue(
//          positionPaint,
//          canvas,
//          /* visualizeZ= */ true,
//          /* rescaleZForVisualization= */ true,
//          point.getPosition().getZ(),
//          zMin,
//          zMax);
//      canvas.drawCircle(
//          translateX(point.getPosition().getX()),
//          translateY(point.getPosition().getY()),
//          FACE_POSITION_RADIUS,
//          positionPaint);
//    }

    if (useCase == FaceMeshDetectorOptions.FACE_MESH) {
      // Draw face mesh triangles
//      for (Triangle<FaceMeshPoint> triangle : triangles) {
//        List<FaceMeshPoint> faceMeshPoints = triangle.getAllPoints();
//        PointF3D point1 = faceMeshPoints.get(0).getPosition();
//        PointF3D point2 = faceMeshPoints.get(1).getPosition();
//        PointF3D point3 = faceMeshPoints.get(2).getPosition();
//
//        drawLine(canvas, point1, point2);
//        drawLine(canvas, point2, point3);
//        drawLine(canvas, point3, point1);
//      }

//      List<FaceMeshPoint> points = faceMesh.getAllPoints();
//      drawTriggle(canvas,points.get(17).getPosition(),points.get(95).getPosition(),points.get(324).getPosition());
//      drawTriggle(canvas,points.get(405).getPosition(),points.get(95).getPosition(),points.get(324).getPosition());
//      drawTriggle(canvas,points.get(181).getPosition(),points.get(95).getPosition(),points.get(324).getPosition());
//      drawTriggle(canvas,points.get(308).getPosition(),points.get(0).getPosition(),points.get(191).getPosition());
//      drawTriggle(canvas,points.get(308).getPosition(),points.get(269).getPosition(),points.get(191).getPosition());
//      drawTriggle(canvas,points.get(308).getPosition(),points.get(39).getPosition(),points.get(191).getPosition());
//
//      // Vẽ tam giác ở vùng trên mỗi dưới miệng
//      drawTriggle(canvas,points.get(287).getPosition(),points.get(2).getPosition(),points.get(57).getPosition());
//
//      // Vẽ tam giác ở vùng mũi
//      drawTriggle(canvas,points.get(423).getPosition(),points.get(9).getPosition(),points.get(203).getPosition());
//      drawTriggle(canvas,points.get(423).getPosition(),points.get(5).getPosition(),points.get(203).getPosition());
//      drawTriggle(canvas,points.get(423).getPosition(),points.get(6).getPosition(),points.get(203).getPosition());
//      drawTriggle(canvas,points.get(423).getPosition(),points.get(336).getPosition(),points.get(203).getPosition());
//      drawTriggle(canvas,points.get(423).getPosition(),points.get(107).getPosition(),points.get(203).getPosition());
//      drawTriggle(canvas,points.get(336).getPosition(),points.get(4).getPosition(),points.get(107).getPosition());
//
//      // Vẽ tam giác ở vùng trán
//      drawTriggle(canvas,points.get(333).getPosition(),points.get(10).getPosition(),points.get(104).getPosition());
//      drawTriggle(canvas,points.get(333).getPosition(),points.get(336).getPosition(),points.get(104).getPosition());
//      drawTriggle(canvas,points.get(333).getPosition(),points.get(107).getPosition(),points.get(104).getPosition());
//
//      // Vẽ tam giác ở vung cằm
//      drawTriggle(canvas,points.get(377).getPosition(),points.get(18).getPosition(),points.get(148).getPosition());
//      drawTriggle(canvas,points.get(400).getPosition(),points.get(43).getPosition(),points.get(176).getPosition());
//      drawTriggle(canvas,points.get(400).getPosition(),points.get(273).getPosition(),points.get(176).getPosition());
//
//
//      // Vẽ tam gác ở vùng mặt
//      drawTriggle(canvas,points.get(57).getPosition(),points.get(203).getPosition(),points.get(226).getPosition());
//      drawTriggle(canvas,points.get(446).getPosition(),points.get(423).getPosition(),points.get(287).getPosition());
//
//      // Vẽ tam giác ở vùng lông mày
//      //bên trái
//
//      drawTriggle(canvas,points.get(276).getPosition(),points.get(334).getPosition(),points.get(336).getPosition());
//      //bên phải
//      drawTriggle(canvas,points.get(107).getPosition(),points.get(105).getPosition(),points.get(46).getPosition());
//
//      // Vẽ tam giác ở vùng mắt
//      drawTriggle(canvas,points.get(159).getPosition(),points.get(145).getPosition(),points.get(130).getPosition());
//      drawTriggle(canvas,points.get(159).getPosition(),points.get(145).getPosition(),points.get(133).getPosition());
//
//      drawTriggle(canvas,points.get(386).getPosition(),points.get(374).getPosition(),points.get(398).getPosition());
//      drawTriggle(canvas,points.get(386).getPosition(),points.get(374).getPosition(),points.get(263).getPosition());


      drawTriangle(0,423 ,9 ,203, canvas);
      drawTriangle(1,423 ,5 ,203,  canvas);
      drawTriangle(2, 423 ,6 ,203, canvas);
      drawTriangle(3,423 ,336 ,203, canvas);
      drawTriangle(4,423 ,107 ,203, canvas);
      drawTriangle(5,336 ,4 ,107, canvas);
      drawTriangle(6,377 ,18 ,148, canvas);
      drawTriangle(7,400 ,43 ,176, canvas);
      drawTriangle(8,400 ,273 ,176, canvas);
      drawTriangle(9,57 ,203 ,226, canvas);
      drawTriangle(10,446 ,423 ,287, canvas);
      drawTriangle(11,276 ,334 ,336, canvas);
      drawTriangle(12,107 ,105 ,46, canvas);
      drawTriangle(13,159 ,145 ,130,canvas);
      drawTriangle(14,159 ,145 ,133, canvas);
      drawTriangle(15,386 ,374 ,398, canvas);
      drawTriangle(16,386 ,374 ,263, canvas);
      drawTriangle(17,17 ,95 ,324, canvas);
      drawTriangle(18,405 ,95 ,324, canvas);
      drawTriangle(19,181 ,95 ,324, canvas);
      drawTriangle(20,308 ,0 ,191, canvas);
      drawTriangle(21,308 ,269 ,191, canvas);
      drawTriangle(22,308 ,39 ,191, canvas);
      drawTriangle(23,287 ,2 ,57,  canvas);
      drawTriangle(24,323 ,446 ,287,  canvas);
      drawTriangle(25,93 ,226 ,57,  canvas);




    }
  }

  private void drawTriangle(int index,int i, int j ,int k,Canvas canvas){


    float x1 = meshPointList.get(i).getPosition().getX() ;
    float y1 = meshPointList.get(i).getPosition().getY() ;
    float z1 = meshPointList.get(i).getPosition().getZ();
    float x2 = meshPointList.get(j).getPosition().getX() ;
    float y2 = meshPointList.get(j).getPosition().getY() ;
    float z2 = meshPointList.get(j).getPosition().getZ() ;
    float x3 = meshPointList.get(k).getPosition().getX();
    float y3 = meshPointList.get(k).getPosition().getY() ;
    float z3 = meshPointList.get(k).getPosition().getZ();

    double tmp = -1;
    if (PersonFace.size() > 0 ) {
      for (int userIndex = 0; userIndex < PersonFace.size() ; userIndex++) {
        tmp = PersonFace.compareCongruentTriangle(userIndex, index, meshPointList.get(i), meshPointList.get(j), meshPointList.get(k));
      }
    }

    Paint tmpPaint = new Paint();
    tmpPaint.setStrokeWidth(1);
    if (tmp < 0)
      tmpPaint.setColor(Color.YELLOW);
    else if (tmp < 0.2)
      tmpPaint.setColor(Color.BLUE);
    else
      tmpPaint.setColor(Color.RED);


    tmpPaint.setStyle(Paint.Style.FILL);

    canvas.drawLine(translateX(x1), translateY(y1), translateX(x2), translateY(y2), tmpPaint);
    canvas.drawLine(translateX(x2), translateY(y2), translateX(x3), translateY(y3), tmpPaint);
    canvas.drawLine(translateX(x3), translateY(y3), translateX(x1), translateY(y1), tmpPaint);

  }

  private List<FaceMeshPoint> getContourPoints(FaceMesh faceMesh) {
    List<FaceMeshPoint> contourPoints = new ArrayList<>();
    for (int type : DISPLAY_CONTOURS) {
      contourPoints.addAll(faceMesh.getPoints(type));
    }
    return contourPoints;
  }

  private void drawLine(Canvas canvas, PointF3D point1, PointF3D point2) {
//    updatePaintColorByZValue(
////        positionPaint,
////        canvas,
////        /* visualizeZ= */ true,
////        /* rescaleZForVisualization= */ true,
////        (point1.getZ() + point2.getZ()) / 2,
////        zMin,
////        zMax);
    canvas.drawLine(
        translateX(point1.getX()),
        translateY(point1.getY()),
        translateX(point2.getX()),
        translateY(point2.getY()),
        positionPaint);
  }
}
