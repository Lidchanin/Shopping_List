package com.lidchanin.crudindiploma.utils;

import android.content.Context;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Destroyed on 01.12.2017.
 */

public class TutorialManager {

    public static List<String> getTutorialTextList(Context context){
        List<String> tutorialTextList = new ArrayList<>();
        tutorialTextList.add(context.getResources().getString(R.string.tutorial_screen_first));
        tutorialTextList.add(context.getResources().getString(R.string.tutorial_screen_second));
        tutorialTextList.add(context.getResources().getString(R.string.tutorial_screen_third));
        return tutorialTextList;
    }

    public static List<Integer> getTutorialImageList(Context context){
        List<Integer> tutorialImageList = new ArrayList<>();
        tutorialImageList.add(R.mipmap.tutorial_first);
        tutorialImageList.add(R.mipmap.tutorial_first);
        tutorialImageList.add(R.mipmap.tutorial_first);
        return tutorialImageList;
    }
}
