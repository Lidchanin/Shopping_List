package com.lidchanin.crudindiploma.forlib;

import android.os.Parcelable;

import com.lidchanin.crudindiploma.R;

/**
 * Created by Alexander Destroyed on 14.10.2017.
 */

public class RecyclerViewItems {

    public RecyclerViewItems(String cost, String name, String theme, int previewId){
        this.cost = cost;
        this.name = name;
        this.theme = theme;
        this.previewId = previewId;
    }

    public RecyclerViewItems(String cost, String name, String theme){
        this.cost = cost;
        this.name = name;
        this.theme = theme;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    private String theme;

    public int getPreviewId() {
        return previewId;
    }

    public void setPreviewId(int previewId) {
        this.previewId = previewId;
    }

    private int previewId = R.drawable.virgintest ;

}
