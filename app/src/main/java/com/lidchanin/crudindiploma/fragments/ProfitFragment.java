package com.lidchanin.crudindiploma.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ProfitAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.models.ProfitItems;
import com.lidchanin.crudindiploma.utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfitFragment extends Fragment {

    // TODO: 16.07.2017 new transaction in main
    private LinearLayout firstElement;
    private LinearLayout secondElement;
    private Button clearButton;
    private Button compare;
    private EditText firstName;
    private EditText secondName;
    private EditText firstCost;
    private EditText secondCost;
    private EditText firstWeight;
    private EditText secondWeight;
    private ImageView redCross;
    private ImageView redCross2;
    private ImageView betterChoice;
    private ImageView betterChoice2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((NavigationDrawerActivity)getActivity()).setButtonsToDefault();
        View view = inflater.inflate(R.layout.fragment_profit,container,false);
        firstElement = (LinearLayout) view.findViewById(R.id.inside_first);
        secondElement = (LinearLayout) view.findViewById(R.id.inside_second);
        firstName = (EditText) view.findViewById(R.id.name_first);
        firstCost = (EditText) view.findViewById(R.id.cost_first);
        firstWeight = (EditText) view.findViewById(R.id.weight_first);
        secondName = (EditText) view.findViewById(R.id.name_second);
        secondCost = (EditText) view.findViewById(R.id.cost_second);
        secondWeight = (EditText) view.findViewById(R.id.weight_second);
        clearButton = (Button) view.findViewById(R.id.button_clean);
        compare = (Button) view.findViewById(R.id.button_compare);
        betterChoice = (ImageView) view.findViewById(R.id.best_choice);
        betterChoice2 = (ImageView) view.findViewById(R.id.best_choice2);
        redCross = (ImageView) view.findViewById(R.id.red_cross);
        redCross2 = (ImageView) view.findViewById(R.id.red_cross2);

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double firstProductCost = 0;
                double firstProductWeight = 0;
                double secondProductCost = 0;
                double secondProductWeight = 0;
                if(!TextUtils.isEmpty(firstCost.getText()) && !TextUtils.isEmpty(secondCost.getText()) && !TextUtils.isEmpty(firstWeight.getText()) && !TextUtils.isEmpty(secondWeight.getText())){
                    firstProductCost = Double.parseDouble((String.valueOf(firstCost.getText())));
                    firstProductWeight = Double.parseDouble((String.valueOf(firstWeight.getText())));
                    secondProductCost = Double.parseDouble((String.valueOf(secondCost.getText())));
                    secondProductWeight = Double.parseDouble((String.valueOf(secondWeight.getText())));
                    Toast.makeText(getContext(),"SHIT",Toast.LENGTH_LONG).show();
                    if(firstProductCost*firstProductWeight<secondProductCost*secondProductWeight){
                        dropVisibility();
                        betterChoice.setVisibility(View.VISIBLE);
                        redCross2.setVisibility(View.VISIBLE);
                    }else if(firstProductCost*firstProductWeight>secondProductCost*secondProductWeight){
                        dropVisibility();
                        redCross.setVisibility(View.VISIBLE);
                        betterChoice2.setVisibility(View.VISIBLE);
                    }else {
                       dropVisibility();
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropVisibility();
                firstElement.setVisibility(View.VISIBLE);
                secondElement.setVisibility(View.VISIBLE);
                firstName.setText("");
                firstCost.setText("");
                firstWeight.setText("");
                secondName.setText("");
                secondCost.setText("");
                secondWeight.setText("");
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        return view;
    }

    private void dropVisibility(){
        redCross.setVisibility(View.GONE);
        redCross2.setVisibility(View.GONE);
        betterChoice.setVisibility(View.GONE);
        betterChoice2.setVisibility(View.GONE);
    }


}
