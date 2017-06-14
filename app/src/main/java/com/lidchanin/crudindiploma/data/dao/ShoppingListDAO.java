package com.lidchanin.crudindiploma.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.lidchanin.crudindiploma.data.ShoppingListCursorWrapper;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

import java.util.ArrayList;
import java.util.List;

import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_DATE_OF_CREATION;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_LIST_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_NAME;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_EXISTING_PRODUCTS;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_SHOPPING_LISTS;

/**
 * The class <code>ShoppingListDAO</code> extends {@link DatabaseDAO} and implements database
 * operations such as add, update, delete, get {@link ShoppingList}.
 *
 * @author Lidchanin
 */
public class ShoppingListDAO extends DatabaseDAO {

    private static final String ID_EQUALS = COLUMN_ID + " =?";
    private static final String LIST_ID_EQUALS = COLUMN_LIST_ID + " =?";

    public ShoppingListDAO(Context context) {
        super(context);
    }

    /**
     * The method <code>getContentValues</code> fills the ContentValues with the
     * {@link ShoppingList}.
     *
     * @param shoppingList is the shopping list.
     * @return filled ContentValues.
     */
    private static ContentValues getContentValues(ShoppingList shoppingList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, shoppingList.getName());
        return contentValues;
    }

    /**
     * The method <code>queryShoppingLists</code> wraps the {@link Cursor}.
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
    private ShoppingListCursorWrapper queryShoppingLists(String[] columns, String selection,
                                                         String[] selectionArgs, String groupBy,
                                                         String having, String orderBy,
                                                         String limit) {
        Cursor cursor = database.query(TABLE_SHOPPING_LISTS, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        return new ShoppingListCursorWrapper(cursor);
    }

    /**
     * The method <code>add</code> add shopping list to database.
     *
     * @param shoppingList is shopping list, which you want to add to the database.
     * @return added shopping list id in the database.
     */
    public long add(ShoppingList shoppingList) {
        ContentValues contentValues = getContentValues(shoppingList);
        return database.insert(TABLE_SHOPPING_LISTS, null, contentValues);
    }

    /**
     * The method <code>update</code> update shopping list in the database.
     *
     * @param shoppingList is the shopping list, which you need to update.
     * @return the number of rows affected.
     */
    public long update(ShoppingList shoppingList) {
        ContentValues contentValues = getContentValues(shoppingList);
        return database.update(TABLE_SHOPPING_LISTS, contentValues, ID_EQUALS,
                new String[]{String.valueOf(shoppingList.getId())});
    }

    /**
     * The method <code>deleteFromDatabase</code> deleteFromDatabase shopping list in the database.
     *
     * @param shoppingList is the shopping list, which you want to deleteFromDatabase.
     */
    public void delete(ShoppingList shoppingList) {
        database.beginTransaction();
        try {
            deleteRelationships(shoppingList.getId());
            database.delete(TABLE_SHOPPING_LISTS, ID_EQUALS,
                    new String[]{String.valueOf(shoppingList.getId())});
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * The method <code>getOne</code> get shopping list by id from the database.
     *
     * @param shoppingListId is the shopping list id, which you want to get.
     * @return needed shopping list or null.
     */
    public ShoppingList getOne(long shoppingListId) {
        String[] columns = {COLUMN_ID, COLUMN_NAME};
        String selection = ID_EQUALS;
        String[] selectionArgs = {String.valueOf(shoppingListId)};
        String limit = String.valueOf(1);
        ShoppingListCursorWrapper cursor = queryShoppingLists(columns, selection, selectionArgs,
                null, null, null, limit);
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getShoppingListWithoutDate();
        } finally {
            cursor.close();
        }
    }

    /**
     * The method <code>getAll</code> get all shopping lists in the database.
     *
     * @return all shopping lists, which you need, or empty shopping lists array.
     */
    public List<ShoppingList> getAll() {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_DATE_OF_CREATION};
        database.beginTransaction();
        ShoppingListCursorWrapper cursor = queryShoppingLists(columns, null, null, null, null,
                null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                shoppingLists.add(cursor.getShoppingListWithDate());
                cursor.moveToNext();
//                database.yieldIfContendedSafely();
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            cursor.close();
            database.endTransaction();
        }
        return shoppingLists;
    }

    /**
     * The method <code>deleteRelationships</code> need to deleteFromDatabase relationships, i.e.
     * delete in database all products from the current shopping list.
     *
     * @param shoppingListId is the current shopping list id.
     */
    private void deleteRelationships(long shoppingListId) {
        database.delete(TABLE_EXISTING_PRODUCTS, LIST_ID_EQUALS,
                new String[]{String.valueOf(shoppingListId)});
    }
}
