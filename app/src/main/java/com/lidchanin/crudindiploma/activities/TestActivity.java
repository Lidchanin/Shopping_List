package com.lidchanin.crudindiploma.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.database.DaoMaster;
import com.lidchanin.crudindiploma.database.DaoSession;
import com.lidchanin.crudindiploma.database.ExistingProduct;
import com.lidchanin.crudindiploma.database.ExistingProductDao;
import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ProductDao;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.ShoppingListDao;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ShoppingListDao shoppingListDao;
    private ProductDao productDao;
    private ExistingProductDao existingProductDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "database", null);
        database = helper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        shoppingListDao = daoSession.getShoppingListDao();
        productDao = daoSession.getProductDao();
        existingProductDao = daoSession.getExistingProductDao();

        Button button = (Button) findViewById(R.id.test_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingListDao.deleteAll();
                productDao.deleteAll();
                existingProductDao.deleteAll();
//                shListAdd();
//                productAdd();
//                shListGetAll();
//                productGetAll();
//                getExistingProductsFromCurrentShoppingList();
            }
        });

        daoSession.clear();
    }

    private void shListAdd() {
        ShoppingList shoppingList1 = new ShoppingList(null, "ListA", "202020");
        ShoppingList shoppingList2 = new ShoppingList(null, "ListB", "152505");
        ShoppingList shoppingList3 = new ShoppingList(null, "ListC", "303030");

        shoppingListDao.insert(shoppingList1);
        shoppingListDao.insert(shoppingList2);
        shoppingListDao.insert(shoppingList3);
    }

    private List<ShoppingList> shListGetAll() {
        List<ShoppingList> shoppingLists = shoppingListDao.loadAll();
        for (int i = 0; i < shoppingLists.size(); i++) {
            Log.d(TAG, "id = " + shoppingLists.get(i).getId() + " name = " +
                    shoppingLists.get(i).getName());
        }
        return shoppingLists;
    }

    private void shListDelete() {
        shoppingListDao.deleteByKey((long) 1);
        shoppingListDao.deleteByKey((long) 3);
        shoppingListDao.deleteByKey((long) 5);
    }

    private void productAdd() {
        Product product1 = new Product(null, "prA", 10.5, (long) 1);
        Product product2 = new Product(null, "prB", 11.5, (long) 2);
        Product product3 = new Product(null, "prC", 12.5, (long) 3);

        productDao.insert(product1);
        productDao.insert(product2);
        productDao.insert(product3);
    }

    private List<Product> productGetAll() {
        List<Product> products = productDao.loadAll();
        for (int i = 0; i < products.size(); i++) {
            Log.d(TAG, "id = " + products.get(i).getId() + " name = " +
                    products.get(i).getName());
        }
        return products;
    }

    private List<ExistingProduct> getExistingProductsFromCurrentShoppingList() {
        ShoppingList shoppingList = shoppingListDao.load((long) 1);

        ExistingProduct ex1 = new ExistingProduct(null, 0.5, true, (long) 1, 1);
        ExistingProduct ex2 = new ExistingProduct(null, 0.5, true, (long) 2, 1);

        existingProductDao.insert(ex1);
        existingProductDao.insert(ex2);

        List<ExistingProduct> existingProducts = shoppingList.getExistingProducts();
        for (int i = 0; i < existingProducts.size(); i++) {
            Log.d(TAG, "id exP = " + existingProducts.get(i).getId()
                    + " quantity = " + existingProducts.get(i).getQuantity()
                    + " \n---productId = " + existingProducts.get(i).getProduct().getId()
                    + " productName = " + existingProducts.get(i).getProduct().getName());
        }

        return existingProducts;
    }
}
