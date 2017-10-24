package com.lidchanin.crudindiploma.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ListsMainRVAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.dao.DaoMaster;
import com.lidchanin.crudindiploma.database.dao.DaoSession;
import com.lidchanin.crudindiploma.database.dao.ProductDao;
import com.lidchanin.crudindiploma.database.dao.ShoppingListDao;
import com.lidchanin.crudindiploma.database.dao.StatisticDao;
import com.lidchanin.crudindiploma.database.dao.UsedProductDao;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class ShoppingListFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "ShoppingListFragment";

    private ListsMainRVAdapter listsMainRVAdapter;

    private List<ShoppingList> shoppingLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        ImageButton addButton = ((NavigationDrawerActivity) getActivity()).addNewItem();

        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "db", null);
        final Database database = helper.getWritableDb();
        final DaoMaster daoMaster = new DaoMaster(database);
        final DaoSession daoSession = daoMaster.newSession();
        final ShoppingListDao shoppingListDao = daoSession.getShoppingListDao();
        final ProductDao productDao = daoSession.getProductDao();
        final UsedProductDao usedProductDao = daoSession.getUsedProductDao();
        final StatisticDao statisticDao = daoSession.getStatisticDao();

        shoppingLists = shoppingListDao.loadAll();

        RecyclerView mainRV = (RecyclerView) view.findViewById(R.id.main_screen_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainRV.setLayoutManager(layoutManager);
        listsMainRVAdapter = new ListsMainRVAdapter(getContext(), shoppingListDao,
                productDao, usedProductDao, statisticDao, shoppingLists);
        mainRV.setAdapter(listsMainRVAdapter);

        addButton.setVisibility(View.VISIBLE);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForAdd(shoppingListDao);
            }
        });
        return view;
    }

    /**
     * The method <b>createAndShowAlertDialogForAdd</b> creates and shows a dialog, which
     * need to create new {@link ShoppingList}.
     */
    private void createAndShowAlertDialogForAdd(final ShoppingListDao shoppingListDao) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.MyDialogTheme);
        builder.setTitle(R.string.add_a_new_shopping_list);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(getContext());
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(getString(R.string.enter_name));

        final TextInputLayout textInputLayout = new TextInputLayout(getContext());
        textInputLayout.addView(editTextName);

        builder.setView(textInputLayout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextName.getText() != null
                        && editTextName.getText().toString().length() != 0) {
                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setName(editTextName.getText().toString());
                    shoppingList.setDate(System.currentTimeMillis());
                    shoppingListDao.insert(shoppingList);
                    shoppingLists.add(shoppingList);
                    listsMainRVAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), R.string.please_enter_name,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForAdd(shoppingListDao);
                }
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
