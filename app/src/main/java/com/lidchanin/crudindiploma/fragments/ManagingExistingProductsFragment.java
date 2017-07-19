package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ManagingExistingProductsRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Destroyed on 16.07.2017.
 */

public class ManagingExistingProductsFragment extends Fragment{

    private RecyclerView recyclerViewAllProducts;
    private ProductDAO productDAO;
    private List<Product> products;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_managing_existing_products,container,false);
        initializeData();
        initializeRecyclerView(view);
        initializeAdapter();
        return view;
    }

    private void initializeData() {
        productDAO = new ProductDAO(getActivity());
        products = productDAO.getAll();
        if (products == null) {
            products = new ArrayList<>();
        }
    }

    private void initializeRecyclerView(View view) {
        recyclerViewAllProducts = (RecyclerView)
                view.findViewById(R.id.managing_existing_products_recycler_view_all_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAllProducts.setLayoutManager(layoutManager);
    }

    private void initializeAdapter() {
        ManagingExistingProductsRecyclerViewAdapter adapter
                = new ManagingExistingProductsRecyclerViewAdapter(products, productDAO, getActivity());
        recyclerViewAllProducts.setAdapter(adapter);
    }

}