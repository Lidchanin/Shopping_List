package com.lidchanin.crudindiploma.data.models;

/**
 * Class <code>ExistingProduct</code> is an entity. Existing product contains references on unique
 * product and on shopping list, where it's located, and product quantity or product weight, and
 * total cost.
 *
 * @author Lidchanin
 */
public class ExistingProduct {

    private long id;
    private long shoppingListId;
    private long productId;
    private double quantityOrWeight;
    private double totalCost;

    public ExistingProduct() {}

    public ExistingProduct(double quantityOrWeight) {
        this.quantityOrWeight = quantityOrWeight;
    }

    public ExistingProduct(double quantityOrWeight, double totalCost) {
        this.quantityOrWeight = quantityOrWeight;
        this.totalCost = totalCost;
    }

    //getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getQuantityOrWeight() {
        return quantityOrWeight;
    }

    public void setQuantityOrWeight(double quantityOrWeight) {
        this.quantityOrWeight = quantityOrWeight;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
