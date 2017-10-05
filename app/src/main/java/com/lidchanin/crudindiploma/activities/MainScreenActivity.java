package com.lidchanin.crudindiploma.activities;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.MainRVAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.database.DaoMaster;
import com.lidchanin.crudindiploma.database.DaoSession;
import com.lidchanin.crudindiploma.database.ExistingProductDao;
import com.lidchanin.crudindiploma.database.ProductDao;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.ShoppingListDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainScreenActivity extends NavigationDrawerActivity {

//    private ShoppingListDAO shoppingListDAO;
//    private ProductDAO productDAO;
//    private ExistingProductDAO existingProductDAO;

    private static final String TAG = "MainScreenActivity";

    private List<ShoppingList> shoppingLists;

    private RecyclerView mainRV;
    private Button buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main_screen);
//        shoppingListDAO = new ShoppingListDAO(this);
//        shoppingLists = shoppingListDAO.getAll();
//        productDAO = new ProductDAO(this);
//        existingProductDAO = new ExistingProductDAO(this);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "database", null);
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();
        final ShoppingListDao shoppingListDao = daoSession.getShoppingListDao();
        ProductDao productDao = daoSession.getProductDao();
        ExistingProductDao existingProductDao = daoSession.getExistingProductDao();

        shoppingLists = shoppingListDao.loadAll();

        mainRV = (RecyclerView) findViewById(R.id.main_screen_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRV.setLayoutManager(layoutManager);
        mainRV.setAdapter(new MainRVAdapter(this, shoppingListDao,
                productDao, existingProductDao, shoppingLists));

        buttonAdd = (Button) findViewById(R.id.main_screen_add_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForAdd(shoppingListDao);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        shoppingListDAO.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        shoppingListDAO.close();
    }

    /**
     * The method <b>createAndShowAlertDialogForAdd</b> creates and shows a dialog, which
     * need to create new {@link ShoppingList}.
     */
    private void createAndShowAlertDialogForAdd(final ShoppingListDao shoppingListDao) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setTitle(R.string.add_a_new_shopping_list);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(getApplicationContext());
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(getString(R.string.enter_name));

        final TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.addView(editTextName);

        builder.setView(textInputLayout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextName.getText() != null
                        && editTextName.getText().toString().length() != 0) {
                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setName(editTextName.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            getString(R.string.database_date_format), Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());
                    shoppingList.setDate(currentDateAndTime);
                    long shoppingListId = shoppingListDao.insert(shoppingList);
                    Log.d(TAG, "shopping list id = " + shoppingListId);
                    // TODO: 08.09.2017 fix recreate
                    recreate();

                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_name,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForAdd(shoppingListDao);
                }
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}