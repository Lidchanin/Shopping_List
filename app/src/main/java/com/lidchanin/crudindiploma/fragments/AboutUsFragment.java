package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidchanin.crudindiploma.R;

/**
 * Class extends {@link Fragment}.
 *
 * @author Lidchanin
 * @see android.support.v4.app.Fragment
 */
public class AboutUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        return view;
    }
}
