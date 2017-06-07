package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.MainScreenRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ShoppingList;
import com.lidchanin.crudindiploma.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class <code>MainScreenActivity</code> is a activity and extends {@link AppCompatActivity}.
 * Here all shopping lists are displayed and there is a button for adding a new shopping list.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class MainScreenActivity extends AppCompatActivity {

    public static final String KEY_DEFAULT_SORT_BY = "defaultSortBy";
    public static final String KEY_DEFAULT_ORDER_BY = "defaultOrderBy";

    private RecyclerView recyclerViewAllShoppingLists;
    private MainScreenRecyclerViewAdapter mainScreenRecyclerViewAdapter;

    private boolean defaultSortBy; // false - by date, true - alphabetically
    private boolean defaultOrderBy;

    private List<ShoppingList> shoppingLists;

    private SharedPrefsManager sharedPrefsManager;

    private ShoppingListDAO shoppingListDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        shoppingListDAO = new ShoppingListDAO(this);

        initializeData();
        initializeRecyclerViews();
        initializeAdapters();
        initializeViewsAndButtons();

        startedSortShoppingList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingListDAO.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shoppingListDAO.close();
    }

    /**
     * Method <code>initializeViewsAndButtons</code> initializes {@link Button}s and
     * {@link android.widget.ImageButton}s.
     */
    private void initializeViewsAndButtons() {
        initializeButtonAdd();
        initializeButtonSortByAlphabet();
        initializeButtonSortByDate();
    }

    /**
     * Method <code>initializeButtonAdd</code> initializes {@link Button} for adding shopping list.
     */
    private void initializeButtonAdd() {
        Button button = (Button) findViewById(R.id.main_screen_button_add_shopping_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenActivity.this,
                        AddingShoppingListActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Method <code>initializeButtonSortByAlphabet</code> initializes {@link ImageButton} for
     * sorting all shopping lists by alphabet.
     */
    private void initializeButtonSortByAlphabet() {
        ImageButton button = (ImageButton)
                findViewById(R.id.main_screen_image_button_sort_by_alphabet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoppingLists(defaultSortBy, defaultOrderBy);
                defaultOrderBy = !defaultOrderBy;
                defaultSortBy = true;
            }
        });
    }

    /**
     * Method <code>initializeButtonSortByDate</code> initializes {@link ImageButton} for
     * sorting all shopping lists by date.
     */
    private void initializeButtonSortByDate() {
        ImageButton button = (ImageButton)
                findViewById(R.id.main_screen_image_button_sort_by_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoppingLists(defaultSortBy, defaultOrderBy);
                defaultOrderBy = !defaultOrderBy;
                defaultSortBy = false;
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
        mainScreenRecyclerViewAdapter
                = new MainScreenRecyclerViewAdapter(shoppingLists, this);
        recyclerViewAllShoppingLists.setAdapter(mainScreenRecyclerViewAdapter);
    }

    /**
     * The method <code>sortShoppingLists</code> sorts shopping lists by name or by date.
     *
     * @param lastSortedBy is the last sorted value.
     * @param lastOrderBy is the last ordered value.
     */
    private void sortShoppingLists(final boolean lastSortedBy, final boolean lastOrderBy) {
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
    }

    /**
     * The method <code>startedSortShoppingList</code> sorts shopping lists, when activity start.
     */
    private void startedSortShoppingList() {
        sharedPrefsManager = new SharedPrefsManager(this);
        defaultSortBy = sharedPrefsManager.readBoolean(KEY_DEFAULT_SORT_BY);
        defaultOrderBy = sharedPrefsManager.readBoolean(KEY_DEFAULT_ORDER_BY);
        sortShoppingLists(defaultSortBy, defaultOrderBy);
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
}
