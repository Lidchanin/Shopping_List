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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ManagingExistingProductsRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Class <code>ManagingExistingProductsRecyclerViewAdapter</code> is a activity and extends
 * {@link AppCompatActivity}. Here all products are displayed and there is a button for deleting
 * and editing product in database.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class ManagingExistingProductsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerViewAllProducts;

    private ProductDAO productDAO;

    private List<Product> products;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_existing_products);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        initNavigationDrawer();
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        initializeData();
        initializeRecyclerView();
        initializeAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        productDAO.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productDAO.close();
    }

    /**
     * Method <code>initializeData</code> receives all products from the database.
     */
    private void initializeData() {
        productDAO = new ProductDAO(this);
        products = productDAO.getAll();
        if (products == null) {
            products = new ArrayList<>();
        }
    }

    /**
     * Method <code>initializeRecyclerView</code> initializes {@link RecyclerView}.
     */
    private void initializeRecyclerView() {
        recyclerViewAllProducts = (RecyclerView)
                findViewById(R.id.managing_existing_products_recycler_view_all_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAllProducts.setLayoutManager(layoutManager);
    }

    /**
     * Method <code>initializeAdapter</code> initializes adapter for {@link RecyclerView}.
     */
    private void initializeAdapter() {
        ManagingExistingProductsRecyclerViewAdapter adapter
                = new ManagingExistingProductsRecyclerViewAdapter(products, productDAO, this);
        recyclerViewAllProducts.setAdapter(adapter);
    }

    void initNavigationDrawer() {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_lists) {
            startActivity(new Intent(this, MainScreenActivity.class));
        } else if (id == R.id.nav_profit) {

        } else if (id == R.id.nav_settings) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, MainScreenActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
}
