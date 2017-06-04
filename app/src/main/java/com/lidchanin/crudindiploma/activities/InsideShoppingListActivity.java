package com.lidchanin.crudindiploma.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.InsideShoppingListRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class <code>InsideShoppingListActivity</code> is a activity and extends
 * {@link AppCompatActivity}. Here all products in current shopping list are displayed and
 * there is a button for adding a new product in current shopping list.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class InsideShoppingListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAllProducts;

    private List<Product> products;
    private List<ExistingProduct> existingProducts;
    private long shoppingListId;
    private double costsSum = 0;
    private Button scan;
    private Button type;
    private ShoppingListDAO shoppingListDAO;
    private ProductDAO productDAO;
    private ExistingProductDAO existingProductDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_shopping_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        shoppingListId = getIntent().getLongExtra("shoppingListId", -1);

        shoppingListDAO = new ShoppingListDAO(this);
        productDAO = new ProductDAO(this);
        existingProductDAO = new ExistingProductDAO(this);

        initializeData(shoppingListId);
        initializeViewsAndButtons(shoppingListId);
        initializeRecyclerViews();
        initializeAdapters();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingListDAO.open();
        productDAO.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shoppingListDAO.close();
        productDAO.close();
    }

    /**
     * Method <code>initializeData</code> reads and receives all data in current shopping list
     * from the database.
     *
     * @param shoppingListId is the current shopping list id.
     */
    private void initializeData(long shoppingListId) {
        products = productDAO.getAllFromCurrentShoppingList(shoppingListId);
        if (products == null) {
            products = new ArrayList<>();
        }
        existingProducts = existingProductDAO.getAllFromCurrentShoppingList(shoppingListId);
        if (existingProducts == null) {
            existingProducts = new ArrayList<>();
        } else {
            for (int i = 0; i < existingProducts.size(); i++) {
                double existingProductCost = existingProducts.get(i).getTotalCost();
                if (existingProductCost == 0) {
                    costsSum += products.get(i).getCost();
                } else {
                    costsSum += existingProducts.get(i).getTotalCost();
                }
            }
            Log.d("MY_LOG", "" + costsSum);
        }
    }

    /**
     * Method <code>initializeViewsAndButtons</code> initialize views and buttons and add them an
     * actions or other properties.
     *
     * @param shoppingListId is the current shopping list id.
     */
    private void initializeViewsAndButtons(final long shoppingListId) {
        // FIXME: 06.04.2017 fab is need to fix?
        type = (Button) findViewById(R.id.enter_by_type);
        scan = (Button) findViewById(R.id.scan);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideShoppingListActivity.this,
                        InsideShoppingListAddProductPopUpWindowActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideShoppingListActivity.this,
                        CameraActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);

            }
        });

        String shoppingListName = shoppingListDAO.getOne(shoppingListId).getName();
        TextView textViewShoppingListName = (TextView)
                findViewById(R.id.inside_shopping_list_text_view_shopping_list_name);
        textViewShoppingListName.setText(shoppingListName);

        TextView textViewCostsSum = (TextView)
                findViewById(R.id.inside_shopping_list_text_view_products_costs_sum);
        textViewCostsSum.setText(getString(R.string.estimated_amount,
                new DecimalFormat("#.##").format(costsSum)));
    }

    /**
     * Method <code>initializeRecyclerViews</code> initializes {@link RecyclerView}s.
     */
    public void initializeRecyclerViews() {
        recyclerViewAllProducts
                = (RecyclerView) findViewById(R.id.inside_shopping_list_recycler_view_all_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAllProducts.setLayoutManager(layoutManager);
    }

    /**
     * Method <code>initializeAdapters</code> initializes adapter for {@link RecyclerView}.
     */
    private void initializeAdapters() {
        InsideShoppingListRecyclerViewAdapter adapter = new InsideShoppingListRecyclerViewAdapter(
                products, existingProducts, this, shoppingListId);
        recyclerViewAllProducts.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            createAndShowAlertDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Method <code>createAndShowAlertDialog</code> creates and displays an alert dialog. Dialog
     * reminding the user that he forgot to buy.
     */
    private void createAndShowAlertDialog() {
        AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.are_you_forgot);
        final List<Product> topFiveProducts = productDAO.getTopFiveProducts(products);
        final String[] productsNames = new String[5];
        for (int i = 0; i < productsNames.length; i++) {
            productsNames[i] = topFiveProducts.get(i).getName();
        }
        final Boolean[] states = new Boolean[productsNames.length];
        Arrays.fill(states, false);
        builder.setMultiChoiceItems(productsNames, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        states[which] = isChecked;
                    }
                });
        builder.setPositiveButton(R.string.add_selected_products,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (allFalse(states)) {
                            Toast.makeText(InsideShoppingListActivity.this,
                                    getString(R.string.you_did_not_select_anything),
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            for (int i = 0; i < 5; i++) {
                                if (states[i]) {
                                    productDAO.assignProductToShoppingList(shoppingListId,
                                            topFiveProducts.get(i).getId());
                                }
                            }
                            Intent intent = new Intent(InsideShoppingListActivity.this,
                                    InsideShoppingListActivity.class);
                            intent.putExtra("shoppingListId", shoppingListId);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });
        builder.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(InsideShoppingListActivity.this,
                        MainScreenActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(InsideShoppingListActivity.this,
                        MainScreenActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Method <code>allFalse</code> checks the boolean array and gives us the answer that all
     * elements are false or not.
     *
     * @param states contains all checkboxes states.
     * @return if true all elements are false, else true.
     */
    private static boolean allFalse(Boolean[] states) {
        for (boolean state : states) {
            if (state)
                return false;
        }
        return true;
    }
}
