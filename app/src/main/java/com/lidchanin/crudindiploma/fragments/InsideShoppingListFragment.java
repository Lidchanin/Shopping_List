package com.lidchanin.crudindiploma.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.activities.CameraActivity;
import com.lidchanin.crudindiploma.adapters.AutoCompleteProductNamesAndCostsAdapter;
import com.lidchanin.crudindiploma.adapters.InsideShoppingListRecyclerViewAdapter;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

/**
 * Created by Alexander Destroyed on 10.07.2017.
 */

public class InsideShoppingListFragment extends Fragment {

    private RecyclerView recyclerViewAllProducts;
    private InsideShoppingListRecyclerViewAdapter recyclerViewAdapter;
    private TextView textViewEstimatedAmount;

    private List<Product> products;
    private List<ExistingProduct> existingProducts;
    private ShoppingListDAO shoppingListDAO;
    private ExistingProductDAO existingProductDAO;
    private ProductDAO productDAO;
    private Button scan;
    private Button type;
    private View view;
    private long shoppingListId;

    public static InsideShoppingListFragment getInstance(){
        return new InsideShoppingListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListId =getArguments().getLong(Constants.Bundles.SHOPPING_LIST_ID);
        shoppingListDAO = new ShoppingListDAO(getActivity());
        productDAO = new ProductDAO(getActivity());
        existingProductDAO = new ExistingProductDAO(getActivity());
        initializeData(shoppingListId);
        shoppingListDAO.open();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inside_shopping_list,container,false);
        initializeViewsAndButtons(shoppingListId);
        return view;
    }

    private void initializeViewsAndButtons(final long shoppingListId) {
        type = (Button) view.findViewById(R.id.enter_by_type);
        scan = (Button) view.findViewById(R.id.scan);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForManualType();
            }
        });

        scan = (Button) view.findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),
                        CameraActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                startActivity(intent);
            }
        });

        initializeTextViewWithShoppingListName();
    }

    private void initializeData(long shoppingListId) {
        shoppingListDAO.open();
        productDAO.open();
        existingProductDAO.open();
        products = productDAO.getAllFromCurrentShoppingList(shoppingListId);
        if (products == null) {
            products = new ArrayList<>();
        }
        existingProducts = existingProductDAO.getAllFromCurrentShoppingList(shoppingListId);
        if (existingProducts == null) {
            existingProducts = new ArrayList<>();
        } else {
            calculationOfEstimatedAmount(products, existingProducts);
        }
    }

    private void initializeTextViewWithShoppingListName() {
        shoppingListDAO.open();
        String shoppingListName = shoppingListDAO.getOne(shoppingListId).getName();
        getActivity().setTitle(shoppingListName);
    }

    private double calculationOfEstimatedAmount(List<Product> products,
                                                List<ExistingProduct> existingProducts) {
        double estimatedAmount = 0;
        for (int i = 0; i < existingProducts.size(); i++) {
            double productCost = products.get(i).getCost();
            double existingProductQuantity = existingProducts.get(i).getQuantityOrWeight();
            estimatedAmount += productCost * existingProductQuantity;
        }
        return estimatedAmount;
    }

    private void createAndShowAlertDialogForManualType() {
        productDAO.open();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        builder.setTitle(R.string.add_new_product);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextCost = new EditText(getActivity());
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(getString(R.string.enter_cost));
        editTextCost.setHintTextColor(Color.BLACK);

        final AutoCompleteTextView autoCompleteTextViewName = new AutoCompleteTextView(getActivity());
        autoCompleteTextViewName.setInputType(InputType.TYPE_CLASS_TEXT);
        autoCompleteTextViewName.setHint(getString(R.string.enter_name));
        autoCompleteTextViewName.setHintTextColor(Color.BLACK);
        autoCompleteTextViewName.setTextColor(Color.BLACK);

        List<Product> allProducts = productDAO.getAll();
        AutoCompleteProductNamesAndCostsAdapter autoCompleteAdapter
                = new AutoCompleteProductNamesAndCostsAdapter(getActivity(), allProducts);
        autoCompleteTextViewName.setAdapter(autoCompleteAdapter);
        autoCompleteTextViewName.setTextColor(Color.BLACK);
        autoCompleteTextViewName.onTextContextMenuItem(getResources().getColor(R.color.cardview_dark_background));
        autoCompleteTextViewName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selected = (Product) parent.getAdapter().getItem(position);
                editTextCost.setText(String.valueOf(selected.getCost()));
            }
        });


        final EditText editTextQuantity = new EditText(getActivity());
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(getString(R.string.enter_quantity));
        editTextQuantity.setHintTextColor(Color.BLACK);

        layout.addView(autoCompleteTextViewName);
        layout.addView(editTextCost);
        layout.addView(editTextQuantity);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productDAO.open();
                autoCompleteTextViewName.setTextColor(getResources().getColor(R.color.cardview_dark_background));
                if (autoCompleteTextViewName.getText() != null
                        && autoCompleteTextViewName.getText().toString().length() != 0) {
                    Product newProduct = new Product();
                    newProduct.setName(autoCompleteTextViewName.getText().toString());
                    if(editTextCost.getText() != null
                            && editTextCost.getText().toString().length() != 0
                            && editTextQuantity.getText() != null
                            && editTextQuantity.getText().toString().length() != 0){
                        newProduct.setCost(Double.valueOf(editTextCost.getText().toString()));
                        ExistingProduct newExistingProduct = new ExistingProduct(Double
                                .parseDouble(editTextQuantity.getText().toString()));
                        boolean existence = productDAO
                                .addInCurrentShoppingListAndCheck(newProduct, shoppingListId);
                        notifyListsChanges(existence, newProduct, newExistingProduct);
                        setTextForTextViewCostsSum(textViewEstimatedAmount);
                        dialog.dismiss();
                    }
                    else{
                        newProduct.setCost(Double.valueOf("0.0"));
                        ExistingProduct newExistingProduct = new ExistingProduct(Double
                                .parseDouble("1"));
                        productDAO.open();

                        boolean existence = productDAO
                                .addInCurrentShoppingListAndCheck(newProduct, shoppingListId);
                        notifyListsChanges(existence, newProduct, newExistingProduct);
                        setTextForTextViewCostsSum(textViewEstimatedAmount);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.please_enter_all_data,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForManualType();
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

    private void notifyListsChanges(boolean existence, Product newProduct,
                                    ExistingProduct newExistingProduct) {
        existingProductDAO.open();
        productDAO.open();
        long tempProductId = productDAO.getOneByName(newProduct.getName()).getId();
        ExistingProduct tempExistingProduct = existingProductDAO
                .getOne(shoppingListId, tempProductId);
        tempExistingProduct.setQuantityOrWeight(newExistingProduct.getQuantityOrWeight());
        existingProductDAO.update(tempExistingProduct);

        if (!existence) {
            products.add(products.size(), newProduct);
            Log.d(TAG, "notifyListsChanges: "+products.size() + newProduct.getName());
            existingProducts.add(tempExistingProduct);
            recyclerViewAdapter.notifyItemInserted(products.size());
        } else {
            for (Product p : products) {
                if (p.getName() != null && p.getName().contains(newProduct.getName())) {
                    int position = products.indexOf(p);
                    products.set(position, newProduct);
                    existingProducts.set(position, newExistingProduct);
                    recyclerViewAdapter.notifyItemChanged(position);
                    break;
                }
            }
        }
    }

    private void setTextForTextViewCostsSum(TextView textViewCostsSum) {
        textViewCostsSum.setText(getString(R.string.estimated_amount,
                new DecimalFormat("#.##").format(
                        calculationOfEstimatedAmount(products, existingProducts))));
    }

}
