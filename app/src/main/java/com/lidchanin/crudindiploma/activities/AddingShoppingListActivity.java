package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

/**
 * Class <code>AddingShoppingListActivity</code> is a activity and extends
 * {@link AppCompatActivity}. Here you can create a new shopping list.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class AddingShoppingListActivity extends AppCompatActivity {

    private EditText editTextEnterNameShoppingList;

    private ShoppingListDAO shoppingListDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_shopping_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        shoppingListDAO = new ShoppingListDAO(this);

        initializeButtons();
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
     * Method <code>initializeViewsAndButtons</code> initialize views and buttons and add them an
     * actions or other properties.
     */
    public void initializeButtons() {
        editTextEnterNameShoppingList = (EditText)
                findViewById(R.id.adding_shopping_list_edit_text_enter_shopping_list_name);
        Button buttonAddShoppingList = (Button)
                findViewById(R.id.adding_shopping_list_button_add_shopping_list);
        buttonAddShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEnterNameShoppingList.getText().length() >= 3) {
                    long shoppingListId = shoppingListDAO.add(
                            new ShoppingList(editTextEnterNameShoppingList.getText().toString()));
                    Intent intent = new Intent(AddingShoppingListActivity.this,
                            InsideShoppingListActivity.class);
                    intent.putExtra("shoppingListId", shoppingListId);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Uncorrected name!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(AddingShoppingListActivity.this, MainScreenActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
