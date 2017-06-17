package com.lidchanin.crudindiploma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.AutoCompleteProductNamesAndCostsAdapter;
import com.lidchanin.crudindiploma.adapters.InsideShoppingListRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
public class InsideShoppingListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerViewAllProducts;
    private InsideShoppingListRecyclerViewAdapter recyclerViewAdapter;
    private TextView textViewEstimatedAmount;

    private List<Product> products;
    private List<ExistingProduct> existingProducts;
    private long shoppingListId;

    private ShoppingListDAO shoppingListDAO;
    private ExistingProductDAO existingProductDAO;
    private ProductDAO productDAO;

    private Button scan;
    private Button type;
    private DrawerLayout drawer;
    private Uri photoUrl;
    private String accountName;
    private String accountEmail;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ImageButton buttonHamburger;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView headerImageView;
    private Transformation transformation;

    /**
     * The method <code>allFalse</code> checks the boolean array and gives us the answer that all
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_shopping_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        shoppingListId = getIntent().getLongExtra("shoppingListId", -1);
        shoppingListDAO = new ShoppingListDAO(this);
        productDAO = new ProductDAO(this);
        existingProductDAO = new ExistingProductDAO(this);

        initNavigationDrawer();

        initializeData(shoppingListId);
        initializeTextViewWithCostsSum();
        initializeRecyclerViews();
        initializeAdapters();
        initializeViewsAndButtons(shoppingListId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingListDAO.open();
        productDAO.open();
        existingProductDAO.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shoppingListDAO.close();
        productDAO.close();
        existingProductDAO.close();
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
            calculationOfEstimatedAmount(products, existingProducts);
        }
    }

    /**
     * Method <code>initializeViewsAndButtons</code> initialize views and buttons and add them an
     * actions or other properties.
     *
     * @param shoppingListId is the current shopping list id.
     */
    private void initializeViewsAndButtons(final long shoppingListId) {
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        type = (Button) findViewById(R.id.enter_by_type);
        scan = (Button) findViewById(R.id.scan);
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForManualType();
            }
        });

        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideShoppingListActivity.this,
                        CameraActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);
            }
        });

        initializeTextViewWithShoppingListName();
    }

    /**
     * The method <code>initializeTextViewWithShoppingListName</code> initializes the
     * textViewShoppingListName and adds some actions to it.
     */
    private void initializeTextViewWithShoppingListName() {
        String shoppingListName = shoppingListDAO.getOne(shoppingListId).getName();
        TextView textViewShoppingListName = (TextView)
                findViewById(R.id.inside_shopping_list_text_view_shopping_list_name);
        textViewShoppingListName.setText(shoppingListName);
    }

    /**
     * The method <code>initializeTextViewWithCostsSum</code> initializes the textViewCostsSum and
     * adds some actions to it.
     */
    private void initializeTextViewWithCostsSum() {
        textViewEstimatedAmount = (TextView)
                findViewById(R.id.inside_shopping_list_text_view_products_costs_sum);
        setTextForTextViewCostsSum(textViewEstimatedAmount);
    }

    /**
     * The method <code>setTextForTextViewCostsSum</code> adds text to TextView with estimated
     * amount.
     *
     * @param textViewCostsSum is the TextView with estimated amount.
     */
    private void setTextForTextViewCostsSum(TextView textViewCostsSum) {
        textViewCostsSum.setText(getString(R.string.estimated_amount,
                new DecimalFormat("#.##").format(
                        calculationOfEstimatedAmount(products, existingProducts))));
    }

    private void initNavigationDrawer() {
        mAuth = FirebaseAuth.getInstance();
        transformation = new RoundedTransformationBuilder().oval(true).build();
        shoppingListDAO = new ShoppingListDAO(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerImageView = (ImageView) headerLayout.findViewById(R.id.headerImageView);
        emailTextView = (TextView) headerLayout.findViewById(R.id.user_mail);
        nameTextView = (TextView) headerLayout.findViewById(R.id.user_name);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }

    /**
     * The method <code>initializeRecyclerView</code> initializes {@link RecyclerView}.
     */
    private void initializeRecyclerViews() {
        recyclerViewAllProducts
                = (RecyclerView) findViewById(R.id.inside_shopping_list_recycler_view_all_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAllProducts.setLayoutManager(layoutManager);
    }

    /**
     * Method <code>initializeAdapters</code> initializes recyclerViewAdapter for {@link RecyclerView}.
     */
    private void initializeAdapters() {
        recyclerViewAdapter = new InsideShoppingListRecyclerViewAdapter(
                products, existingProducts,
                productDAO, existingProductDAO,
                this, shoppingListId);
        recyclerViewAdapter.setOnDataChangeListener(new InsideShoppingListRecyclerViewAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(List<ExistingProduct> existingProducts) {
                setTextForTextViewCostsSum(textViewEstimatedAmount);
            }
        });
        recyclerViewAllProducts.setAdapter(recyclerViewAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            createAndShowAlertDialogTopFive();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Method <code>createAndShowAlertDialogTopFive</code> creates and displays an alert dialog.
     * Dialog reminding the user that he forgot to buy.
     */
    private void createAndShowAlertDialogTopFive() {
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
                                    Product newProduct = topFiveProducts.get(i);
                                    boolean existence = productDAO.addInCurrentShoppingListAndCheck(
                                            newProduct, shoppingListId);
                                    notifyListsChanges(existence, newProduct,
                                            new ExistingProduct(1));
                                }
                            }
                            setTextForTextViewCostsSum(textViewEstimatedAmount);
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
     * The method <code>createAndShowAlertDialogForManualType</code> create and shows a dialog,
     * which need to manual adding product.
     */
    private void createAndShowAlertDialogForManualType() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_new_product);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextCost = new EditText(this);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(getString(R.string.enter_cost));
        editTextCost.setText("0");

        final AutoCompleteTextView autoCompleteTextViewName = new AutoCompleteTextView(this);
        autoCompleteTextViewName.setInputType(InputType.TYPE_CLASS_TEXT);
        autoCompleteTextViewName.setHint(getString(R.string.enter_name));
        List<Product> allProducts = productDAO.getAll();
        AutoCompleteProductNamesAndCostsAdapter autoCompleteAdapter
                = new AutoCompleteProductNamesAndCostsAdapter(this, allProducts);
        autoCompleteTextViewName.setAdapter(autoCompleteAdapter);
        autoCompleteTextViewName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selected = (Product) parent.getAdapter().getItem(position);
                editTextCost.setText(String.valueOf(selected.getCost()));
            }
        });

        final EditText editTextQuantity = new EditText(this);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(getString(R.string.enter_quantity));
        editTextQuantity.setText(String.valueOf(1));

        layout.addView(autoCompleteTextViewName);
        layout.addView(editTextCost);
        layout.addView(editTextQuantity);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (autoCompleteTextViewName.getText() != null
                        && autoCompleteTextViewName.getText().toString().length() != 0
                        && editTextCost.getText() != null
                        && editTextCost.getText().toString().length() != 0
                        && editTextQuantity.getText() != null
                        && editTextQuantity.getText().toString().length() != 0) {
                    Product newProduct = new Product();
                    newProduct.setName(autoCompleteTextViewName.getText().toString());
                    newProduct.setCost(Double.valueOf(editTextCost.getText().toString()));

                    ExistingProduct newExistingProduct = new ExistingProduct(Double
                            .parseDouble(editTextQuantity.getText().toString()));

                    boolean existence = productDAO
                            .addInCurrentShoppingListAndCheck(newProduct, shoppingListId);

                    notifyListsChanges(existence, newProduct, newExistingProduct);
                    setTextForTextViewCostsSum(textViewEstimatedAmount);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_all_data,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForManualType();
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

    /**
     * The method <code>notifyListsChanges</code> updates Product and ExistingProduct in lists
     * on screen.
     *
     * @param existence          is the existence Product in lists.
     * @param newProduct         is the new Product.
     * @param newExistingProduct is the new ExistingProduct.
     */
    private void notifyListsChanges(boolean existence, Product newProduct,
                                    ExistingProduct newExistingProduct) {
        long tempProductId = productDAO.getOneByName(newProduct.getName()).getId();
        ExistingProduct tempExistingProduct = existingProductDAO
                .getOne(shoppingListId, tempProductId);
        tempExistingProduct.setQuantityOrWeight(newExistingProduct.getQuantityOrWeight());
        existingProductDAO.update(tempExistingProduct);

        if (!existence) {
            products.add(products.size(), newProduct);
            existingProducts.add(tempExistingProduct);
            recyclerViewAdapter.notifyItemInserted(products.size());
        } else {
            for (Product p : products) {
                if (p.getName() != null && p.getName().contains(newProduct.getName())) {
                    int position = products.indexOf(p);
                    products.set(position, newProduct);
                    existingProducts.set(position, newExistingProduct);
                    recyclerViewAdapter.notifyItemChanged(position);
                    break;
                }
            }
        }
    }

    /**
     * The method <code>calculationOfEstimatedAmount</code> is calculating total amount of costs
     * products.
     *
     * @param products         - all products from shopping list.
     * @param existingProducts - all existing products from shopping list.
     * @return estimated amount.
     */
    private double calculationOfEstimatedAmount(List<Product> products,
                                                List<ExistingProduct> existingProducts) {
        double estimatedAmount = 0;
        for (int i = 0; i < existingProducts.size(); i++) {
            double productCost = products.get(i).getCost();
            double existingProductQuantity = existingProducts.get(i).getQuantityOrWeight();
            estimatedAmount += productCost * existingProductQuantity;
        }
        return estimatedAmount;
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            assert currentUser != null;
            photoUrl = currentUser.getPhotoUrl();
            accountName = currentUser.getDisplayName();
            accountEmail = currentUser.getEmail();
            emailTextView.setText(accountEmail);
            nameTextView.setText(accountName);
            Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation).into(headerImageView);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_lists) {
            startActivity(new Intent(this, MainScreenActivity.class));
        } else if (id == R.id.nav_existing_products) {
            startActivity(new Intent(this, ManagingExistingProductsActivity.class));
        } else if (id == R.id.nav_profit) {
            startActivity(new Intent(this, ProfitActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
