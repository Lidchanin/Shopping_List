package com.lidchanin.shoppinglist.models;

/**
 * Created by Alexander Destroyed on 07.09.2017.
 */

public class ProfitItems {

    double weiht = 0;

    double cost = 0;

    double sum = 0;

    boolean isCustomized = false;

    public double getWeiht() {
        return weiht;
    }

    public void setWeiht(double weiht) {
        this.weiht = weiht;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public boolean isCustomized() {
        return isCustomized;
    }

    public void setCustomized(boolean customized) {
        isCustomized = customized;
    }

}
