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
import com.lidchanin.crudindiploma.adapters.AllProductRVAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.dao.DaoMaster;
import com.lidchanin.crudindiploma.database.dao.DaoSession;
import com.lidchanin.crudindiploma.database.dao.ProductDao;
import com.lidchanin.crudindiploma.database.dao.UsedProductDao;

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
        final UsedProductDao usedProductDao = daoSession.getUsedProductDao();

        products = productDao.queryBuilder().orderAsc(ProductDao.Properties.Name).list();

        initializeRecyclerView(view);
        initializeAdapter(productDao, usedProductDao);
        return view;
    }

    private void initializeRecyclerView(View view) {
        recyclerViewAllProducts = (RecyclerView)
                view.findViewById(R.id.managing_existing_products_recycler_view_all_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAllProducts.setLayoutManager(layoutManager);
    }

    private void initializeAdapter(ProductDao productDao,
                                   UsedProductDao usedProductDao) {
        AllProductRVAdapter adapter
                = new AllProductRVAdapter(products,
                productDao, usedProductDao, getContext());
        recyclerViewAllProducts.setAdapter(adapter);
    }

}
