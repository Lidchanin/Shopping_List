package com.lidchanin.crudindiploma;

import android.os.Environment;

public interface Constants {
    interface Tessaract{

        String SLASH="/";
        String DATA_PATH = String.valueOf(Environment.getExternalStorageDirectory());
        String TESSDATA = "tessdata";
        String ENGTRAIN = "eng.traineddata";
        String RUSTRAIN = "rus.traineddata";

    }
}