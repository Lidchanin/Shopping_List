package com.lidchanin.crudindiploma.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.utils.ThemeManager;

public class ShoppingListFragmentManager extends NavigationDrawerActivity {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.initFragment(Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(getIntent().hasExtra(Constants.Bundles.VIEWPAGER_PAGE)){
            initFragment(Constants.Bundles.SETTINGS_FRAGMENT_ID,
                    getIntent().getIntExtra(Constants.Bundles.VIEWPAGER_PAGE,0));
        }else {
            initFragment(Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID);
        }
    }
}
