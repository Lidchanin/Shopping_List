package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;

/**
 * Created by Alexander Destroyed on 01.12.2017.
 */

public class TutorialFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        ImageView imageView =(ImageView) view.findViewById(R.id.tutorial_image);
        TextView textView = (TextView) view.findViewById(R.id.tutorial_text);
        if(getArguments()!=null){
            imageView.setImageResource(getArguments().getInt(Constants.Tutorial.IMAGES));
            textView.setText(getArguments().getString(Constants.Tutorial.STRINGS));
        }
        return view;
    }
}
