package com.lidchanin.crudindiploma.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.MainScreenRecyclerViewAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.models.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShoppingListFragment extends android.support.v4.app.Fragment {

    private ExistingProductDAO existingProductDAO;
    private ShoppingListDAO shoppingListDAO;
    private List<ShoppingList> shoppingLists;
    private RecyclerView recyclerViewAllShoppingLists;
    private MainScreenRecyclerViewAdapter mainScreenRecyclerViewAdapter;
    private Button buttonAdd;

    public static ShoppingListFragment getInstance() {
        return new ShoppingListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NavigationDrawerActivity)getActivity()).setButtonsToDefault();
        shoppingListDAO = new ShoppingListDAO(getActivity());
        existingProductDAO = new ExistingProductDAO(getActivity());

        initializeData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        recyclerViewAllShoppingLists = (RecyclerView)
                fragmentView.findViewById(R.id.main_screen_recycler_view_all_shopping_lists);
        buttonAdd = (Button)
                fragmentView.findViewById(R.id.main_screen_button_add_shopping_list);
        initializeButtonAdd();
        initializeRecyclerViews();
        initializeAdapters();
        shoppingListDAO.open();
        existingProductDAO.open();
        ((NavigationDrawerActivity) getActivity()).setShoppingListSorts(shoppingLists,mainScreenRecyclerViewAdapter);
        return fragmentView;
    }

    /**
     * Method <code>initializeRecyclerView</code> initializes {@link RecyclerView}.
     */

    public void initializeRecyclerViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAllShoppingLists.setLayoutManager(layoutManager);
    }

    /**
     * Method <code>initializeAdapters</code> initializes adapter for {@link RecyclerView}.
     */

    private void initializeAdapters() {
        mainScreenRecyclerViewAdapter
                = new MainScreenRecyclerViewAdapter(shoppingLists, shoppingListDAO,
                existingProductDAO, getActivity());
        recyclerViewAllShoppingLists.setAdapter(mainScreenRecyclerViewAdapter);
    }

    /**
     * Method <code>initializeButtonAdd</code> initializes {@link Button} for adding shopping list.
     */

    private void initializeButtonAdd() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForAdd();
            }
        });
    }


    /**
     * The method <code>createAndShowAlertDialogForAdd</code> create and shows a dialog, which
     * need to add new shopping list.
     */

    private void createAndShowAlertDialogForAdd() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.MyDialogTheme);
        builder.setTitle(R.string.add_a_new_shopping_list);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(getActivity());
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
                    ShoppingList temp = new ShoppingList();
                    temp.setName(editTextName.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            getString(R.string.database_date_format), Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());
                    temp.setDateOfCreation(currentDateAndTime);
                    long shoppingListId = shoppingListDAO.add(temp);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    InsideShoppingListFragment fragment = new InsideShoppingListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.Bundles.SHOPPING_LIST_ID, shoppingListId);
                    fragment.setArguments(bundle);
                    //// TODO: 14.07.2017 add to add to another container!!! , and replace to replace :D your friend Cap ;x
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    dialog.dismiss();
                } else {
                    // FIXME: 09.06.2017 alert dialog for add
                    Toast.makeText(getActivity(), R.string.please_enter_name,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForAdd();
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

    @Override
    public void onResume() {
        super.onResume();
        shoppingListDAO.open();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shoppingListDAO.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        shoppingListDAO.close();
    }

    /**
     * Method <code>initializeData</code> reads and receives all shopping lists from the database.
     */
    private void initializeData() {
        shoppingLists = shoppingListDAO.getAll();
        if (shoppingLists == null) {
            shoppingLists = new ArrayList<>();
        }
    }
}
