package com.lidchanin.crudindiploma.ocr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.activities.ChoiceActivity;
import com.lidchanin.crudindiploma.utils.ScanLogs;

import java.util.ArrayList;
public class Recognize extends AsyncTask<Bitmap,TessBaseAPI.ProgressValues,Void> implements TessBaseAPI.ProgressNotifier {
    private final String TAG=this.getClass().getSimpleName();
    private final Regex mRegex= new Regex();
    private ProgressBar progressBar;
    private Context context;
    private long shoppingListId;
    SharedPreferences sharedPreferences;
    public Recognize(ProgressBar progressBar, final Context context,long shoppingListId){
        this.context=context;
        this.progressBar= progressBar;
        this.shoppingListId = shoppingListId;
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
        mTessBaseAPI.init(String.valueOf(Environment.getExternalStorageDirectory()), sharedPreferences.getString(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE,""));
        mTessBaseAPI.setImage(new ImageFilters().changeBitmapContrastBrightness(context, bitmap[0],1.8f,0));
        output = mTessBaseAPI.getUTF8Text();
        Log.d(TAG,output);
        new ScanLogs(output,context);
        final String[] splited= mRegex.splitForClean(output);
        final ArrayList<String> nameRec=mRegex.parseName(splited);
        final ArrayList<String> costRec=mRegex.parseCost(splited);
        final Intent intent = new Intent(context, ChoiceActivity.class);
        Log.d(TAG, String.valueOf(shoppingListId));
        intent.putExtra("shoppingListId",shoppingListId);
        intent.putExtra("NameList",  nameRec);
        intent.putExtra("CostList",  costRec);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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