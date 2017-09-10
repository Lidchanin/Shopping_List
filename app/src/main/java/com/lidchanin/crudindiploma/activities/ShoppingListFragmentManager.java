package com.lidchanin.crudindiploma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.utils.ThemeManager;

public class ShoppingListFragmentManager extends NavigationDrawerActivity {


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.initFragment(-1, Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID);
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

        long tempListId = 0;
        if (getIntent().hasExtra(Constants.Bundles.SHOPPING_LIST_ID)) {
            tempListId = getIntent().getLongExtra(Constants.Bundles.SHOPPING_LIST_ID, -1);
        }
        initFragment(tempListId, Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID);
    }

    private void createAndShowAlertDialogForExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.ask_close_app, getString(R.string.app_name)));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
