package com.lidchanin.crudindiploma.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.fragments.InsideShoppingListFragment;
import com.lidchanin.crudindiploma.fragments.ShoppingListFragment;
import com.lidchanin.crudindiploma.utils.ThemeManager;

/**
 * Created by Alexander Destroyed on 08.07.2017.
 */

public class ShoppingListFragmentManager extends NavigationDrawerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container,ShoppingListFragment.getInstance());
        fragmentTransaction.commit();
        if(getIntent().hasExtra(Constants.Bundles.SHOPPING_LIST_ID)) {
            long tempListId = getIntent().getLongExtra(Constants.Bundles.SHOPPING_LIST_ID, -1);
            FragmentTransaction secondFragmentTransaction =getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.Bundles.SHOPPING_LIST_ID,tempListId);
            InsideShoppingListFragment insideShoppingListActivity  = new InsideShoppingListFragment();
            insideShoppingListActivity.setArguments(bundle);
            secondFragmentTransaction.add(R.id.container,insideShoppingListActivity);
            secondFragmentTransaction.commit();
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK) {
            new InsideShoppingListActivity().createAndShowAlertDialogTopFive();
            return true;
        }
        return super.onKeyDown(keyCode, event);*/
        //// TODO: 06.07.2017 fix by fragments
        /*if (keyCode == KeyEvent.KEYCODE_BACK) {
            createAndShowAlertDialogForExit();
        }
        return super.onKeyDown(keyCode, event);*/

        return true;
    }
}
