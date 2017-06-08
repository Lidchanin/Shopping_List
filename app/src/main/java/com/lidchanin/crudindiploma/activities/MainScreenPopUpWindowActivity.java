package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

public class MainScreenPopUpWindowActivity extends AppCompatActivity {

    private EditText editTextName;

    private ShoppingListDAO shoppingListDAO;

    private long shoppingListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_pop_up_window);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        getWindow().setLayout((int) (width * .8), ActionBar.LayoutParams.WRAP_CONTENT);

        shoppingListDAO = new ShoppingListDAO(this);

        shoppingListId = getIntent().getLongExtra("shoppingListId", -1);

        initializeButtons(shoppingListId);
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
     * Method <code>initializeButtons</code> initialize button and add an actions for
     * {@link Button}s.
     *
     * @param shoppingListId is the current shopping list id.
     */
    private void initializeButtons(final long shoppingListId) {
        ImageButton closeButton = (ImageButton)
                findViewById(R.id.main_screen_image_button_close_in_pop_up_window);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreenPopUpWindowActivity.this,
                        MainScreenActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);
            }
        });

        editTextName = (EditText)
                findViewById(R.id.main_screen_edit_text_shopping_list_name_in_pop_up_window);
        String previousName = shoppingListDAO.getOne(shoppingListId).getName();
        editTextName.setText(previousName);

        ImageButton imageButtonConfirm = (ImageButton)
                findViewById(R.id.main_screen_image_button_confirm_in_pop_up_window);
        imageButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.length() <= 1) {
                    Toast.makeText(MainScreenPopUpWindowActivity.this,
                            "Enter name!", Toast.LENGTH_SHORT).show();
                } else {
                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setId(shoppingListId);
                    shoppingList.setName(editTextName.getText().toString());
                    shoppingListDAO.update(shoppingList);
                    Intent intent = new Intent(MainScreenPopUpWindowActivity.this,
                            MainScreenActivity.class);
                    intent.putExtra("shoppingListId", shoppingListId);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MainScreenPopUpWindowActivity.this,
                    MainScreenActivity.class);
            intent.putExtra("shoppingListId", shoppingListId);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
