package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.utils.DownloadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button buttonRus;
    private Button buttonEng;
    private ProgressBar progressEng;
    private ProgressBar progressRus;
    private Button buttonGoToCam;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        buttonRus = (Button) findViewById(R.id.rus_tessdata);
        buttonEng = (Button) findViewById(R.id.eng_tessdata);
        progressEng = (ProgressBar) findViewById(R.id.eng_tessdata_progress);
        progressRus = (ProgressBar) findViewById(R.id.rus_tessdata_progress);
        buttonGoToCam = (Button) findViewById(R.id.to_camera);
        progressRus.setVisibility(View.GONE);
        progressEng.setVisibility(View.GONE);
        final File rusTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH +Constants.Tessaract.TESSDATA+Constants.Tessaract.SLASH+ Constants.Tessaract.RUSTRAIN);
        final File engTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH +Constants.Tessaract.TESSDATA+Constants.Tessaract.SLASH+ Constants.Tessaract.ENGTRAIN);
        /*if (rusTessaract.exists()) {
            buttonRus.setVisibility(View.GONE);
        }
        if (engTessaract.exists()) {
            buttonEng.setVisibility(View.GONE);
        }*/
        if ((buttonEng.getVisibility() == View.GONE) && (buttonRus.getVisibility() == View.GONE)) {
            startActivity( new Intent(MainActivity.this, MainScreenActivity.class));
        } else
            buttonEng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (engTessaract.exists()) {
                        Toast.makeText(MainActivity.this, "It's also exists!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new DownloadTask(MainActivity.this, progressEng, Constants.Tessaract.ENGTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Feng.traineddata?alt=media&token=58c2aa2d-417f-4d22-87eb-80627577feb8");
                    }
                }
            });
        buttonRus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rusTessaract.exists()) {
                    Toast.makeText(MainActivity.this, "It's also exists!", Toast.LENGTH_SHORT).show();
                }
                else{
                    new DownloadTask(MainActivity.this, progressRus, Constants.Tessaract.RUSTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Frus.traineddata?alt=media&token=9cf09afa-e1bd-4f2c-b0dd-3bc457d2f5f0");
                }
            }
        });
        buttonGoToCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(engTessaract.exists()||rusTessaract.exists()) {
                    startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
                }
                else {
                    Toast.makeText(MainActivity.this, "Choose Recognize language!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}