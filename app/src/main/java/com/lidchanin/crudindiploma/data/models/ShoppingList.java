package com.lidchanin.crudindiploma.data.models;

import java.util.Calendar;

/**
 * The class {@link ShoppingList} is an entity. Shopping list contains id, name.
 *
 * @author Lidchanin
 */
public class ShoppingList {

    // FIXME: 20.06.2017 check date
    Calendar calendar = Calendar.getInstance();
    int seconds = calendar.get(Calendar.SECOND);
    private long id;
    private String name;
    private String dateOfCreation = String.valueOf(seconds);

    /**
     * Constructor for create an empty shopping list.
     */
    public ShoppingList() {
    }

    /**
     * Constructor for create a shopping list.
     *
     * @param name is the shopping list name.
     */
    public ShoppingList(String name) {
        this.name = name;
    }

    /**
     * Constructor for create a shopping list.
     *
     * @param id   is the shopping list id.
     * @param name is the shopping list name.
     */
    public ShoppingList(long id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
