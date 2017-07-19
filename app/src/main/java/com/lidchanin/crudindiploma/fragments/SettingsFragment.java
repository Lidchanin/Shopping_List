package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.utils.DownloadTask;
import com.lidchanin.crudindiploma.utils.SharedPrefsManager;

import java.io.File;

/**
 * Created by Alexander Destroyed on 16.07.2017.
 */

public class SettingsFragment extends Fragment{

    private FrameLayout blueGradient;
    private FrameLayout turquoiseGradient;
    private FrameLayout virginGradient;
    private FrameLayout loveAndLibertyGradient;
    private FrameLayout purpleGradient;
    private Button buttonTessRus;
    private Button buttonTessEng;
    private SharedPrefsManager sharedPrefsManager;

    //todo recreate problem check!
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        sharedPrefsManager = new SharedPrefsManager(getActivity());
        blueGradient = (FrameLayout) view.findViewById(R.id.blue_gradient);
        purpleGradient = (FrameLayout) view.findViewById(R.id.blue_purple_gradient);
        virginGradient = (FrameLayout) view.findViewById(R.id.virgin_gradient);
        turquoiseGradient = (FrameLayout) view.findViewById(R.id.turquoise_gradient);
        loveAndLibertyGradient = (FrameLayout) view.findViewById(R.id.love_and_liberty_gradient);
        buttonTessEng = (Button) view.findViewById(R.id.button_eng_tess);
        buttonTessRus = (Button) view.findViewById(R.id.button_rus_tess);
        final File rusTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH + Constants.Tessaract.TESSDATA + Constants.Tessaract.SLASH + Constants.Tessaract.RUSTRAIN);
        final File engTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH + Constants.Tessaract.TESSDATA + Constants.Tessaract.SLASH + Constants.Tessaract.ENGTRAIN);
        buttonTessRus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefsManager.readString(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE).equals(Constants.Tessaract.ENG_TESS_SHARED)){
                    if(!rusTessaract.exists()){
                        new DownloadTask(getActivity(), Constants.Tessaract.RUSTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Frus.traineddata?alt=media&token=9cf09afa-e1bd-4f2c-b0dd-3bc457d2f5f0");
                    }
                }
            }
        });
        buttonTessEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefsManager.readString(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE).equals(Constants.Tessaract.RUS_TESS_SHARED)){
                    if(!engTessaract.exists()){
                        new DownloadTask(getActivity(), Constants.Tessaract.ENGTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Feng.traineddata?alt=media&token=58c2aa2d-417f-4d22-87eb-80627577feb8");
                    }
                }
            }
        });
        blueGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getActivity()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"blue");
                getActivity().recreate();
            }
        });
        purpleGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getActivity()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"purple");
                getActivity().recreate();
            }
        });
        loveAndLibertyGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getActivity()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"loveAndLiberty");
                getActivity().recreate();
            }
        });
        turquoiseGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getActivity()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"turquoise");
                getActivity().recreate();
            }
        });
        virginGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getActivity()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"virgin");
                getActivity().recreate();
            }
        });

        return view;
    }
}
