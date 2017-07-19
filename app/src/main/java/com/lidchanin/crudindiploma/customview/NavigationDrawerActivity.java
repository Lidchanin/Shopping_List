package com.lidchanin.crudindiploma.customview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.activities.ShoppingListFragmentManager;
import com.lidchanin.crudindiploma.data.models.ShoppingList;
import com.lidchanin.crudindiploma.fragments.ShoppingListFragment;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by Alexander Destroyed on 08.07.2017.
 */

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG =NavigationDrawerActivity.class.getCanonicalName() ;
    private ImageButton buttonHamburger;
    private DrawerLayout drawer;
    private Uri photoUrl;
    private String accountName;
    private String accountEmail;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView headerImageView;
    private Transformation transformation;
    private TextView pageTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_drawer);
        Log.d(TAG, "setContentView");
        ViewGroup content = (ViewGroup) findViewById(R.id.container);
        if (content != null) {
            getLayoutInflater().inflate(layoutResID, content);
        }
        initNavigationDrawer();
        initializeViewsAndButtons();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            assert currentUser != null;
            photoUrl = currentUser.getPhotoUrl();
            accountName = currentUser.getDisplayName();
            accountEmail = currentUser.getEmail();
            emailTextView.setText(accountEmail);
            nameTextView.setText(accountName);
            Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation).into(headerImageView);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

    }

    private void initNavigationDrawer() {
        mAuth = FirebaseAuth.getInstance();
        transformation = new RoundedTransformationBuilder().oval(true).build();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerImageView = (ImageView) headerLayout.findViewById(R.id.headerImageView);
        emailTextView = (TextView) headerLayout.findViewById(R.id.user_mail);
        nameTextView = (TextView) headerLayout.findViewById(R.id.user_name);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }

    private void initializeViewsAndButtons() {
        pageTitle = (TextView) findViewById(R.id.title);
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this, ShoppingListFragmentManager.class);
        switch (id){
            case R.id.nav_lists:
                intent.putExtra(Constants.Bundles.FRAGMENT_ID, Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID);
                startActivity(intent);
                break;
            case R.id.nav_existing_products:
                intent.putExtra(Constants.Bundles.FRAGMENT_ID, Constants.Bundles.MANAGING_EXISTING_PRODUCTS_FRAGMENT_ID);
                startActivity(intent);
                break;
            case R.id.nav_profit:
                intent.putExtra(Constants.Bundles.FRAGMENT_ID,Constants.Bundles.PROFIT_FRAGMENT_ID);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent.putExtra(Constants.Bundles.FRAGMENT_ID,Constants.Bundles.SETTINGS_FRAGMENT_ID);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTitle(String title){
        pageTitle.setText(title);
    }
}
