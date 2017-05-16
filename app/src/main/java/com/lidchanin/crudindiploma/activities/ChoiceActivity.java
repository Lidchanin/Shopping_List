package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ChoiceAdapter;

import java.util.ArrayList;


public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_choice);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final Intent intent=getIntent();
        final ArrayList<String> nameChoice = intent.getStringArrayListExtra("NameList");
        final ArrayList<String> costChoice = intent.getStringArrayListExtra("CostList");
        final ChoiceAdapter mChoiceAdapter= new ChoiceAdapter(nameChoice,costChoice);
        recyclerView.setAdapter(mChoiceAdapter);
    }
}