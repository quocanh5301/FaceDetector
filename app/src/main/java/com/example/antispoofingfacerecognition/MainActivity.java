package com.example.antispoofingfacerecognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {
    private Button btnDetect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDetect = findViewById(R.id.btnDetect);
        requestRuntimePer();
    }

    public void Detect(View view) {
//        Intent intent = new Intent(this,LivePreviewActivity.class);
//        startActivity(intent);



        //qa
        File localFile = new File(Environment.getExternalStorageDirectory().getPath(), "mySaveFile.txt");
        String readContent = "";
        if (!localFile.exists()){
            Log.i("main", "file: not exist");
            Intent intent = new Intent(this,LivePreviewActivity.class);
            startActivity(intent);
        } else {

            Log.i("main", "file: exist ");
//            localFile.delete();
            if (localFile.isFile()){
                Log.i("main", "file: is file ");
            } else {
                Log.i("main", "file: not file ");
            }

        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getPath() + "/mySaveFile.txt"));
            int c;
            while ((c = bufferedReader.read()) != -1){
                readContent = readContent + (char) c;
            }
            JSONObject jsonObject = new JSONObject(readContent);
            Log.i("main", "JSON saved: " + jsonObject.toString());
        } catch (Exception e){
            Log.i("mxain", "JSON saved: error");
            e.printStackTrace();
        }
        //qa
    }

    // request runtime permission

    public void requestRuntimePer(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }
}