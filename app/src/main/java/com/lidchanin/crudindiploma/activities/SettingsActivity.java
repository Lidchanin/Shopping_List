package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.utils.DownloadTask;
import com.lidchanin.crudindiploma.utils.SharedPrefsManager;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

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
    private FrameLayout turquoiseGradient;
    private FrameLayout virginGradient;
    private FrameLayout loveAndLibertyGradient;
    private FrameLayout purpleGradient;
    private Button buttonTessRus;
    private Button buttonTessEng;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
            if(getSupportActionBar()!=null){
                getSupportActionBar().hide();
            }
            initNavigationDrawer();
        sharedPrefsManager = new SharedPrefsManager(getApplicationContext());
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        blueGradient = (FrameLayout) findViewById(R.id.blue_gradient);
        purpleGradient = (FrameLayout) findViewById(R.id.blue_purple_gradient);
        virginGradient = (FrameLayout) findViewById(R.id.virgin_gradient);
        turquoiseGradient = (FrameLayout) findViewById(R.id.turquoise_gradient);
        loveAndLibertyGradient = (FrameLayout) findViewById(R.id.love_and_liberty_gradient);
        buttonTessEng = (Button) findViewById(R.id.button_eng_tess);
        buttonTessRus = (Button) findViewById(R.id.button_rus_tess);
        final File rusTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH + Constants.Tessaract.TESSDATA + Constants.Tessaract.SLASH + Constants.Tessaract.RUSTRAIN);
        final File engTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH + Constants.Tessaract.TESSDATA + Constants.Tessaract.SLASH + Constants.Tessaract.ENGTRAIN);
        buttonTessRus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefsManager.readString(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE).equals(Constants.Tessaract.ENG_TESS_SHARED)){
                    if(!rusTessaract.exists()){
                        new DownloadTask(SettingsActivity.this, Constants.Tessaract.RUSTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Frus.traineddata?alt=media&token=9cf09afa-e1bd-4f2c-b0dd-3bc457d2f5f0");
                    }
                }
            }
        });
        buttonTessEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefsManager.readString(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE).equals(Constants.Tessaract.RUS_TESS_SHARED)){
                    if(!engTessaract.exists()){
                        new DownloadTask(SettingsActivity.this, Constants.Tessaract.ENGTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Feng.traineddata?alt=media&token=58c2aa2d-417f-4d22-87eb-80627577feb8");
                    }
                    }
            }
        });
        blueGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getApplicationContext()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"blue");
                recreate();
            }
        });
        purpleGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getApplicationContext()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"purple");
                recreate();
            }
        });
        loveAndLibertyGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getApplicationContext()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"loveAndLiberty");
                recreate();
            }
        });
        turquoiseGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getApplicationContext()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"turquoise");
                recreate();
            }
        });
        virginGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefsManager(getApplicationContext()).editString(Constants.SharedPreferences.PREF_KEY_THEME,"virgin");
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
