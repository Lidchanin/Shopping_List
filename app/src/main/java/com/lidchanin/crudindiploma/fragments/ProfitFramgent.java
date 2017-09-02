package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ProfitAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;

import java.util.HashMap;
import java.util.Map;

public class ProfitFramgent extends Fragment {

    // TODO: 16.07.2017 new transaction in main
    private Button clearButton;
    private Button addButton;
    private RecyclerView profitRecyclerView;
    private Map<Integer, Double> sumMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((NavigationDrawerActivity)getActivity()).setButtonsToDefault();
        View view = inflater.inflate(R.layout.fragment_profit,container,false);
        clearButton = (Button) view.findViewById(R.id.button_clean);

        addButton = (Button) view.findViewById(R.id.button_add_best);
        sumMap = new HashMap<>();
        profitRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_profit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        profitRecyclerView.setLayoutManager(layoutManager);
        profitRecyclerView.setAdapter(initNewAdapter());

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profitRecyclerView.setAdapter(initNewAdapter());
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        return view;
    }


    private ProfitAdapter initNewAdapter(){
        ProfitAdapter adapter = new ProfitAdapter();
        adapter.setOnSumChangeListener(new ProfitAdapter.OnSumChangeListener() {
            @Override
            public void onSumChanged(int key, double sum) {
                sumMap.put(key, sum);
            }
        });
        ((NavigationDrawerActivity) getActivity()).addNewItem(adapter);
        return adapter;
    }
}
