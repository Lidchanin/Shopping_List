package com.lidchanin.shoppinglist.models;

import com.lidchanin.shoppinglist.R;

/**
 * Created by Alexander Destroyed on 14.10.2017.
 */

public class ThemeViewModels {

    private String cost;
    private String name;
    private String theme;
    private int previewId = R.drawable.virgintest;

    public ThemeViewModels(String cost, String name, String theme, int previewId) {
        this.cost = cost;
        this.name = name;
        this.theme = theme;
        this.previewId = previewId;
    }

    public ThemeViewModels(String cost, String name, String theme) {
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getPreviewId() {
        return previewId;
    }

    public void setPreviewId(int previewId) {
        this.previewId = previewId;
    }

}
