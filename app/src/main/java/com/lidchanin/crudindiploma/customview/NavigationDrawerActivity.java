package com.lidchanin.crudindiploma.customview;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import com.lidchanin.crudindiploma.adapters.ProfitAdapter;
import com.lidchanin.crudindiploma.fragments.AllProductsFragment;
import com.lidchanin.crudindiploma.fragments.ProfitFragment;
import com.lidchanin.crudindiploma.fragments.SettingsFragment;
import com.lidchanin.crudindiploma.fragments.ShoppingListFragment;
import com.lidchanin.crudindiploma.fragments.StatisticsFragment;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = NavigationDrawerActivity.class.getCanonicalName();

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
    private ImageButton alphabetSort;
    private ImageButton dateSort;
    private ImageButton addItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_drawer);
        ViewGroup content = (ViewGroup) findViewById(R.id.container);
        if (content != null) {
            getLayoutInflater().inflate(layoutResID, content);
        }
        initNavigationDrawer();
        initializeViewsAndButtons();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            photoUrl = currentUser.getPhotoUrl();
            accountName = currentUser.getDisplayName();
            accountEmail = currentUser.getEmail();
            emailTextView.setText(accountEmail);
            nameTextView.setText(accountName);
            Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation)
                    .into(headerImageView);
        }
    }

    private void initNavigationDrawer() {
        mAuth = FirebaseAuth.getInstance();
        transformation = new RoundedTransformationBuilder().oval(true).build();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        addItem = (ImageButton) findViewById(R.id.add_button);
        alphabetSort = (ImageButton) findViewById(R.id.main_screen_image_button_sort_by_alphabet);
        dateSort = (ImageButton) findViewById(R.id.main_screen_image_button_sort_by_date);
        headerImageView = (ImageView) headerLayout.findViewById(R.id.headerImageView);
        emailTextView = (TextView) headerLayout.findViewById(R.id.user_mail);
        nameTextView = (TextView) headerLayout.findViewById(R.id.user_name);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }

    public void setButtonsToDefault() {
        addItem.setVisibility(View.GONE);
        alphabetSort.setVisibility(View.GONE);
        dateSort.setVisibility(View.GONE);
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
        switch (id) {
            case R.id.nav_lists:
                initFragment(Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID);
                break;
            case R.id.nav_existing_products:
                initFragment(Constants.Bundles.ALL_PRODUCTS_FRAGMENT_ID);
                break;
            case R.id.nav_profit:
                initFragment(Constants.Bundles.PROFIT_FRAGMENT_ID);
                break;
            case R.id.nav_statistics:
                initFragment(Constants.Bundles.STATISTICS_FRAGMENT_ID);
                break;
            case R.id.nav_settings:
                initFragment(Constants.Bundles.SETTINGS_FRAGMENT_ID);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addNewItem(final ProfitAdapter profitAdapter) {
        addItem.setVisibility(View.VISIBLE);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profitAdapter.addNewItem();
            }
        });
    }

    public ImageButton addNewItem() {
        return addItem;
    }

    public void setTitle(String title) {
        pageTitle.setText(title);
    }

    public void initFragment(String fragmentExtra) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (fragmentExtra) {
            case Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID:
                ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
                fragmentTransaction.replace(R.id.container, shoppingListFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.STATISTICS_FRAGMENT_ID:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                fragmentTransaction.replace(R.id.container, statisticsFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.PROFIT_FRAGMENT_ID:
                ProfitFragment profitFragment = new ProfitFragment();
                fragmentTransaction.replace(R.id.container, profitFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.SETTINGS_FRAGMENT_ID:
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.container, settingsFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.ALL_PRODUCTS_FRAGMENT_ID:
                AllProductsFragment allProductsFragment = new AllProductsFragment();
                fragmentTransaction.replace(R.id.container, allProductsFragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
