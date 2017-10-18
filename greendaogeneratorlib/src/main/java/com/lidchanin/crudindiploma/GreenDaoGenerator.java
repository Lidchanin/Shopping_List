package com.lidchanin.crudindiploma;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

public class GreenDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(5, "com.lidchanin.crudindiploma.database");
        schema.setDefaultJavaPackageDao("com.lidchanin.crudindiploma.database.dao");
        schema.enableKeepSectionsByDefault();

        Entity shoppingList = schema.addEntity("ShoppingList");
        shoppingList.addIdProperty()
                .codeBeforeField("/* id */");
        shoppingList.addStringProperty("name").notNull()
                .codeBeforeField("/* shopping list name */");
        shoppingList.addLongProperty("date").notNull()
                .codeBeforeField("/* date of creation */");

        Entity product = schema.addEntity("Product");
        product.addIdProperty()
                .codeBeforeField("/* id */");
        product.addStringProperty("name").notNull().unique()
                .codeBeforeField("/* product name */");
        product.addDoubleProperty("cost").notNull()
                .codeBeforeField("/* product cost */");

        Entity usedProduct = schema.addEntity("UsedProduct");
        usedProduct.addIdProperty()
                .codeBeforeField("/* id */");
        usedProduct.addDoubleProperty("quantity").notNull()
                .codeBeforeField("/* used product quantity */");
        usedProduct.addBooleanProperty("unit").notNull()
                .codeBeforeField("/* true -> kg, false - pieces */");
        usedProduct.addBooleanProperty("isPurchased").notNull()
                .codeBeforeField("/* true - purchased, false - not */");

        //Creating One-To-One relation, existing product has "one" product
        Property productIdProperty = usedProduct.addLongProperty("productId").getProperty();
        usedProduct.addToOne(product, productIdProperty);

        //Creating One-To-Many relation, shopping list has "many" existing products
        Property shoppingListIdProperty = usedProduct.addLongProperty("shoppingListId")
                .notNull().getProperty();
        ToMany shoppingListToExistingProducts = shoppingList.addToMany(usedProduct,
                shoppingListIdProperty);
        shoppingListToExistingProducts.setName("usedProducts");

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }
}
