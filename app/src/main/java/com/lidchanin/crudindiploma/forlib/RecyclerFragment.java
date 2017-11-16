package com.lidchanin.crudindiploma.forlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.utils.ThemeManager;

import java.util.List;

/**
 * Created by Alexander Destroyed on 12.10.2017.
 */

public class RecyclerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        RecyclerView mainRV = (RecyclerView) view.findViewById(R.id.design_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainRV.setLayoutManager(layoutManager);
        List<RecyclerViewItems> list = ThemeManager.getInstance(getContext())
                .getThemes(getArguments().getString("List"));
        RecyclerViewAdapter mainRVAdapter = new RecyclerViewAdapter(list,
                getActivity(), getArguments().getInt(Constants.Bundles.VIEWPAGER_PAGE));
        mainRV.setAdapter(mainRVAdapter);
        return view;
    }
}
