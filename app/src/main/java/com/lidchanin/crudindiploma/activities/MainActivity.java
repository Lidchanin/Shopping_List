package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.TutorialPagerAdapter;
import com.lidchanin.crudindiploma.forlib.DesignedViewPager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DesignedViewPager designedViewPager;
    private TabLayout tabLayout;
    private Button previous;
    private Button next;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        designedViewPager = (DesignedViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 01.12.2017 move it to previous
            }
        });
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 01.12.2017 move it to next
                startActivity(new Intent(MainActivity.this, ShoppingListFragmentManager.class));
            }
        });
        List<Integer> tutorialImages = new ArrayList<>();
        List<String> tutorialStrings = new ArrayList<>();
        tutorialImages.add(R.mipmap.tutorial_first);
        tutorialImages.add(R.mipmap.tutorial_first);
        tutorialImages.add(R.mipmap.tutorial_first);
        tutorialStrings.add(getString(R.string.lists));
        tutorialStrings.add(getString(R.string.lists));
        tutorialStrings.add(getString(R.string.lists));

        designedViewPager.setAdapter(new TutorialPagerAdapter(getSupportFragmentManager(), tutorialImages, tutorialStrings));
    }



}
