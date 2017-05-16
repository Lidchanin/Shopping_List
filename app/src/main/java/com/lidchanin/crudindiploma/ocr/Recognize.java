package com.lidchanin.crudindiploma.ocr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.lidchanin.crudindiploma.activities.ChoiceActivity;

import java.util.ArrayList;
public class Recognize extends AsyncTask<Bitmap,TessBaseAPI.ProgressValues,Void> implements TessBaseAPI.ProgressNotifier {
    private final String TAG=this.getClass().getSimpleName();
    private final Regex mRegex= new Regex();
    private ProgressBar progressBar;
    private Context context;
    public Recognize(ProgressBar progressBar, final Context context){
        this.context=context;
        this.progressBar= progressBar;
    }

    @Override
    protected Void doInBackground(Bitmap... bitmap) {
        final String output;
        final TessBaseAPI mTessBaseAPI = new TessBaseAPI(new TessBaseAPI.ProgressNotifier() {
            @Override
            public void onProgressValues(TessBaseAPI.ProgressValues progressValues) {
                progressBar.setProgress(progressValues.getPercent());
            }
        });
        mTessBaseAPI.init(String.valueOf(Environment.getExternalStorageDirectory()), "rus");
        mTessBaseAPI.setImage(new ImageFilters().changeBitmapContrastBrightness(bitmap[0],1.8f,0));
        output = mTessBaseAPI.getUTF8Text();
        Log.d(TAG,output);
        final String[] splited= mRegex.splitForClean(output);
        final ArrayList<String> nameRec=mRegex.parseName(splited);
        final ArrayList<String> costRec=mRegex.parseCost(splited);
        final Intent intent = new Intent(context, ChoiceActivity.class);
        intent.putExtra("NameList",  nameRec);
        intent.putExtra("CostList",  costRec);
        context.startActivity(intent);
        mTessBaseAPI.end();
        return null;
    }

    @Override
    public void onProgressValues(TessBaseAPI.ProgressValues progressValues) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(progressValues.getPercent());
    }
}