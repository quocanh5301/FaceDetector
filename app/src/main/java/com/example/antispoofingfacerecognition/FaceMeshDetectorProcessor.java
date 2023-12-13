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

import static com.example.antispoofingfacerecognition.DataManager.DataManager.getData;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import static com.serenegiant.utils.UIThreadHelper.runOnUiThread;

import com.example.antispoofingfacerecognition.UI.LivePreviewActivity;
import com.example.antispoofingfacerecognition.Utils.PreferenceUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.facemesh.FaceMesh;
import com.google.mlkit.vision.facemesh.FaceMeshDetection;
import com.google.mlkit.vision.facemesh.FaceMeshDetector;
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions;
import com.google.mlkit.vision.facemesh.FaceMeshPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Selfie Face Detector Demo.
 */
public class FaceMeshDetectorProcessor extends VisionProcessorBase<List<FaceMesh>> {

    private static final String TAG = "SelfieFaceProcessor";

    private final FaceMeshDetector detector;

    private volatile FaceMesh faceMesh;
    public static int saveMeshRequest = 0;
    public static int savingMesh = 0;
//    public static Context context;
    public static TextInputEditText edtName = null;
    public static TextView txtUserName = null;


    public FaceMeshDetectorProcessor(Context context) {
        super(context);
        FaceMeshDetectorOptions.Builder optionsBuilder = new FaceMeshDetectorOptions.Builder();
        if (PreferenceUtils.getFaceMeshUseCase(context) == FaceMeshDetectorOptions.BOUNDING_BOX_ONLY) {
            optionsBuilder.setUseCase(FaceMeshDetectorOptions.BOUNDING_BOX_ONLY);
        }
        detector = FaceMeshDetection.getClient(optionsBuilder.build());
    }

    @Override
    public void stop() {
        super.stop();
        detector.close();
    }

    @Override
    protected Task<List<FaceMesh>> detectInImage(InputImage image) {

        return detector.process(image).addOnSuccessListener(new OnSuccessListener<List<FaceMesh>>() {
            @Override
            public void onSuccess(List<FaceMesh> faceMeshes) {

            }
        });
    }

    int count = 0;
    @Override
    protected void onSuccess(@NonNull List<FaceMesh> faces, @NonNull GraphicOverlay graphicOverlay) {
        for (FaceMesh face : faces) {
            graphicOverlay.add(new FaceMeshGraphic(graphicOverlay, face));
        }
        count++;

        Log.d("count", count + " " + System.currentTimeMillis());
        if (faces != null) {
            if (faces.size() > 0) {
                List<FaceMeshPoint> points = new ArrayList<>();
//                for (FaceMesh face : faces) {
//                    List<FaceMeshPoint> tmpPoints = face.getAllPoints();
//
//                    Log.d("TAG", ' ' + String.valueOf(tmpPoints.size()) + " points");
//                    points.addAll(tmpPoints);
//                }
                points = faces.get(0).getAllPoints();
                FaceMeshGraphic.meshPointList = points;

                // detect nháy mắt
                double x0 = points.get(336).getPosition().getX();
                double y0 = points.get(336).getPosition().getY();
                double x1 = points.get(285).getPosition().getX();
                double y1 = points.get(285).getPosition().getY();

                // mắt phải
                double x2 = points.get(159).getPosition().getX();
                double y2 = points.get(159).getPosition().getY();
                double x3 = points.get(145).getPosition().getX();
                double y3 = points.get(145).getPosition().getY();
                // mắt trái
                double x4 = points.get(386).getPosition().getX();
                double y4 = points.get(386).getPosition().getY();
                double x5 = points.get(374).getPosition().getX();
                double y5 = points.get(374).getPosition().getY();


                double tmp = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
                // độ dài mắt phải
                double tmp1 = Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));
                // độ dài mắt trái
                double tmp2 = Math.sqrt((x5 - x4) * (x5 - x4) + (y5 - y4) * (y5 - y4));


                double result1 = tmp1/tmp;
                double result2 = tmp2/tmp;

//                Log.i(TAG,"Result1: "+String.valueOf(result1));
//                Log.i(TAG,"Result2: "+String.valueOf(result2));
                Log.d(TAG,"tmp: "+String.valueOf(tmp));
                Log.d(TAG,"tmp1: "+String.valueOf(tmp1));
                Log.d(TAG,"tmp2: "+String.valueOf(tmp2));

                if (result1 < 0.4){
                    Log.d(TAG, "Eye Right Close:");

                } else {
                    Log.d(TAG,"Eye Right Open");
                }

                if (result2 < 0.5) {
//                    Log.d(TAG, "Eye Left Close:");
                }

                if (saveMeshRequest == 1) {
                    savingMesh = 1;
                    saveMeshRequest = 0;
                    PersonFace.addUser(edtName.getText().toString());
                    //1
                    PersonFace.addPoint(points.get(423));
                    PersonFace.addPoint(points.get(9));
                    PersonFace.addPoint(points.get(203));
                    //2
                    PersonFace.addPoint(points.get(423));
                    PersonFace.addPoint(points.get(5));
                    PersonFace.addPoint(points.get(203));
                    //3
                    PersonFace.addPoint(points.get(423));
                    PersonFace.addPoint(points.get(6));
                    PersonFace.addPoint(points.get(203));
                    //4
                    PersonFace.addPoint(points.get(423));
                    PersonFace.addPoint(points.get(336));
                    PersonFace.addPoint(points.get(203));
                    //5
                    PersonFace.addPoint(points.get(423));
                    PersonFace.addPoint(points.get(107));
                    PersonFace.addPoint(points.get(203));
                    //6
                    PersonFace.addPoint(points.get(333));
                    PersonFace.addPoint(points.get(107));
                    PersonFace.addPoint(points.get(104));
                    //7
                    PersonFace.addPoint(points.get(377));
                    PersonFace.addPoint(points.get(18));
                    PersonFace.addPoint(points.get(148));
                    //8
                    PersonFace.addPoint(points.get(400));
                    PersonFace.addPoint(points.get(43));
                    PersonFace.addPoint(points.get(176));
                    //9
                    PersonFace.addPoint(points.get(400));
                    PersonFace.addPoint(points.get(273));
                    PersonFace.addPoint(points.get(176));
                    //10
                    PersonFace.addPoint(points.get(57));
                    PersonFace.addPoint(points.get(203));
                    PersonFace.addPoint(points.get(226));
                    //11
                    PersonFace.addPoint(points.get(446));
                    PersonFace.addPoint(points.get(423));
                    PersonFace.addPoint(points.get(287));
                    //12
                    PersonFace.addPoint(points.get(276));
                    PersonFace.addPoint(points.get(334));
                    PersonFace.addPoint(points.get(336));
                    //13
                    PersonFace.addPoint(points.get(107));
                    PersonFace.addPoint(points.get(105));
                    PersonFace.addPoint(points.get(46));
                    //14
                    PersonFace.addPoint(points.get(159));
                    PersonFace.addPoint(points.get(145));
                    PersonFace.addPoint(points.get(130));
                    //15
                    PersonFace.addPoint(points.get(159));
                    PersonFace.addPoint(points.get(145));
                    PersonFace.addPoint(points.get(133));
                    //16
                    PersonFace.addPoint(points.get(386));
                    PersonFace.addPoint(points.get(374));
                    PersonFace.addPoint(points.get(398));
                    //17
                    PersonFace.addPoint(points.get(386));
                    PersonFace.addPoint(points.get(374));
                    PersonFace.addPoint(points.get(263));
                    //18
                    PersonFace.addPoint(points.get(17));
                    PersonFace.addPoint(points.get(95));
                    PersonFace.addPoint(points.get(324));
                    //19
                    PersonFace.addPoint(points.get(405));
                    PersonFace.addPoint(points.get(95));
                    PersonFace.addPoint(points.get(324));
                    //20
                    PersonFace.addPoint(points.get(181));
                    PersonFace.addPoint(points.get(95));
                    PersonFace.addPoint(points.get(324));
                    //21
                    PersonFace.addPoint(points.get(308));
                    PersonFace.addPoint(points.get(0));
                    PersonFace.addPoint(points.get(191));
                    //22
                    PersonFace.addPoint(points.get(308));
                    PersonFace.addPoint(points.get(269));
                    PersonFace.addPoint(points.get(191));
                    //23
                    PersonFace.addPoint(points.get(308));
                    PersonFace.addPoint(points.get(39));
                    PersonFace.addPoint(points.get(191));
                    //24
                    PersonFace.addPoint(points.get(287));
                    PersonFace.addPoint(points.get(2));
                    PersonFace.addPoint(points.get(57));

                    //25
                    PersonFace.addPoint(points.get(323));
                    PersonFace.addPoint(points.get(446));
                    PersonFace.addPoint(points.get(287));
                    //26
                    PersonFace.addPoint(points.get(93));
                    PersonFace.addPoint(points.get(226));
                    PersonFace.addPoint(points.get(57));

                    getData().saveTextFile(PersonFace.getLastUserName(), PersonFace.getLastPoint().toString(), edtName.getContext());

                    savingMesh = 0;

                } else if (PersonFace.size() > 0) {
                    for (int userIndex = 0; userIndex < PersonFace.size(); userIndex++) {
                        int score = PersonFace.countMacht(userIndex, points);
                        txtUserName = LivePreviewActivity.txtShowName;
                        if (score >= 24) {
                            String name = PersonFace.getUserName(userIndex);
                            txtUserName.setText(name);
                            break;
                        } else {
                            txtUserName.setText("UnKnown!!!");
                        }
                    }
                }
            }
        }


    }


    @Override
    protected void onFailure(@NonNull Exception e) {
        txtUserName.setText("NO FACE");
    }

}
