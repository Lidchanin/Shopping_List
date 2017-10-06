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
import com.lidchanin.crudindiploma.adapters.ManagingProductsRVAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.database.DaoMaster;
import com.lidchanin.crudindiploma.database.DaoSession;
import com.lidchanin.crudindiploma.database.ExistingProductDao;
import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ProductDao;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class ManagingExistingProductsFragment extends Fragment {

    private RecyclerView recyclerViewAllProducts;

    private List<Product> products;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_managing_existing_products, container,
                false);

        ((NavigationDrawerActivity) getActivity()).setButtonsToDefault();

        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "db", null);
        final Database database = helper.getWritableDb();
        final DaoMaster daoMaster = new DaoMaster(database);
        final DaoSession daoSession = daoMaster.newSession();
        final ProductDao productDao = daoSession.getProductDao();
        final ExistingProductDao existingProductDao = daoSession.getExistingProductDao();

        products = productDao.loadAll();

        initializeRecyclerView(view);
        initializeAdapter(productDao, existingProductDao);
        return view;
    }

    private void initializeRecyclerView(View view) {
        recyclerViewAllProducts = (RecyclerView)
                view.findViewById(R.id.managing_existing_products_recycler_view_all_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAllProducts.setLayoutManager(layoutManager);
    }

    private void initializeAdapter(ProductDao productDao,
                                   ExistingProductDao existingProductDao) {
        ManagingProductsRVAdapter adapter
                = new ManagingProductsRVAdapter(products,
                productDao, existingProductDao, getContext());
        recyclerViewAllProducts.setAdapter(adapter);
    }

}
