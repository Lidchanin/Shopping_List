package com.lidchanin.crudindiploma.data.models;

/**
 * The class <code>Product</code> is an entity. Product contains id, name, cost and
 * popularity.
 *
 * @author Lidchanin
 */
public class Product {

    private long id;
    private String name;
    private double cost;
    // Product popularity increases every time, where product is used.
    private long popularity = 0;

    /**
     * Constructor for create an empty product.
     */
    public Product() {
    }

    /**
     * Constructor for create a product with name and cost.
     *
     * @param name is the product name.
     * @param cost is the product cost.
     */
    public Product(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    /**
     * Constructor for create a product with name? cost and popularity.
     *
     * @param name       is the product name.
     * @param cost       is the product cost.
     * @param popularity is the product popularity.
     */
    public Product(String name, double cost, long popularity) {
        this.name = name;
        this.cost = cost;
        this.popularity = popularity;
    }

    /**
     * Constructor for create a product with id, name and cost.
     *
     * @param id   is the product id.
     * @param name is the product name.
     * @param cost is the product cost.
     */
    public Product(long id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    /**
     * Constructor for create a product with id, name, cost and popularity.
     *
     * @param id         is the product id.
     * @param name       is the product name.
     * @param cost       is the product cost.
     * @param popularity is the product popularity.
     */
    public Product(long id, String name, double cost, long popularity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return name;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

}
