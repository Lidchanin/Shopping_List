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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ProfitAdapter;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ProfitActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private Uri photoUrl;
    private String accountName;
    private String accountEmail;
    private FirebaseUser currentUser ;
    private FirebaseAuth mAuth;
    private ImageButton buttonHamburger;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView headerImageView;
    private Transformation transformation;
    private ImageButton buttonAdd;
    private RecyclerView profitRecyclerView;
    private ProfitAdapter profitAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        profitAdapter =new ProfitAdapter();
        profitRecyclerView = (RecyclerView) findViewById(R.id.recycler_profit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        profitRecyclerView.setLayoutManager(layoutManager);
        profitRecyclerView.setAdapter(profitAdapter);
        initNavigationDrawer();
        initButtons();
    }

    private void initButtons() {
        buttonAdd = (ImageButton) findViewById(R.id.add_item);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profitAdapter.addNewItem();
            }
        });
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });
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

        if (id == R.id.nav_lists) {
            startActivity(new Intent(this,MainScreenActivity.class));
        } else if (id == R.id.nav_existing_products) {

        } else if (id == R.id.nav_settings) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void initNavigationDrawer() {
        mAuth = FirebaseAuth.getInstance();
        transformation = new RoundedTransformationBuilder().oval(true).build();
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
}
