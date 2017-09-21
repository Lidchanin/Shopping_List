package com.lidchanin.crudindiploma.models;

/**
 * The lass <code>ExistingProduct</code> is an entity. Existing product contains references on
 * unique product and on shopping list, where it's located, and product quantity or product weight.
 *
 * @author Lidchanin
 */
public class ExistingProduct {

    private long id;
    private long shoppingListId;
    private long productId;
    private double quantityOrWeight = 1;
    private boolean isPurchased = false;

    public ExistingProduct() {}

    public ExistingProduct(double quantityOrWeight) {
        this.quantityOrWeight = quantityOrWeight;
    }

    public ExistingProduct(long shoppingListId, long productId, double quantityOrWeight) {
        this.shoppingListId = shoppingListId;
        this.productId = productId;
        this.quantityOrWeight = quantityOrWeight;
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

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }
}
