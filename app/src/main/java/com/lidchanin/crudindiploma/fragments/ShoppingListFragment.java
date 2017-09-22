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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.MainRVAdapter;
import com.lidchanin.crudindiploma.customview.NavigationDrawerActivity;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShoppingListFragment extends android.support.v4.app.Fragment {

    private ShoppingListDAO shoppingListDAO;
    private ProductDAO productDAO;
    private ExistingProductDAO existingProductDAO;
    private List<ShoppingList> shoppingLists;
    private RecyclerView mainRV;
    private Button buttonAdd;
    private MainRVAdapter mainRVAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoppinglist,container,false);
        ImageButton imageButton = ((NavigationDrawerActivity) getActivity()).addNewItem();
        shoppingListDAO = new ShoppingListDAO(getActivity());
        shoppingLists = shoppingListDAO.getAll();
        productDAO = new ProductDAO(getActivity());
        existingProductDAO = new ExistingProductDAO(getActivity());

        mainRV = (RecyclerView) view.findViewById(R.id.main_screen_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainRV.setLayoutManager(layoutManager);
        mainRVAdapter = new MainRVAdapter(getActivity(), shoppingLists, shoppingListDAO,
                productDAO, existingProductDAO);
        mainRV.setAdapter(mainRVAdapter);

        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForAdd();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shoppingListDAO.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        shoppingListDAO.close();
    }

    /**
     * The method <b>createAndShowAlertDialogForAdd</b> creates and shows a dialog, which
     * need to create new {@link ShoppingList}.
     */
    private void createAndShowAlertDialogForAdd() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_a_new_shopping_list);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(getContext());
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(getString(R.string.enter_name));

        final TextInputLayout textInputLayout = new TextInputLayout(getActivity());
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

                    mainRVAdapter.notifyAdding(temp);

                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), R.string.please_enter_name,
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
}
