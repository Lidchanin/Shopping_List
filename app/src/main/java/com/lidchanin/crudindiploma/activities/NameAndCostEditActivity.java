package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.lidchanin.crudindiploma.utils.filters.DecimalDigitsInputFilter;

public class NameAndCostEditActivity extends AppCompatActivity{

    Button doneButton;
    EditText editTextName;
    EditText editTextCost;
    EditText editTextWeight;

    private ProductDAO productDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name_and_cost);
        doneButton=(Button) findViewById(R.id.edit_done);
        editTextWeight = (EditText) findViewById(R.id.edit_weight);
        editTextCost = (EditText) findViewById(R.id.edit_cost);
        editTextName = (EditText) findViewById(R.id.edit_name);
        productDAO = new ProductDAO(this);
        String nameToPut=getIntent().getExtras().getString("OutPutName");
        String costToPut=getIntent().getExtras().getString("OutPutCost");
        final long shoppingListId = getIntent().getLongExtra("shoppingListId", -1);
        editTextName.setText(nameToPut);
        editTextCost.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
        editTextCost.setText(costToPut);

        final Product product = new Product();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 15.06.2017 Add weight and product to list and DB
                productDAO.open();
                product.setName(editTextName.getText().toString());
                product.setCost(Double.parseDouble(editTextCost.getText().toString()));
                productDAO.addInCurrentShoppingList(product, shoppingListId);
                Intent intent= new Intent(NameAndCostEditActivity.this,InsideShoppingListActivity.class);
                intent.putExtra("shoppingListId",shoppingListId);
                startActivity(intent);
            }
        });

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
}