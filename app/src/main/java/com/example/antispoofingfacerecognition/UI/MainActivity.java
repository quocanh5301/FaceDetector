package com.example.antispoofingfacerecognition.UI;

import static com.example.antispoofingfacerecognition.DataManager.DataManager.getData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.antispoofingfacerecognition.PersonFace;
import com.example.antispoofingfacerecognition.R;

public class MainActivity extends AppCompatActivity {
    private Button btnDetect;

    private static final int MANAGE_FILE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDetect = findViewById(R.id.btnDetect);
        requestRuntimePer();
    }

    public void DeleteSaveData(View view){
        getData().deleteTextFile(this);
        PersonFace.clearData();
    }
    public void Detect(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MANAGE_FILE_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with file creation
            Intent intent = new Intent(this, LivePreviewActivity.class);
            startActivity(intent);
        }
    }

    public void DetectImage(View view) {
        Intent intent = new Intent(this, StillImageActivity.class);
        startActivity(intent);
    }

    // request runtime permission
    public void requestRuntimePer(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MANAGE_FILE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file creation\
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                // Permission denied, handle the error condition appropriately
                finish();
            }
        }
    }


}