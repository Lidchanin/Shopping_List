package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
        File f = new File(Environment.getExternalStorageDirectory() + Constants.Tessaract.SLASH + Constants.Tessaract.RUSTRAIN);
        if (f.exists()) {
            buttonRus.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "BOOM!", Toast.LENGTH_SHORT).show();
        }
        if ((buttonEng.getVisibility() == View.GONE) && (buttonRus.getVisibility() == View.GONE)) {
            final Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
            startActivity(intent);
        } else
            buttonEng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DownloadTask(MainActivity.this, progressEng, Constants.Tessaract.ENGTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Feng.traineddata?alt=media&token=58c2aa2d-417f-4d22-87eb-80627577feb8");
                }
            });
        buttonRus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this, progressRus, Constants.Tessaract.RUSTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Frus.traineddata?alt=media&token=9cf09afa-e1bd-4f2c-b0dd-3bc457d2f5f0");
            }
        });
        buttonGoToCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((progressRus.getProgress() < 100 && progressRus.getProgress() > 0) || (progressRus.getProgress() > 0 && progressEng.getProgress() < 100)) {
                }
                startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
            }
        });
    }
}