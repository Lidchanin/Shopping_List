package com.lidchanin.shoppinglist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lidchanin.shoppinglist.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Alexander Destroyed on 18.05.2017.
 */

public class ScanLogs {
    private SharedPreferences sharedPreferences ;
    private Context context;
    private String TAG = getClass().getCanonicalName();
    public ScanLogs(String output,Context context) {
        this.context=context;
        writeToFile(output);
    }

    private boolean writerPreference(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.contains(Constants.SharedPreferences.PREF_KEY_SCAN_TIMES))
        {
            int scanTimes=sharedPreferences.getInt(Constants.SharedPreferences.PREF_KEY_SCAN_TIMES, 0);
            if(scanTimes<29){
                sharedPreferences.edit().putInt(Constants.SharedPreferences.PREF_KEY_SCAN_TIMES,scanTimes+1).apply();
                return true;
            }
            else{
                Log.d(TAG,"Also done 30 scans");
                return false;
            }
        }
        else{
            sharedPreferences.edit().putInt(Constants.SharedPreferences.PREF_KEY_SCAN_TIMES,1).apply();
            return true;
        }
    }
    private void writeToFile(String data) {
            Calendar calendar = Calendar.getInstance();
            final File txtOutput = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH + Constants.Tessaract.TESSDATA + Constants.Tessaract.SLASH + "scan" + calendar.get(Calendar.DATE) + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR) + ".txt");
            if (writerPreference()) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(txtOutput, true);
                    fileOutputStream.write(("/-----Scan Starts-----/\n" + data + " \n/-----Scan Ended----/\n").getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            } else {
                Log.d(TAG, "Also done 30 scans");
                //// TODO: 29.05.2017 add send to firebase scan result on 30 scans
        }
    }
}
