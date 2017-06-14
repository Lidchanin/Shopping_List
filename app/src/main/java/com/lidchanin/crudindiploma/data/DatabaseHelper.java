package com.lidchanin.crudindiploma.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Class <code>DatabaseHelper</code> extends {@link SQLiteOpenHelper}, which manages database
 * creation and version management. This class creates an “Personal shopping list” with three
 * tables “shopping_lists”, “products” and “existing_products”.
 *
 * @author Lidchanin
 * @see SQLiteOpenHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "personal_shopping_lists";

    public static final String TABLE_SHOPPING_LISTS = "shopping_lists";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_EXISTING_PRODUCTS = "existing_products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_LIST_ID = "list_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_QUANTITY_OR_WEIGHT = "quantity_or_weight";
    public static final String COLUMN_DATE_OF_CREATION = "date_of_creation";

    private static final String CREATE_TABLE_SHOPPING_LISTS
            = "CREATE TABLE " + TABLE_SHOPPING_LISTS
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_DATE_OF_CREATION + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";
    private static final String CREATE_TABLE_PRODUCTS
            = "CREATE TABLE " + TABLE_PRODUCTS
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_COST + " REAL, "
            + COLUMN_POPULARITY + " INTEGER"
            + ");";
    private static final String CREATE_TABLE_EXISTED_PRODUCTS
            = "CREATE TABLE " + TABLE_EXISTING_PRODUCTS
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + COLUMN_LIST_ID + " INTEGER REFERENCES " + TABLE_SHOPPING_LISTS + " (" + COLUMN_ID + "), "
            + COLUMN_PRODUCT_ID + " INTEGER REFERENCES " + TABLE_PRODUCTS + " (" + COLUMN_ID + "), "
            + COLUMN_QUANTITY_OR_WEIGHT + " REAL DEFAULT 1"
            + ");";

    private static DatabaseHelper instance;

    private Context context;

    /**
     * Simple constructor for create <code>DatabaseHelper</code> exemplar.
     *
     * @param context is the context, where you want to use <code>DatabaseHelper</code>
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SHOPPING_LISTS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_EXISTED_PRODUCTS);
        loadDefaultProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXISTING_PRODUCTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Method <code>loadDefaultProducts</code> loads products into the database.
     *
     * @param db is the SQLite database.
     */
    private void loadDefaultProducts(SQLiteDatabase db) {
        List<Product> defaultProducts = defaultProducts();
        for (int i = 0; i < defaultProducts.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME, defaultProducts.get(i).getName());
            contentValues.put(COLUMN_COST, defaultProducts.get(i).getCost());
            contentValues.put(COLUMN_POPULARITY, defaultProducts.get(i).getPopularity());
            db.insert(TABLE_PRODUCTS, null, contentValues);
        }
    }

    /**
     * Method <code>defaultProducts</code> fills the list with products.
     *
     * @return default products list.
     */
    private List<Product> defaultProducts() {
        List<Product> defaultProducts = new ArrayList<>();
        defaultProducts.add(new Product(context.getString(R.string.potatoes), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.cabbage), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.carrot), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.tomatoes), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.cucumbers), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.garlic), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.onion), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.beetroot), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.apples), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.bananas), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.oranges), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.lemons), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.butter), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.olive_oil), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.sunflower_oil), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.milk), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.fish), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.peas), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.corn), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.mushrooms), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.chicken_meat), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.pork), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.beef), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.spaghetti), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.rice), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.buckwheat), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.mustard), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.eggs), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.sugar), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.salt), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.coffee), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.black_tea), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.green_tea), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.red_tea), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.cocoa), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.dish_soap), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.white_bread), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.bread), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.juice), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.cheese), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.sour_cream), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.ketchup), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.mayonnaise), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.garbage_bag), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.gum), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.flour), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.honey), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.vinegar), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.margarine), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.soda), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.bay_leaf), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.radish), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.tomato_paste), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.condensed_milk), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.chocolate), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.foil_for_baking), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.nuts), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.beans), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.dried_fruits), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.yogurt), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.cookie), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.toilet_paper), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.pita), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.waffles), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.ice_cream), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.smoked_sausage), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.sausage), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.pate), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.dumplings), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.spice), 0, 1));
        defaultProducts.add(new Product(context.getString(R.string.chips), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.muesli), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.marshmallows), 0, 0));
        defaultProducts.add(new Product(context.getString(R.string.marmalade), 0, 0));
        return defaultProducts;
    }
}