/*
 * Copyright 2020 Google LLC. All rights reserved.
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

package com.example.antispoofingfacerecognition.UI;

import static com.example.antispoofingfacerecognition.DataManager.DataManager.getData;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.antispoofingfacerecognition.CameraSource;
import com.example.antispoofingfacerecognition.CameraSourcePreview;
import com.example.antispoofingfacerecognition.FaceMeshDetectorProcessor;
import com.example.antispoofingfacerecognition.GraphicOverlay;
import com.example.antispoofingfacerecognition.PersonFace;
import com.example.antispoofingfacerecognition.R;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

/**
 * Live preview demo for ML Kit APIs.
 */
@KeepName
public final class LivePreviewActivity extends AppCompatActivity
        implements OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private static final String FACE_MESH_DETECTION = "Face Mesh Detection (Beta)";

    private static final String TAG = "LivePreviewActivity";

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private String selectedModel = FACE_MESH_DETECTION;
    private ImageView imgFlipCamera;
    private ImageView imgCamera;
    public static TextInputEditText edtInputName = null;
    public static TextView txtShowName = null;

    private int saveMeshRequest = 0;
    private boolean isFrontFacing = true;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PersonFace.clearData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_live_preview);

        getData().readTextFile(this);
        PersonFace.userName = getData().userNameList;
        PersonFace.facePoints = getData().pointList;

        imgFlipCamera = findViewById(R.id.imgFlipCamera);
        imgCamera = findViewById(R.id.imgCamera);
        preview = findViewById(R.id.preview_view);
        txtShowName = findViewById(R.id.name);
        edtInputName = findViewById(R.id.save_text);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.graphic_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        imgFlipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrontFacing = !isFrontFacing;
                toggleCamera();
            }
        });


        createCameraSource(selectedModel);
    }


    private void toggleCamera() {
        Log.d(TAG, "set facing");
        if (cameraSource != null) {
            if (!isFrontFacing) {
                Log.d(TAG, "cameraSource front");
                cameraSource.setFacing(cameraSource.CAMERA_FACING_FRONT);
            } else {
                Log.d(TAG, "cameraSource back");
                cameraSource.setFacing(cameraSource.CAMERA_FACING_BACK);
            }
        }
        preview.stop();
        startCameraSource();
    }

    @Override
    public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedModel = parent.getItemAtPosition(pos).toString();
        Log.d(TAG, "Selected model: " + selectedModel);
        preview.stop();
        createCameraSource(selectedModel);
        startCameraSource();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "Set facing");
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
            } else {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
            }
        }
        preview.stop();
        startCameraSource();
    }

    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }
        Log.i(TAG, "Using Face Detector Proccessor");
        cameraSource.setMachineLearningFrameProcessor(new FaceMeshDetectorProcessor(this));
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        createCameraSource(selectedModel);
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    public void saveFaceDetection(View view) {
        if (edtInputName.getText().toString().equals("")){
            Toast.makeText(this, "Please fill your name", Toast.LENGTH_LONG).show();
            edtInputName.requestFocus();
        } else {
            FaceMeshDetectorProcessor.saveMeshRequest = 1;
            FaceMeshDetectorProcessor.edtName = edtInputName;
            FaceMeshDetectorProcessor.txtUserName = txtShowName;
        }
    }
}
