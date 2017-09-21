package com.lidchanin.crudindiploma.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.models.ExistingProduct;
import com.lidchanin.crudindiploma.models.Product;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.lidchanin.crudindiploma.utils.filters.DecimalDigitsInputFilter;

public class NameAndCostEditActivity extends AppCompatActivity {

    Button doneButton;
    EditText editTextName;
    EditText editTextCost;
    EditText editTextWeight;

    private long shoppingListId;

    private ProductDAO productDAO;
    private ExistingProductDAO existingProductDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name_and_cost);

        doneButton = (Button) findViewById(R.id.edit_done);
        editTextWeight = (EditText) findViewById(R.id.edit_weight);
        editTextCost = (EditText) findViewById(R.id.edit_cost);
        editTextName = (EditText) findViewById(R.id.edit_name);

        productDAO = new ProductDAO(this);
        existingProductDAO = new ExistingProductDAO(this);

        String nameToPut = getIntent().getExtras().getString("OutPutName");
        String costToPut = getIntent().getExtras().getString("OutPutCost");
        shoppingListId = getIntent().getLongExtra("shoppingListId", -1);

        editTextName.setText(nameToPut);

        editTextCost.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
        editTextCost.setText(costToPut);

        editTextWeight.setText(String.valueOf(1));


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setName(editTextName.getText().toString());
                product.setCost(Double.parseDouble(editTextCost.getText().toString()));

                ExistingProduct existingProduct = new ExistingProduct();
                existingProduct.setQuantityOrWeight(Double.parseDouble(editTextWeight.getText()
                        .toString()));

                productDAO.addInCurrentShoppingList(product, shoppingListId);

                notifyListsChanges(product, existingProduct);

               /* Intent intent = new Intent(NameAndCostEditActivity.this,
                        InsideShoppingListActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);*/
            }
        });
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
     * The method <code>notifyListsChanges</code> updates Product and ExistingProduct in lists.
     *
     * @param newProduct         is the new Product.
     * @param newExistingProduct is the  new ExistingProduct.
     */
    private void notifyListsChanges(Product newProduct,
                                    ExistingProduct newExistingProduct) {
        long tempProductId = productDAO.getOneByName(newProduct.getName()).getId();
        ExistingProduct tempExistingProduct = existingProductDAO
                .getOne(shoppingListId, tempProductId);
        tempExistingProduct.setQuantityOrWeight(newExistingProduct.getQuantityOrWeight());
        existingProductDAO.update(tempExistingProduct);
    }

}