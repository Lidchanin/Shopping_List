package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;

public class NameAndCostEditActivity extends AppCompatActivity{

    Button doneButton;
    EditText editTextName;
    EditText editTextCost;

    private ProductDAO productDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name_and_cost);
        doneButton=(Button) findViewById(R.id.edit_done);
        editTextCost = (EditText) findViewById(R.id.edit_cost);
        editTextName = (EditText) findViewById(R.id.edit_name);
        Intent intent = getIntent();
        String nameToPut=intent.getExtras().getString("OutPutName");
        String costToPut=intent.getExtras().getString("OutPutCost");
        editTextName.setText(nameToPut);
        editTextCost.setText(costToPut);

        final Product product = new Product();
        product.setName(nameToPut);
        product.setCost(Double.parseDouble(costToPut));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME: 16.05.2017 Needed getIntent().getLongExtra("shoppingListId",-1);
                productDAO.addInCurrentShoppingList(product, 100500);
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