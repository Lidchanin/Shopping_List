package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ProfitAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.models.ProfitItems;
import com.lidchanin.crudindiploma.utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfitFramgent extends Fragment {

    // TODO: 16.07.2017 new transaction in main
    private Button clearButton;
    private Button addButton;
    private RecyclerView profitRecyclerView;
    private List<ProfitItems> profitItemList;
    private int lastItemCustomized;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((NavigationDrawerActivity)getActivity()).setButtonsToDefault();
        View view = inflater.inflate(R.layout.fragment_profit,container,false);
        clearButton = (Button) view.findViewById(R.id.button_clean);
        addButton = (Button) view.findViewById(R.id.button_add_best);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        profitItemList = new ArrayList<>();
        profitItemList.add(new ProfitItems());
        profitItemList.add(new ProfitItems());
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


    private ProfitAdapter initNewAdapter() {
        final ProfitAdapter adapter = new ProfitAdapter();
        adapter.setOnSumChangeListener(new ProfitAdapter.OnSumChangeListener() {
            @Override
            public void onSumChanged(int key, List<ProfitItems> profitItems) {
                profitItemList = profitItems;
                Log.d("TESTSUM", "onSumChanged: " + profitItemList.get(MathUtils.min(profitItemList)).getSum());
                Log.d("TESTSUM", "onSumChanged: " + MathUtils.min(profitItemList));
                if(lastItemCustomized!=MathUtils.min(profitItemList)) {
                    reInitAdapter(profitItemList);
                }
            }
        });
        ((NavigationDrawerActivity) getActivity()).addNewItem(adapter);
        return new ProfitAdapter();
    }

    private void reInitAdapter(List<ProfitItems> list){
        lastItemCustomized = MathUtils.min(profitItemList);
        profitItemList = dropProfitListCustomization(profitItemList);
        profitItemList.get(MathUtils.min(profitItemList)).setCustomized(true);
        profitRecyclerView.setAdapter(new ProfitAdapter(list));
    }

    private List<ProfitItems> dropProfitListCustomization(List<ProfitItems> list){
        for(int i=0;i<list.size();i++){
            list.get(i).setCustomized(false);
        }
        return list;
    }
}
