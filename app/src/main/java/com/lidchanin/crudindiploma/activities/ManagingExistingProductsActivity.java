package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ManagingExistingProductsRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_existing_products);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        productDAO = new ProductDAO(this);
        products = productDAO.getAll();
        if (products == null) {
            products = new ArrayList<>();
        }

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
     * Method <code>initializeRecyclerView</code> initializes {@link RecyclerView}s.
     */
    public void initializeRecyclerView() {
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
                = new ManagingExistingProductsRecyclerViewAdapter(products, this);
        recyclerViewAllProducts.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_lists) {
            startActivity(new Intent(this, MainScreenActivity.class));
        } else if (id == R.id.nav_existing_products) {
            startActivity(new Intent(this, ManagingExistingProductsActivity.class));
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
            // TODO: 08.06.2017 need code to onKeyDown
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
