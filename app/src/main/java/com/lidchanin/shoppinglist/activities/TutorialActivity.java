package com.lidchanin.shoppinglist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lidchanin.shoppinglist.R;
import com.lidchanin.shoppinglist.adapters.TutorialPagerAdapter;
import com.lidchanin.shoppinglist.customview.DesignedViewPager;
import com.lidchanin.shoppinglist.utils.TutorialManager;

import java.util.List;


public class TutorialActivity extends AppCompatActivity {

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
        final List<Integer> tutorialImages = TutorialManager.getTutorialImageList(this);
        List<String> tutorialStrings = TutorialManager.getTutorialTextList(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(designedViewPager, true);
        previous = (Button) findViewById(R.id.previous);
        previous.setVisibility(View.GONE);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(designedViewPager.getCurrentItem()-1>=0) {
                    designedViewPager.setCurrentItem(designedViewPager.getCurrentItem()-1);
                }
                if(designedViewPager.getCurrentItem() == 0 ){
                    previous.setVisibility(View.GONE);
                }
            }
        });
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous.setVisibility(View.VISIBLE);
                if(designedViewPager.getCurrentItem()+1<tutorialImages.size()) {
                    designedViewPager.setCurrentItem(designedViewPager.getCurrentItem()+1);
                }else {
                    // TODO: 03.12.2017 make animation to hide load
                    startActivity(new Intent(TutorialActivity.this, ShoppingListFragmentManager.class));
                }
            }
        });
        designedViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(designedViewPager.getCurrentItem() == 0 ){
                    previous.setVisibility(View.GONE);
                }else {
                    previous.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        designedViewPager.setAdapter(new TutorialPagerAdapter(getSupportFragmentManager(), tutorialImages, tutorialStrings));
    }



}
