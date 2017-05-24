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
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;

public class InsideShoppingListUpdateProductPopUpWindowActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextCost;

    private ProductDAO productDAO;

    private long productId;
    private long shoppingListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_shopping_list_update_product_pop_up_window);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        productDAO = new ProductDAO(this);
        productId = getIntent().getLongExtra("productId", -1);
        shoppingListId = getIntent().getLongExtra("shoppingListId", -1);

        initializeButtons(shoppingListId, productId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        productDAO.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        productDAO.close();
    }

    /**
     * Method <code>initializeButtons</code> initialize button and add an actions for
     * {@link Button}s.
     *
     * @param shoppingListId is the current shopping list id.
     * @param productId      is the current product id.
     */
    private void initializeButtons(final long shoppingListId, final long productId) {
        editTextName = (EditText) findViewById(
                R.id.inside_shopping_list_update_product_pop_up_window_edit_text_product_name);
        String previousName = productDAO.getOneById(productId).getName();
        editTextName.setText(previousName);

        editTextCost = (EditText) findViewById(
                R.id.inside_shopping_list_update_product_pop_up_window_edit_text_product_cost);
        String previousCost = String.valueOf(productDAO.getOneById(productId).getCost());
        editTextCost.setText(previousCost);

        ImageButton closeButton = (ImageButton) findViewById(
                R.id.inside_shopping_list_update_product_pop_up_window_image_button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideShoppingListUpdateProductPopUpWindowActivity.this,
                        InsideShoppingListActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                intent.putExtra("productId", productId);
                startActivity(intent);
            }
        });

        ImageButton imageButtonConfirm = (ImageButton) findViewById(
                R.id.inside_shopping_list_update_product_pop_up_window_image_button_confirm);
        imageButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.length() <= 1 && Integer.valueOf(editTextCost.getText().toString()) <= 0) {
                    Toast.makeText(InsideShoppingListUpdateProductPopUpWindowActivity.this,
                            "Enter name and cost!", Toast.LENGTH_SHORT).show();
                } else {
                    Product product = new Product();
                    product.setId(productId);
                    product.setName(editTextName.getText().toString());
                    product.setCost(Double.valueOf(editTextCost.getText().toString()));
                    product.setPopularity(productDAO.getOneById(productId).getPopularity());
                    productDAO.update(product);

                    Intent intent = new Intent(InsideShoppingListUpdateProductPopUpWindowActivity.this,
                            InsideShoppingListActivity.class);
                    intent.putExtra("shoppingListId", shoppingListId);
                    intent.putExtra("productId", productId);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(InsideShoppingListUpdateProductPopUpWindowActivity.this,
                    InsideShoppingListActivity.class);
            intent.putExtra("productId", productId);
            intent.putExtra("shoppingListId", shoppingListId);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
