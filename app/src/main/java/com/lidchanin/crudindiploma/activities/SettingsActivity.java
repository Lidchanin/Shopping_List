package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.utils.SharedPrefsManager;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton buttonHamburger;
    private ShoppingListDAO shoppingListDAO;
    private DrawerLayout drawer;
    private Uri photoUrl;
    private String accountName;
    private String accountEmail;
    private FirebaseUser currentUser ;
    private FirebaseAuth mAuth;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView headerImageView;
    private Transformation transformation;
    private FrameLayout blueGradient;
    private FrameLayout purpleGradient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
            if(getSupportActionBar()!=null){
                getSupportActionBar().hide();
            }
            initNavigationDrawer();
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        blueGradient = (FrameLayout) findViewById(R.id.blue_gradient);
        blueGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getApplicationContext()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"blue");
                recreate();
            }
        });
        purpleGradient = (FrameLayout) findViewById(R.id.blue_purple_gradient);
        purpleGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getApplicationContext()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"purple");
                recreate();
            }
        });
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });
    }

    private void initNavigationDrawer() {
        mAuth = FirebaseAuth.getInstance();
        transformation = new RoundedTransformationBuilder().oval(true).build();
        shoppingListDAO = new ShoppingListDAO(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerImageView =(ImageView) headerLayout.findViewById(R.id.headerImageView);
        emailTextView =(TextView) headerLayout.findViewById(R.id.user_mail);
        nameTextView =(TextView) headerLayout.findViewById(R.id.user_name);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            assert currentUser != null;
            photoUrl=currentUser.getPhotoUrl();
            accountName = currentUser.getDisplayName();
            accountEmail = currentUser.getEmail();
            emailTextView.setText(accountEmail);
            nameTextView.setText(accountName);
            Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation).into(headerImageView);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_existing_products) {
            startActivity(new Intent(this,ManagingExistingProductsActivity.class));
        }else if (id == R.id.nav_lists) {
            startActivity(new Intent(this,MainScreenActivity.class));
        }else if (id == R.id.nav_profit) {
            startActivity(new Intent(this,ProfitActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
