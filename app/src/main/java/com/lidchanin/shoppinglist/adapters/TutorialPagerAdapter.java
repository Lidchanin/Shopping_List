package com.lidchanin.shoppinglist.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lidchanin.shoppinglist.Constants;
import com.lidchanin.shoppinglist.fragments.TutorialFragment;

import java.util.List;


/**
 * Created by Alexander Destroyed on 01.12.2017.
 */

public class TutorialPagerAdapter extends FragmentPagerAdapter{

    private List<Integer> tutorialImages;
    private List<String> tutorialText;

    public TutorialPagerAdapter(FragmentManager fm, List<Integer> tutorialImages, List<String> tutorialText) {
        super(fm);
        this.tutorialImages = tutorialImages;
        this.tutorialText = tutorialText;
    }

    @Override
    public int getCount() {
        return tutorialImages.size();
    }

    @Override
    public Fragment getItem(int position) {
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.Tutorial.IMAGES, tutorialImages.get(position));
        bundle.putString(Constants.Tutorial.STRINGS, tutorialText.get(position));
        tutorialFragment.setArguments(bundle);
        return tutorialFragment;
    }
}
