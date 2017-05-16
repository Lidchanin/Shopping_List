package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.MainScreenRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

import java.util.ArrayList;
import java.util.List;

/**
 * Class <code>MainScreenActivity</code> is a activity and extends {@link AppCompatActivity}.
 * Here all shopping lists are displayed and there is a button for adding a new shopping list.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class MainScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAllShoppingLists;
    private List<ShoppingList> shoppingLists;

    private ShoppingListDAO shoppingListDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        shoppingListDAO = new ShoppingListDAO(this);

        initializeViewsAndButtons();
        initializeRecyclerViews();
        initializeData();
        initializeAdapters();
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
}
