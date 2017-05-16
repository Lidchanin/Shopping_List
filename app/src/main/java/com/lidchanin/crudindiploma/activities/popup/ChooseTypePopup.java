package com.lidchanin.crudindiploma.activities.popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.activities.AddingShoppingListActivity;
import com.lidchanin.crudindiploma.activities.CameraActivity;

public class ChooseTypePopup extends Activity {

    private Button printItem;
    private Button scanItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_choose_fragment);
        getActionBar().hide();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.6),(int)( height*.2));
        printItem = (Button)findViewById(R.id.print_item);
        scanItem = (Button)findViewById(R.id.scan_item);
        printItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddingShoppingListActivity.class);
                startActivity(intent);
            }
        });
        scanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
    }
}