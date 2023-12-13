package com.example.antispoofingfacerecognition.DataManager;

import android.content.Context;
import android.util.Log;

import com.example.antispoofingfacerecognition.Utils.ConverterUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {
    private static DataManager data;
    private final String FILE_NAME = "MyTextFile.txt";
    private final String DIR_NAME = "MyDirFile";

    static {
        data = new DataManager();
    }

    private String TAG = "DataManager";

    public static DataManager getData() {
        if (data == null) {
            data = new DataManager();
        }
        return data;
    }

    public ArrayList<String> userNameList = new ArrayList<>();
    public ArrayList<ArrayList<Double>> pointList = new ArrayList<>();


    // SaveFile
    public void saveTextFile(String userName, String facePoints, Context context) {
        try {
            File directory = new File(context.getExternalFilesDir(null), DIR_NAME);
            if (!directory.exists()) {
                if (directory.mkdirs()){
                    File file = new File(directory, FILE_NAME);
                    Log.i(TAG, "get file path " + file.getPath());
                    if (file.createNewFile()){
                        FileWriter writer = new FileWriter(file, false);
                        writer.write("{\"userName\":\"" + userName + "\", \"facePoints\": " + facePoints + "}\n");
                        writer.flush();
                        writer.close();
                    }
                }
            } else {
                File file = new File(directory, FILE_NAME);
                FileWriter writer = new FileWriter(file, true);
                writer.write("{\"userName\":\"" + userName + "\", \"facePoints\": " + facePoints + "}\n");
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ReadTextFile
    public void readTextFile(Context context) {
        try {
            File directory = new File(context.getExternalFilesDir(null), DIR_NAME);
            File file = new File(directory, FILE_NAME);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);
                String userName = jsonObject.getString("userName");
                String facePoints = jsonObject.getString("facePoints");
                ArrayList<String> arrFacePoints = new ArrayList<>(Arrays.asList(facePoints.split(",")));
                userNameList.add(userName);
                pointList.add(ConverterUtils.convertToDoubleArrayList(arrFacePoints));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTextFile(Context context){
        File directory = new File(context.getExternalFilesDir(null), DIR_NAME);
        File file = new File(directory, FILE_NAME);
        file.delete();
        directory.delete();
    }
}
