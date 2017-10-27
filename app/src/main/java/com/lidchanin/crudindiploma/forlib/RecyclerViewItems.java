package com.lidchanin.crudindiploma.forlib;

import android.os.Parcelable;

import com.lidchanin.crudindiploma.R;

/**
 * Created by Alexander Destroyed on 14.10.2017.
 */

public class RecyclerViewItems {

    public RecyclerViewItems(String cost, String name){
        this.cost=cost;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    private String cost = "Free";
    private String name = "Theme Shit";

    public int getPreviewId() {
        return previewId;
    }

    public void setPreviewId(int previewId) {
        this.previewId = previewId;
    }

    private int previewId = R.drawable.virgintest ;

}
