package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.spongyt.wimo.repository");

        Entity order = schema.addEntity("Order");
        order.addIdProperty();
        order.addStringProperty("orderNumber").notNull();
        order.addStringProperty("name");
        order.addStringProperty("shipper");
        order.addStringProperty("deliveryState");
        order.addStringProperty("deliveryStateText");
        order.addBooleanProperty("isSentByUser");
        order.addBooleanProperty("isSynced");
        order.addLongProperty("createdTimeStamp");
        order.addContentProvider();

        new DaoGenerator().generateAll(schema, "app\\src\\main\\java");
    }
}
