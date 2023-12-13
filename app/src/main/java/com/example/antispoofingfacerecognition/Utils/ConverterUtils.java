package com.example.antispoofingfacerecognition.Utils;

import android.util.Log;

import java.util.ArrayList;

public class ConverterUtils {
    private static String TAG = "ConverterUtils";

    public static ArrayList<Double> convertToDoubleArrayList(ArrayList<String> stringList) {
        ArrayList<Double> doubleArrayList = new ArrayList<>();

        for (String str : stringList) {
            String str2;
            if (!str.contains("[") && !str.contains("]")){
                str2 = str;
            } else {
                String temp = str.replace("[", "");
                str2 = temp.replace("]", "");
            }
            double value = Double.parseDouble(str2);
            doubleArrayList.add(value);
        }

        return doubleArrayList;
    }
}
