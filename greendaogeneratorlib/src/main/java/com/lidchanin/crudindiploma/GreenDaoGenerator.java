package com.lidchanin.crudindiploma;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

public class GreenDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(2, "com.lidchanin.crudindiploma.database");

        Entity shoppingList = schema.addEntity("ShoppingList");
        shoppingList.addIdProperty();
        shoppingList.addStringProperty("name").notNull();
        shoppingList.addStringProperty("date").notNull();

        Entity exProduct = schema.addEntity("ExistingProduct");
        exProduct.addIdProperty();
        exProduct.addDoubleProperty("quantity");
        exProduct.addBooleanProperty("isPurchased");

        Entity product = schema.addEntity("Product");
        product.addIdProperty();
        product.addStringProperty("name").notNull();
        product.addDoubleProperty("cost").notNull();
        product.addLongProperty("popularity");

        //Creating One-To-One relation, existing product has "one" product
        Property productIdProperty = exProduct.addLongProperty("productId").getProperty();
        exProduct.addToOne(product, productIdProperty);

        //Creating One-To-Many relation, shopping list has "many" existing products
        Property shoppingListIdProperty = exProduct.addLongProperty("shoppingListId")
                .notNull().getProperty();
        ToMany shoppingListToExistingProducts = shoppingList.addToMany(exProduct,
                shoppingListIdProperty);
        shoppingListToExistingProducts.setName("existingProducts");

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }
}
