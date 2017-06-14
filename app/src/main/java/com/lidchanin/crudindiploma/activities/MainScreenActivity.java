package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.MainScreenRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ShoppingList;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lidchanin.crudindiploma.R.id.toolbar;

/**
 * Class <code>MainScreenActivity</code> is a activity and extends {@link AppCompatActivity}.
 * Here all shopping lists are displayed and there is a button for adding a new shopping list.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerViewAllShoppingLists;
    private List<ShoppingList> shoppingLists;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initNavigationDrawer();
        initializeViewsAndButtons();
        initializeRecyclerViews();
        initializeData();
        initializeAdapters();
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
    protected void onResume() {
        super.onResume();
        shoppingListDAO.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shoppingListDAO.close();
    }

    /**
     * Method <code>initializeViewsAndButtons</code> add an actions for {@link Button}.
     */
    private void initializeViewsAndButtons() {
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });
        Button buttonAdd = (Button) findViewById(R.id.main_screen_button_add_shopping_list);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this,
                        AddingShoppingListActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Method <code>initializeData</code> reads and receives all shopping lists from the database.
     */
    private void initializeData() {
        shoppingLists = shoppingListDAO.getAll();
        if (shoppingLists == null) {
            shoppingLists = new ArrayList<>();
        }
    }

    /**
     * Method <code>initializeRecyclerViews</code> initializes {@link RecyclerView}.
     */
    public void initializeRecyclerViews() {
        recyclerViewAllShoppingLists = (RecyclerView)
                findViewById(R.id.main_screen_recycler_view_all_shopping_lists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAllShoppingLists.setLayoutManager(layoutManager);
    }

    /**
     * Method <code>initializeAdapters</code> initializes adapter for {@link RecyclerView}.
     */
    private void initializeAdapters() {
        MainScreenRecyclerViewAdapter adapter
                = new MainScreenRecyclerViewAdapter(shoppingLists, this);
        recyclerViewAllShoppingLists.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MainScreenActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_existing_products) {

        } else if (id == R.id.nav_profit) {
            startActivity(new Intent(this,ProfitActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
