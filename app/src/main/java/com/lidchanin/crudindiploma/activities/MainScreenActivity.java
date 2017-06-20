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
import com.lidchanin.crudindiploma.adapters.MainScreenRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ShoppingList;
import com.lidchanin.crudindiploma.utils.SharedPrefsManager;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class <code>MainScreenActivity</code> is a activity and extends {@link AppCompatActivity}.
 * Here all shopping lists are displayed and there is a button for adding a new shopping list.
 *
 * @author Lidchanin
 * @see android.app.Activity
 */
public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_DEFAULT_SORT_BY = "defaultSortBy";
    public static final String KEY_DEFAULT_ORDER_BY = "defaultOrderBy";

    private RecyclerView recyclerViewAllShoppingLists;
    private MainScreenRecyclerViewAdapter mainScreenRecyclerViewAdapter;

    private boolean defaultSortBy; // false - by date, true - alphabetically
    private boolean defaultOrderBy;

    private List<ShoppingList> shoppingLists;
    private ImageButton buttonHamburger;

    private SharedPrefsManager sharedPrefsManager;

    private ShoppingListDAO shoppingListDAO;
    private DrawerLayout drawer;
    private Uri photoUrl;
    private String accountName;
    private String accountEmail;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView headerImageView;
    private Transformation transformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initNavigationDrawer();
        initializeViewsAndButtons();
        initializeRecyclerViews();

        shoppingListDAO = new ShoppingListDAO(this);

        initializeData();
        initializeRecyclerViews();
        initializeAdapters();
        initializeViewsAndButtons();

        startedSortShoppingList();
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
     * Method <code>initializeViewsAndButtons</code> initializes {@link Button}s and
     * {@link android.widget.ImageButton}s.
     */
    private void initializeViewsAndButtons() {
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        initializeButtonAdd();
        initializeButtonSortByAlphabet();
        initializeButtonSortByDate();
    }

    /**
     * Method <code>initializeButtonAdd</code> initializes {@link Button} for adding shopping list.
     */
    private void initializeButtonAdd() {
        Button button = (Button) findViewById(R.id.main_screen_button_add_shopping_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForAdd();
            }
        });
    }

    /**
     * Method <code>initializeButtonSortByAlphabet</code> initializes {@link ImageButton} for
     * sorting all shopping lists by alphabet.
     */
    private void initializeButtonSortByAlphabet() {
        ImageButton button = (ImageButton)
                findViewById(R.id.main_screen_image_button_sort_by_alphabet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoppingLists(defaultSortBy, defaultOrderBy);
                defaultOrderBy = !defaultOrderBy;
                defaultSortBy = true;
            }
        });
    }

    /**
     * Method <code>initializeButtonSortByDate</code> initializes {@link ImageButton} for
     * sorting all shopping lists by date.
     */
    private void initializeButtonSortByDate() {
        ImageButton button = (ImageButton)
                findViewById(R.id.main_screen_image_button_sort_by_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortShoppingLists(defaultSortBy, defaultOrderBy);
                defaultOrderBy = !defaultOrderBy;
                defaultSortBy = false;
            }
        });
    }

    /**
     * Method <code>initializeData</code> reads and receives all shopping lists from the database.
     */
    private void initializeData() {
        shoppingLists = shoppingListDAO.getAll();
        if (shoppingLists == null) {
            shoppingLists = new ArrayList<>();
        }
    }

    /**
     * Method <code>initializeRecyclerView</code> initializes {@link RecyclerView}.
     */
    public void initializeRecyclerViews() {
        recyclerViewAllShoppingLists = (RecyclerView)
                findViewById(R.id.main_screen_recycler_view_all_shopping_lists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAllShoppingLists.setLayoutManager(layoutManager);
    }

    /**
     * Method <code>initializeAdapters</code> initializes adapter for {@link RecyclerView}.
     */
    private void initializeAdapters() {
        mainScreenRecyclerViewAdapter
                = new MainScreenRecyclerViewAdapter(shoppingLists, shoppingListDAO, this);
        recyclerViewAllShoppingLists.setAdapter(mainScreenRecyclerViewAdapter);
    }

    /**
     * The method <code>sortShoppingLists</code> sorts shopping lists by name or by date.
     *
     * @param lastSortedBy is the last sorted value.
     * @param lastOrderBy  is the last ordered value.
     */
    private void sortShoppingLists(final boolean lastSortedBy, final boolean lastOrderBy) {
        Collections.sort(shoppingLists, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList s1, ShoppingList s2) {
                if (!lastSortedBy) {
                    if (!lastOrderBy) {
                        return s1.getDateOfCreation().compareToIgnoreCase(s2.getDateOfCreation());
                    } else {
                        return s2.getDateOfCreation().compareToIgnoreCase(s1.getDateOfCreation());
                    }
                } else {
                    if (!lastOrderBy) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    } else {
                        return s2.getName().compareToIgnoreCase(s1.getName());
                    }
                }
            }
        });
        sharedPrefsManager.editBoolean(KEY_DEFAULT_SORT_BY, lastSortedBy);
        sharedPrefsManager.editBoolean(KEY_DEFAULT_ORDER_BY, lastOrderBy);
        mainScreenRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * The method <code>startedSortShoppingList</code> sorts shopping lists, when activity start.
     */
    private void startedSortShoppingList() {
        sharedPrefsManager = new SharedPrefsManager(this);
        defaultSortBy = sharedPrefsManager.readBoolean(KEY_DEFAULT_SORT_BY);
        defaultOrderBy = sharedPrefsManager.readBoolean(KEY_DEFAULT_ORDER_BY);
        sortShoppingLists(defaultSortBy, defaultOrderBy);
    }

    /**
     * The method <code>createAndShowAlertDialogForAdd</code> create and shows a dialog, which
     * need to add new shopping list.
     */
    private void createAndShowAlertDialogForAdd() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(R.string.add_a_new_shopping_list);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(this);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(getString(R.string.enter_name));

        builder.setView(editTextName);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextName.getText() != null
                        && editTextName.getText().toString().length() != 0) {
                    ShoppingList temp = new ShoppingList();
                    temp.setName(editTextName.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            getString(R.string.database_date_format), Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());
                    temp.setDateOfCreation(currentDateAndTime);
                    long shoppingListId = shoppingListDAO.add(temp);
                    Intent intent = new Intent(MainScreenActivity.this,
                            InsideShoppingListActivity.class);
                    intent.putExtra("shoppingListId", shoppingListId);
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    // FIXME: 09.06.2017 alert dialog for add
                    Toast.makeText(getApplicationContext(), R.string.please_enter_name,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForAdd();
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
     * The method <code>createAndShowAlertDialogForExit</code> create and shows a dialog, which
     * need to close application.
     */
    private void createAndShowAlertDialogForExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.ask_close_app, getString(R.string.app_name)));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            createAndShowAlertDialogForExit();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_existing_products) {
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
