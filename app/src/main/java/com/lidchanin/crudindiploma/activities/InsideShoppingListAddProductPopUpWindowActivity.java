package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.AutoCompleteProductNamesAndCostsAdapter;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;

import java.util.List;

/**
 * Class <code>InsideShoppingListAddProductPopUpWindowActivity</code> is a activity and extends
 * {@link AppCompatActivity}. Here you add products in shopping list.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class InsideShoppingListAddProductPopUpWindowActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextProductNameAndCost;
    private EditText editTextProductCost;

    private long shoppingListId;
    private List<Product> products;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_shopping_list_add_product_pop_up_window);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        productDAO = new ProductDAO(this);

        shoppingListId = getIntent().getLongExtra("shoppingListId", -1);

        initializeButtons(shoppingListId);
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
     */
    private void initializeButtons(final long shoppingListId) {
        autoCompleteTextProductNameAndCost = (AutoCompleteTextView)
                findViewById(R.id.inside_shopping_list_add_product_pop_up_window_auto_complete_text_view_product_name);
        editTextProductCost = (EditText)
                findViewById(R.id.inside_shopping_list_add_product_pop_up_window_edit_text_product_cost);

        products = productDAO.getAll();
        AutoCompleteProductNamesAndCostsAdapter adapter
                = new AutoCompleteProductNamesAndCostsAdapter(this, products);
        autoCompleteTextProductNameAndCost.setAdapter(adapter);
        autoCompleteTextProductNameAndCost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selected = (Product) parent.getAdapter().getItem(position);
                Log.d("MY_LOG", "getItem = " + selected.getId());
//                autoCompleteTextProductNameAndCost.setText(selected.getName());
                editTextProductCost.setText(String.valueOf(selected.getCost()));
            }
        });

        ImageButton imageButtonClose = (ImageButton)
                findViewById(R.id.inside_shopping_list_add_product_pop_up_window_image_button_close);
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideShoppingListAddProductPopUpWindowActivity.this,
                        InsideShoppingListActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);
            }
        });

        ImageButton imageButtonAdd = (ImageButton)
                findViewById(R.id.inside_shopping_list_add_product_pop_up_window_image_button_add);
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            // FIXME: 07.04.2017 delete all ifs
            @Override
            public void onClick(View v) {
                if (autoCompleteTextProductNameAndCost.length() <= 1 && editTextProductCost.length() == 0) {
                    Toast.makeText(InsideShoppingListAddProductPopUpWindowActivity.this,
                            "Enter name and cost!", Toast.LENGTH_SHORT).show();
                } else if (autoCompleteTextProductNameAndCost.length() <= 1) {
                    Toast.makeText(InsideShoppingListAddProductPopUpWindowActivity.this,
                            "Enter name!", Toast.LENGTH_SHORT).show();
                } else if (editTextProductCost.length() == 0) {
                    Toast.makeText(InsideShoppingListAddProductPopUpWindowActivity.this,
                            "Enter cost!", Toast.LENGTH_SHORT).show();
                } else {
                    Product product = new Product();
                    product.setName(autoCompleteTextProductNameAndCost.getText().toString());
                    product.setCost(Double.valueOf(editTextProductCost.getText().toString()));

                    // FIXME: 06.06.2017 delete this shit code
                    productDAO.open();
                    productDAO.addInCurrentShoppingList(product, shoppingListId);
                    productDAO.close();

                    Intent intent = new Intent(InsideShoppingListAddProductPopUpWindowActivity.this,
                            InsideShoppingListActivity.class);
                    intent.putExtra("shoppingListId", shoppingListId);
                    startActivity(intent);
                }
            }
        });

        /*ImageButton imageButtonScan = (ImageButton)
                findViewById(R.id.inside_shopping_list_add_product_pop_up_window_image_button_scan);
        /*imageButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideShoppingListAddProductPopUpWindowActivity.this,
                        CameraActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(InsideShoppingListAddProductPopUpWindowActivity.this,
                    InsideShoppingListActivity.class);
            intent.putExtra("shoppingListId", shoppingListId);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
