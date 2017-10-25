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
import com.lidchanin.crudindiploma.adapters.ProfitAdapter;
import com.lidchanin.crudindiploma.fragments.ManagingExistingProductsFragment;
import com.lidchanin.crudindiploma.fragments.ProfitFragment;
import com.lidchanin.crudindiploma.fragments.SettingsFragment;
import com.lidchanin.crudindiploma.fragments.ShoppingListFragment;
import com.lidchanin.crudindiploma.utils.SharedPrefsManager;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by Alexander Destroyed on 08.07.2017.
 */

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static final String KEY_DEFAULT_SORT_BY = "defaultSortBy";
    public static final String KEY_DEFAULT_ORDER_BY = "defaultOrderBy";
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
    private boolean defaultSortBy; // false - by date, true - alphabetically
    private boolean defaultOrderBy;
    private SharedPrefsManager sharedPrefsManager;
    private ImageButton addItem;

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
        addItem = (ImageButton) findViewById(R.id.add_button);
        alphabetSort = (ImageButton) findViewById(R.id.main_screen_image_button_sort_by_alphabet);
        dateSort = (ImageButton) findViewById(R.id.main_screen_image_button_sort_by_date);
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
                initFragment(Constants.Bundles.MANAGING_EXISTING_PRODUCTS_FRAGMENT_ID);
                break;
            case R.id.nav_profit:
                initFragment(Constants.Bundles.PROFIT_FRAGMENT_ID);
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
/*
    public void setShoppingListSorts(final List <ShoppingList> shoppingLists, final MainScreenRecyclerViewAdapter mainScreenRecyclerViewAdapter){
        sharedPrefsManager = new SharedPrefsManager(this);
        alphabetSort.setVisibility(View.VISIBLE);
        defaultSortBy = sharedPrefsManager.readBoolean(KEY_DEFAULT_SORT_BY);
        defaultOrderBy = sharedPrefsManager.readBoolean(KEY_DEFAULT_ORDER_BY);
        sortShoppingLists(shoppingLists,defaultSortBy, defaultOrderBy,mainScreenRecyclerViewAdapter);
        alphabetSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoppingLists(shoppingLists,defaultSortBy,defaultOrderBy,mainScreenRecyclerViewAdapter);
                defaultOrderBy = !defaultOrderBy;
                defaultSortBy = false;}
        });
        dateSort.setVisibility(View.VISIBLE);
        dateSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoppingLists(shoppingLists,defaultSortBy,defaultOrderBy,mainScreenRecyclerViewAdapter);
                defaultOrderBy = !defaultOrderBy;
                defaultSortBy = true;
            }
        });
    }
    */
    /*public void setInsideShoppingListSorts(final List <ShoppingList> shoppingLists, final MainScreenRecyclerViewAdapter mainScreenRecyclerViewAdapter){
        // TODO: 30.07.2017 plz , Fokin set sorts by cost and by name!
    }*/

    /*private void sortShoppingLists(List<ShoppingList> shoppingLists, final boolean lastSortedBy, final boolean lastOrderBy , MainScreenRecyclerViewAdapter mainScreenRecyclerViewAdapter) {
        Collections.sort(shoppingLists, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList s1, ShoppingList s2) {
                if (!lastSortedBy) {
                    if (!lastOrderBy) {
                        return s1.getDateOfCreation().compareToIgnoreCase(s2.getDateOfCreation());
                    } else {
                        return s2.getDateOfCreation().compareToIgnoreCase(s1.getDateOfCreation());
                    }
                } else {
                    if (!lastOrderBy) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    } else {
                        return s2.getName().compareToIgnoreCase(s1.getName());
                    }
                }
            }
        });
        sharedPrefsManager.editBoolean(KEY_DEFAULT_SORT_BY, lastSortedBy);
        sharedPrefsManager.editBoolean(KEY_DEFAULT_ORDER_BY, lastOrderBy);
        mainScreenRecyclerViewAdapter.notifyDataSetChanged();
    }*/

    public void setTitle(String title) {
        pageTitle.setText(title);
    }


    public void initFragment(String fragmentExtra) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setButtonsToDefault();
        switch (fragmentExtra) {
            case Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID:
                ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
                fragmentTransaction.replace(R.id.container, shoppingListFragment);
                fragmentTransaction.commit();
                break;
            /*case Constants.Bundles.INSIDE_SHOPPING_LIST_FRAGMENT_ID:
                InsideShoppingListFragment insideShoppingListFragment = new InsideShoppingListFragment();
                bundle.putLong(Constants.Bundles.SHOPPING_LIST_ID, shoppingListId);
                insideShoppingListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, insideShoppingListFragment);
                fragmentTransaction.commit();
                break;*/
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
            case Constants.Bundles.MANAGING_EXISTING_PRODUCTS_FRAGMENT_ID:
                ManagingExistingProductsFragment managingExistingProductsFragment = new ManagingExistingProductsFragment();
                fragmentTransaction.replace(R.id.container, managingExistingProductsFragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
