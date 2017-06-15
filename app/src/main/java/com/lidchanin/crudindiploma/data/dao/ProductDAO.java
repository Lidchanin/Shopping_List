package com.lidchanin.crudindiploma.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import com.lidchanin.crudindiploma.data.ShoppingListCursorWrapper;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;

import java.util.ArrayList;
import java.util.List;

import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_COST;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_LIST_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_NAME;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_POPULARITY;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_PRODUCT_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_QUANTITY_OR_WEIGHT;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_EXISTING_PRODUCTS;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_PRODUCTS;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_SHOPPING_LISTS;

/**
 * The class <code>ProductDAO</code> extends {@link DatabaseDAO} and implements database operations
 * such as add, update, delete, get {@link Product}.
 *
 * @author Lidchanin
 */
public class ProductDAO extends DatabaseDAO {

    private static final String WHERE_ID_EQUALS = COLUMN_ID + " =?";
    private static final String WHERE_NAME_EQUALS = COLUMN_NAME + " =?";
    private static final String WHERE_LIST_ID_EQUALS = COLUMN_LIST_ID + " =?";
    private static final String WHERE_PRODUCT_ID_EQUALS = COLUMN_PRODUCT_ID + " =?";

    public ProductDAO(Context context) {
        super(context);
    }

    /**
     * The method <code>getContentValuesProducts</code> fills the ContentValues with the
     * {@link Product}.
     *
     * @param product is the product.
     * @return filled ContentValues.
     */
    private static ContentValues getContentValuesProducts(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, product.getName());
        contentValues.put(COLUMN_COST, product.getCost());
        contentValues.put(COLUMN_POPULARITY, product.getPopularity());
        return contentValues;
    }

    /**
     * The method <code>getContentValuesExistingProducts</code> fills the ContentValues with the
     * {@link ExistingProductDAO}.
     * <p>
     * //     * @param shoppingListId is the shopping list id.
     * //     * @param productId      is the product id.
     *
     * @return filled ContentValues.
     */
    private static ContentValues getContentValuesExistingProducts(ExistingProduct existingProduct) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LIST_ID, existingProduct.getShoppingListId());
        contentValues.put(COLUMN_PRODUCT_ID, existingProduct.getProductId());
        contentValues.put(COLUMN_QUANTITY_OR_WEIGHT, existingProduct.getQuantityOrWeight());
        return contentValues;
    }

    /**
     * The method <code>queryProducts</code> wraps the {@link Cursor}.
     *
     * @param columns       A list of which columns to return. Passing null will return all columns,
     *                      which is discouraged to prevent reading data from storage that isn't
     *                      going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE
     *                      clause (excluding the WHERE itself). Passing null will return all rows
     *                      for the given table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in order that they appear in the selection. The
     *                      values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY
     *                      clause (excluding the GROUP BY itself). Passing null will cause the rows
     *                      to not be grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row
     *                      grouping is being used, formatted as an SQL HAVING clause (excluding
     *                      the HAVING itself). Passing null will cause all row groups to be
     *                      included, and is required when row grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding
     *                      the ORDER BY itself). Passing null will use the default sort order,
     *                      which may be unordered.
     * @param limit         Limits the number of rows returned by the query, formatted as LIMIT
     *                      clause. Passing null denotes no LIMIT clause.
     * @return wrapped cursor.
     */
    private ShoppingListCursorWrapper queryProducts(String[] columns, String selection,
                                                    String[] selectionArgs, String groupBy,
                                                    String having, String orderBy,
                                                    String limit) {
        Cursor cursor = database.query(TABLE_PRODUCTS, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        return new ShoppingListCursorWrapper(cursor);
    }

    /**
     * The method <code>queryExistingProducts</code> wraps the {@link Cursor}.
     *
     * @param columns       A list of which columns to return. Passing null will return all columns,
     *                      which is discouraged to prevent reading data from storage that isn't
     *                      going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE
     *                      clause (excluding the WHERE itself). Passing null will return all rows
     *                      for the given table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in order that they appear in the selection. The
     *                      values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY
     *                      clause (excluding the GROUP BY itself). Passing null will cause the rows
     *                      to not be grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row
     *                      grouping is being used, formatted as an SQL HAVING clause (excluding
     *                      the HAVING itself). Passing null will cause all row groups to be
     *                      included, and is required when row grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding
     *                      the ORDER BY itself). Passing null will use the default sort order,
     *                      which may be unordered.
     * @param limit         Limits the number of rows returned by the query, formatted as LIMIT
     *                      clause. Passing null denotes no LIMIT clause.
     * @return wrapped cursor.
     */
    private ShoppingListCursorWrapper queryExistingProducts(String[] columns, String selection,
                                                            String[] selectionArgs, String groupBy,
                                                            String having, String orderBy,
                                                            String limit) {
        Cursor cursor = database.query(TABLE_EXISTING_PRODUCTS, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        return new ShoppingListCursorWrapper(cursor);
    }

    /**
     * The method <code>getOneById</code> gets product by id in the database.
     *
     * @param productId is the product id, which you want to get.
     * @return product, which you need, or null.
     */
    public Product getOneById(long productId) {
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_COST, COLUMN_POPULARITY};
        String selection = WHERE_ID_EQUALS;
        String[] selectionArgs = {String.valueOf(productId)};
        String limit = String.valueOf(1);
        ShoppingListCursorWrapper cursor = queryProducts(columns, selection, selectionArgs, null,
                null, null, limit);
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProduct();
        } finally {
            cursor.close();
        }
    }

    /**
     * The method <code>getOneByName</code> gets product by name in the database.
     *
     * @param productName is the product name, which you want to get.
     * @return product, which you need, or null.
     */
    public Product getOneByName(String productName) {
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_COST, COLUMN_POPULARITY};
        String selection = WHERE_NAME_EQUALS;
        String[] selectionArgs = {productName};
        String limit = String.valueOf(1);
        ShoppingListCursorWrapper cursor = queryProducts(columns, selection, selectionArgs, null,
                null, null, limit);
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProduct();
        } finally {
            cursor.close();
        }
    }

    /**
     * The method <code>getAll</code> gets all products from the database.
     *
     * @return all products, which you need.
     */
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_COST, COLUMN_POPULARITY};
        database.beginTransaction();
        ShoppingListCursorWrapper cursor = queryProducts(columns, null, null, null, null,
                null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                products.add(cursor.getProduct());
                cursor.moveToNext();
//                database.yieldIfContendedSafely();
            }
            database.setTransactionSuccessful();
        } finally {
            cursor.close();
            database.endTransaction();
        }
        return products;
    }

    /**
     * The method <code>getAllFromCurrentShoppingList</code> gets all products in needed
     * shopping list from the database.
     *
     * @param shoppingListId is the shopping list id, which contains needed products.
     * @return all products in shopping list, which you need, or null.
     */
    // FIXME: 14.06.2017 Fix method getAllFromCurrentShoppingList
    public List<Product> getAllFromCurrentShoppingList(long shoppingListId) {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " tp, "
                + TABLE_SHOPPING_LISTS + " tl, "
                + TABLE_EXISTING_PRODUCTS + " tep "
                + "WHERE "
                + "tl." + COLUMN_ID + " = '" + shoppingListId + "'"
                + " AND "
                + "tp." + COLUMN_ID + " = " + "tep." + COLUMN_PRODUCT_ID
                + " AND "
                + "tl." + COLUMN_ID + " = " + "tep." + COLUMN_LIST_ID;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(0));
                product.setName(cursor.getString(1));
                product.setCost(cursor.getDouble(2));
                product.setPopularity(cursor.getLong(3));
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
            return products;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * The method <code>getTopFiveProducts</code> gets five products that are most popular from the
     * database without already existed products.
     *
     * @param existedProducts are products that exist in the shopping list.
     * @return five top products or not five).
     */
    // FIXME: 14.06.2017 Fix method getTopFiveProducts
    public List<Product> getTopFiveProducts(List<Product> existedProducts) {
        List<Product> products = new ArrayList<>();
        String[] selectionArgs = new String[existedProducts.size()];
        for (int i = 0; i < selectionArgs.length; i++) {
            selectionArgs[i] = "'" + existedProducts.get(i).getId() + "'";
        }
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_COST, COLUMN_POPULARITY};
        String selection = COLUMN_ID
                + " NOT IN (" + TextUtils.join(", ", selectionArgs) + ")";
        String orderBy = COLUMN_POPULARITY + " DESC";
        String limit = String.valueOf(5);
        database.beginTransaction();
        ShoppingListCursorWrapper cursor = queryProducts(columns, selection, null, null,
                null, orderBy, limit);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                products.add(cursor.getProduct());
                cursor.moveToNext();
//                database.yieldIfContendedSafely();
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            database.endTransaction();
        }
        return products;
    }

    /**
     * The method <code>addInCurrentShoppingList</code> adds product in the database and assign
     * it to shopping list.
     *
     * @param product        is the product, which you want to add to the database.
     * @param shoppingListId is the shopping list id.
     * @return added or updated product id.
     * @see #assignProductToShoppingList(long, long)
     * @see #isExistRelationship(long, long)
     */
    public long addInCurrentShoppingList(Product product, long shoppingListId) {
        long productId = 0;
        database.beginTransaction();
        try {
            Product existedProduct = getOneByName(product.getName());
            if (existedProduct == null) {
                Log.i(ProductDAO.class.getSimpleName(), "Product doesn't exist from the database.");
                ContentValues contentValues = getContentValuesProducts(product);
                productId = database.insert(TABLE_PRODUCTS, null, contentValues);
                assignProductToShoppingList(shoppingListId, productId);
            } else {
                Log.i(ProductDAO.class.getSimpleName(), "Product is exist from the database.");
                product.setId(existedProduct.getId());
                product.setPopularity(existedProduct.getPopularity() + 1);
                productId = product.getId();
                update(product);
                if (!isExistRelationship(shoppingListId, productId)) {
                    assignProductToShoppingList(shoppingListId, productId);
                }
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        return productId;
    }

    /**
     * The method <code>addInCurrentShoppingListAndCheck</code> adds product in the database and
     * assign it to shopping list.
     *
     * @param product        is the product, which you want to add to the database.
     * @param shoppingListId is the shopping list id.
     * @return true if product exist in current shopping list, false - does not exist.
     * @see #assignProductToShoppingList(long, long)
     * @see #isExistRelationship(long, long)
     */
    public boolean addInCurrentShoppingListAndCheck(Product product, long shoppingListId) {
        boolean existence = false;
        database.beginTransaction();
        try {
            Product existedProduct = getOneByName(product.getName());
            if (existedProduct == null) {
                Log.i(ProductDAO.class.getSimpleName(), "Product doesn't exist from the database.");
                ContentValues contentValues = getContentValuesProducts(product);
                long newListId = database.insert(TABLE_PRODUCTS, null, contentValues);
                assignProductToShoppingList(shoppingListId, newListId);
                existence = false;
                Log.i(ProductDAO.class.getSimpleName(),
                        "Product doesn't exist from the shopping list.");
            } else {
                Log.i(ProductDAO.class.getSimpleName(), "Product is exist from the database.");
                product.setId(existedProduct.getId());
                product.setPopularity(existedProduct.getPopularity() + 1);
                update(product);
                if (!isExistRelationship(shoppingListId, product.getId())) {
                    assignProductToShoppingList(shoppingListId, product.getId());
                    existence = false;
                    Log.i(ProductDAO.class.getSimpleName(),
                            "Product doesn't exist from the shopping list");
                } else {
                    existence = true;
                    Log.i(ProductDAO.class.getSimpleName(),
                            "Product is exist from the shopping list.");
                }
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        return existence;
    }

    /**
     * The method <code>update</code> update product in the database.
     *
     * @param product is the product, which you need to update.
     * @return the number of rows affected.
     */
    public long update(Product product) {
        ContentValues contentValues = getContentValuesProducts(product);
        return database.update(TABLE_PRODUCTS, contentValues, WHERE_ID_EQUALS,
                new String[]{String.valueOf(product.getId())});
    }

    /**
     * The method <code>deleteFromDatabase</code> delete product from the database.
     *
     * @param productId is the product id, which you want to delete from the database.
     */
    public void deleteFromDatabase(long productId) {
        database.beginTransaction();
        try {
            deleteOneFromAnywhere(productId);
            database.delete(TABLE_PRODUCTS, WHERE_ID_EQUALS,
                    new String[]{String.valueOf(productId)});
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * The method <code>delete</code> delete product only in shopping list.
     *
     * @param shoppingListId is the current shopping list id, which contains needed product.
     * @param productId      is the product id, which you want to delete form shopping list.
     */
    public void delete(final long shoppingListId, final long productId) {
        database.delete(TABLE_EXISTING_PRODUCTS,
                WHERE_LIST_ID_EQUALS + " AND " + WHERE_PRODUCT_ID_EQUALS,
                new String[]{String.valueOf(shoppingListId), String.valueOf(productId)});
    }

    /**
     * The method <code>assignProductToShoppingList</code> assign product to shopping list.
     *
     * @param shoppingListId is the shopping list id.
     * @param productId      is the product id, which you want to assign to shopping list.
     */
    private void assignProductToShoppingList(final long shoppingListId, final long productId) {
        ExistingProduct existingProduct = new ExistingProduct(shoppingListId, productId, 1);
        ContentValues contentValues = getContentValuesExistingProducts(existingProduct);
        database.insert(TABLE_EXISTING_PRODUCTS, null, contentValues);
    }

    /**
     * The method <code>isExistRelationship</code> checks the relationship exists or not.
     *
     * @param shoppingListId is the shopping list id.
     * @param productId      is the product id.
     * @return true if relationship is exist, false - doesn't exist.
     */
    private boolean isExistRelationship(final long shoppingListId, final long productId) {
        String[] columns = {COLUMN_ID, COLUMN_LIST_ID, COLUMN_PRODUCT_ID};
        String selection = WHERE_LIST_ID_EQUALS + " AND " + WHERE_PRODUCT_ID_EQUALS;
        String[] selectionArgs = {String.valueOf(shoppingListId), String.valueOf(productId)};
        String limit = String.valueOf(1);
        ShoppingListCursorWrapper cursor = queryExistingProducts(columns, selection, selectionArgs,
                null, null, null, limit);
        try {
            if (cursor.getCount() == 0) {
                return false;
            }
            cursor.moveToFirst();
            return true;
        } finally {
            cursor.close();
        }
    }

    /**
     * The method <code>deleteOneFromAnywhere</code> deleting product from the all shopping lists.
     *
     * @param productId is the product id, which you want to delete.
     */
    private void deleteOneFromAnywhere(final long productId) {
        database.delete(TABLE_EXISTING_PRODUCTS, WHERE_PRODUCT_ID_EQUALS,
                new String[]{String.valueOf(productId)});
    }
}