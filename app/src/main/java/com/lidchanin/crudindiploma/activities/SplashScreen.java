package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.fragments.ShoppingListFragment;
import com.lidchanin.crudindiploma.utils.SharedPrefsManager;

/**
 * Created by Alexander Destroyed on 23.10.2017.
 */

public class SplashScreen extends AppCompatActivity {

    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefsManager = new SharedPrefsManager(getApplicationContext());
        if(!sharedPrefsManager.readBoolean(Constants.Tutorial.IS_TUTORIAL)) {
            sharedPrefsManager.editBoolean(Constants.Tutorial.IS_TUTORIAL, true);
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, ShoppingListFragmentManager.class);
            startActivity(intent);
            finish();
        }
    }

}
