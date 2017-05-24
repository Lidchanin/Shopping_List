package com.lidchanin.crudindiploma.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lidchanin.crudindiploma.data.DatabaseHelper;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

import java.util.ArrayList;
import java.util.List;

import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_EXISTING_PRODUCTS;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_SHOPPING_LISTS;

/**
 * Class <code>ShoppingListDAO</code> extends {@link DatabaseDAO} and implements database operations
 * such as add, update, delete, get, ...
 * {@link com.lidchanin.crudindiploma.data.models.ShoppingList}.
 *
 * @author Lidchanin
 */
public class ShoppingListDAO extends DatabaseDAO {

    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID + " =?";
    private static final String WHERE_LIST_ID_EQUALS = DatabaseHelper.COLUMN_LIST_ID + " =?";

    public ShoppingListDAO(Context context) {
        super(context);
    }

    /**
     * Method <code>add</code> add shopping list to database.
     *
     * @param shoppingList is shopping list, which you want to add to the database.
     * @return added shopping list id in the database.
     */
    public long add(ShoppingList shoppingList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, shoppingList.getName());
        return database.insert(DatabaseHelper.TABLE_SHOPPING_LISTS, null, contentValues);
    }

    /**
     * Method <code>update</code> update shopping list in the database.
     *
     * @param shoppingList is the shopping list, which you need to update.
     * @return the number of rows affected.
     */
    public long update(ShoppingList shoppingList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, shoppingList.getName());
        return database.update(TABLE_SHOPPING_LISTS, contentValues, WHERE_ID_EQUALS,
                new String[]{String.valueOf(shoppingList.getId())});
    }

    /**
     * Method <code>deleteInDatabase</code> deleteInDatabase shopping list in the database.
     *
     * @param shoppingList is the shopping list, which you want to deleteInDatabase.
     */
    public void delete(ShoppingList shoppingList) {
        deleteRelationships(shoppingList.getId());
        database.delete(DatabaseHelper.TABLE_SHOPPING_LISTS, WHERE_ID_EQUALS,
                new String[]{String.valueOf(shoppingList.getId())});
    }
    /**
     * Method <code>getOne</code> get shopping list by id from the database.
     *
     * @param shoppingListId is the shopping list id, which you want to get.
     * @return needed shopping list.
     */
    public ShoppingList getOne(long shoppingListId) {
        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME};
        String[] selectionArgs = {String.valueOf(shoppingListId)};
        String limit = String.valueOf(1);
        Cursor cursor = database.query(DatabaseHelper.TABLE_SHOPPING_LISTS, columns,
                WHERE_ID_EQUALS, selectionArgs, null, null, null, limit);
        if (cursor.moveToFirst()) {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setId(cursor.getLong(0));
            shoppingList.setName(cursor.getString(1));
            cursor.close();
            return shoppingList;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * Method <code>getAll</code> get all shopping lists in the database.
     *
     * @return all shopping lists, which you need, or empty shopping lists array.
     */
    public List<ShoppingList> getAll() {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_DATE_OF_CREATION};
        Cursor cursor = database.query(DatabaseHelper.TABLE_SHOPPING_LISTS, columns, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ShoppingList shoppingList = new ShoppingList();
                shoppingList.setId(cursor.getLong(0));
                shoppingList.setName(cursor.getString(1));
                shoppingList.setDateOfCreation(cursor.getString(2));
                shoppingLists.add(shoppingList);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return shoppingLists;
    }

    /**
     * Method <code>deleteRelationships</code> need to deleteInDatabase relationships, i.e.
     * delete in database all products from the current shopping list.
     *
     * @param shoppingListId is the current shopping list id.
     */
    private void deleteRelationships(long shoppingListId) {
        database.delete(TABLE_EXISTING_PRODUCTS, WHERE_LIST_ID_EQUALS,
                new String[]{String.valueOf(shoppingListId)});
    }

}
