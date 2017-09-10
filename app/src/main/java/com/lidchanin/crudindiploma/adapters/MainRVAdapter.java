package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class {@link MainRVAdapter} provide a binding from an app-specific data set to views that are
 * displayed within a {@link RecyclerView}.
 * Class extends {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.MainViewHolder> {

    private Context context;
    private ShoppingListDAO shoppingListDAO;
    private ProductDAO productDAO;
    private ExistingProductDAO existingProductDAO;

    private List<ShoppingList> shoppingLists;
    private List<Product> products;
    private List<ExistingProduct> existingProducts;

    private ChildRVAdapter childRVAdapter;

    /**
     * Constructor.
     *
     * @param context            context
     * @param shoppingLists      List with all shopping lists
     * @param shoppingListDAO    existing exemplar {@link ShoppingListDAO}
     * @param productDAO         existing exemplar {@link ProductDAO}
     * @param existingProductDAO existing exemplar {@link ExistingProductDAO}
     */
    public MainRVAdapter(Context context, List<ShoppingList> shoppingLists,
                         ShoppingListDAO shoppingListDAO,
                         ProductDAO productDAO,
                         ExistingProductDAO existingProductDAO) {
        this.context = context;
        this.shoppingLists = shoppingLists;
        this.shoppingListDAO = shoppingListDAO;
        this.productDAO = productDAO;
        this.existingProductDAO = existingProductDAO;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_main_rv, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final long shoppingListId = shoppingLists.get(adapterPosition).getId();

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                holder.rvChild.setLayoutManager(layoutManager);
                products = productDAO.getAllFromCurrentShoppingList(shoppingListId);
                existingProducts = existingProductDAO.getAllFromCurrentShoppingList(shoppingListId);
                childRVAdapter = new ChildRVAdapter(context, productDAO, existingProductDAO,
                        shoppingListId, products, existingProducts);
                childRVAdapter.setOnDataChangeListener(new ChildRVAdapter
                        .OnDataChangeListener() {
                    @Override
                    public void onDataChanged(List<ExistingProduct> existingProducts) {
                        holder.tvEstimatedSum
                                .setText(context
                                        .getString(R.string.estimated_amount,
                                                new DecimalFormat("#.##")
                                                        .format(calculationOfEstimatedAmount(
                                                                products, existingProducts))));
                    }
                });
                holder.rvChild.setAdapter(childRVAdapter);

                holder.tvEstimatedSum.setText(context.getString(R.string.estimated_amount,
                        new DecimalFormat("#.##").format(
                                calculationOfEstimatedAmount(products, existingProducts))));

                if (holder.rvChild.getVisibility() == View.GONE) {
                    holder.rvChild.setVisibility(View.VISIBLE);
                    holder.buttonChildAdd.setVisibility(View.VISIBLE);
                    holder.tvEstimatedSum.setVisibility(View.VISIBLE);
                } else {
                    holder.rvChild.setVisibility(View.GONE);
                    holder.buttonChildAdd.setVisibility(View.GONE);
                    holder.tvEstimatedSum.setVisibility(View.GONE);
                }

                holder.buttonChildAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createAndShowAlertDialogForManualType(shoppingListId);
                    }
                });
            }
        });

        holder.tvName.setText(shoppingLists.get(adapterPosition).getName());
        holder.tvDate.setText(dateConverter(shoppingLists.get(adapterPosition).getDateOfCreation()));

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(adapterPosition);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(adapterPosition);
            }
        });
    }

    /**
     * Method <b>createAndShowAlertDialogForManualType</b> creates and shows {@link AlertDialog}
     * for create new {@link Product} in {@link ShoppingList}.
     *
     * @param shoppingListId current {@link ShoppingList} id
     */
    private void createAndShowAlertDialogForManualType(final long shoppingListId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_new_product);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setHintTextColor(Color.BLACK);
        editTextCost.setText("0");

        final AutoCompleteTextView autoCompleteTextViewName = new AutoCompleteTextView(context);
        autoCompleteTextViewName.setInputType(InputType.TYPE_CLASS_TEXT);
        autoCompleteTextViewName.setHint(context.getString(R.string.enter_name));
        autoCompleteTextViewName.setHintTextColor(Color.BLACK);
        autoCompleteTextViewName.setTextColor(Color.BLACK);

        List<Product> allProducts = productDAO.getAll();
        AutoCompleteProductNamesAndCostsAdapter autoCompleteAdapter
                = new AutoCompleteProductNamesAndCostsAdapter(context, allProducts);
        autoCompleteTextViewName.setAdapter(autoCompleteAdapter);
        autoCompleteTextViewName.setTextColor(Color.BLACK);
        autoCompleteTextViewName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selected = (Product) parent.getAdapter().getItem(position);
                editTextCost.setText(String.valueOf(selected.getCost()));
            }
        });

        final EditText editTextQuantity = new EditText(context);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(context.getString(R.string.enter_quantity));
        editTextQuantity.setHintTextColor(Color.BLACK);
        editTextQuantity.setText("1");

        final TextInputLayout textInputLayoutCost = new TextInputLayout(context);
        final TextInputLayout textInputLayoutAuto = new TextInputLayout(context);
        final TextInputLayout textInputLayoutQuantity = new TextInputLayout(context);

        textInputLayoutCost.addView(editTextCost);
        textInputLayoutAuto.addView(autoCompleteTextViewName);
        textInputLayoutQuantity.addView(editTextQuantity);

        layout.addView(textInputLayoutAuto);
        layout.addView(textInputLayoutCost);
        layout.addView(textInputLayoutQuantity);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (autoCompleteTextViewName.getText() != null
                        && autoCompleteTextViewName.getText().toString().length() != 0) {
                    Product newProduct = new Product();
                    newProduct.setName(autoCompleteTextViewName.getText().toString());
                    if (editTextCost.getText() != null
                            && editTextCost.getText().toString().length() != 0
                            && editTextQuantity.getText() != null
                            && editTextQuantity.getText().toString().length() != 0) {
                        newProduct.setCost(Double.valueOf(editTextCost.getText().toString()));
                        ExistingProduct newExistingProduct = new ExistingProduct(Double
                                .parseDouble(editTextQuantity.getText().toString()));
                        boolean existence = productDAO
                                .addInCurrentShoppingListAndCheck(newProduct, shoppingListId);
                        notifyListsChanges(existence, newProduct, newExistingProduct, shoppingListId);
                        long tempProductId = productDAO.getOneByName(newProduct.getName()).getId();
                        ExistingProduct tempExistingProduct = existingProductDAO
                                .getOne(shoppingListId, tempProductId);
                        tempExistingProduct.setQuantityOrWeight(newExistingProduct.getQuantityOrWeight());
                        existingProductDAO.update(tempExistingProduct);
                        dialog.dismiss();
                    } else {
                        newProduct.setCost(Double.valueOf("0.0"));
                        ExistingProduct newExistingProduct = new ExistingProduct(Double
                                .parseDouble("1"));
                        boolean existence = productDAO
                                .addInCurrentShoppingListAndCheck(newProduct, shoppingListId);
                        notifyListsChanges(existence, newProduct, newExistingProduct, shoppingListId);
                        long tempProductId = productDAO.getOneByName(newProduct.getName()).getId();
                        ExistingProduct tempExistingProduct = existingProductDAO
                                .getOne(shoppingListId, tempProductId);
                        tempExistingProduct.setQuantityOrWeight(newExistingProduct.getQuantityOrWeight());
                        existingProductDAO.update(tempExistingProduct);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForManualType(shoppingListId);
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

    /**
     * The method <b>createAndShowAlertDialogForDelete</b> creates and shows a dialog, which
     * need to confirm deleting {@link ShoppingList}.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} are located.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.delete_shopping_list,
                shoppingLists.get(adapterPosition).getName()));
        builder.setMessage(context
                .getString(R.string.are_you_sure_you_want_to_delete_this_shopping_list));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingListDAO.delete(shoppingLists.get(adapterPosition));
                shoppingLists.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, shoppingLists.size());
                dialog.dismiss();
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

    /**
     * The method <b>createAndShowAlertDialogForUpdate</b> creates and shows a dialog for
     * update {@link ShoppingList}.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.ask_update_shopping_list,
                shoppingLists.get(adapterPosition).getName()));
        builder.setMessage(context.getString(R.string.ask_update_shopping_list_from_database));

        final EditText editTextName = new EditText(context);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(context.getString(R.string.enter_name));
        editTextName.setText(shoppingLists.get(adapterPosition).getName());

        final TextInputLayout textInputLayoutName = new TextInputLayout(context);
        textInputLayoutName.addView(editTextName);

        builder.setView(textInputLayoutName);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextName.getText() != null
                        && editTextName.getText().toString().length() != 0) {
                    ShoppingList shoppingList = shoppingLists.get(adapterPosition);
                    shoppingList.setName(editTextName.getText().toString());
                    shoppingListDAO.update(shoppingList);
                    shoppingLists.set(adapterPosition, shoppingList);
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, R.string.please_enter_name, Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForUpdate(adapterPosition);
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

    /**
     * The method <b>notifyListsChanges</b> updates {@link RecyclerView} items without recreate
     * {@link android.app.Activity} or {@link android.support.v4.app.Fragment}.
     *
     * @param existence          {@link Product} existence
     * @param newProduct         now created {@link Product}
     * @param newExistingProduct now created {@link ExistingProduct}
     * @param shoppingListId     {@link ShoppingList} id
     */
    private void notifyListsChanges(boolean existence, Product newProduct,
                                    ExistingProduct newExistingProduct,
                                    final long shoppingListId) {
        long tempProductId = productDAO.getOneByName(newProduct.getName()).getId();
        ExistingProduct tempExistingProduct = existingProductDAO
                .getOne(shoppingListId, tempProductId);
        tempExistingProduct.setQuantityOrWeight(newExistingProduct.getQuantityOrWeight());
        existingProductDAO.update(tempExistingProduct);
        if (!existence) {
            products.add(products.size(), newProduct);
            existingProducts.add(tempExistingProduct);
            childRVAdapter.notifyItemInserted(products.size());
        } else {
            for (Product p : products) {
                if (p.getName() != null && p.getName().contains(newProduct.getName())) {
                    int position = products.indexOf(p);
                    products.set(position, newProduct);
                    existingProducts.set(position, newExistingProduct);
                    childRVAdapter.notifyItemChanged(position);
                    break;
                }
            }
        }
    }

    /**
     * The method <b>calculationOfEstimatedAmount</b> calculates of estimated amounts all costs
     * {@link Product}s.
     *
     * @param products         all {@link Product}s in current {@link ShoppingList}
     * @param existingProducts all {@link ExistingProduct}s in current {@link ShoppingList}
     * @return estimated amount of all {@link Product}s in {@link ShoppingList}
     */
    private double calculationOfEstimatedAmount(List<Product> products,
                                                List<ExistingProduct> existingProducts) {
        double estimatedAmount = 0;
        if (existingProducts != null) {
            for (int i = 0; i < existingProducts.size(); i++) {
                double productCost = products.get(i).getCost();
                double existingProductQuantity = existingProducts.get(i).getQuantityOrWeight();
                estimatedAmount += productCost * existingProductQuantity;
            }
            return estimatedAmount;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    /**
     * The method <b>dateConverter</b> converts date to needed format.
     *
     * @param previousDate is the date before converting.
     * @return date after converting.
     */
    private String dateConverter(String previousDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context
                .getString(R.string.database_date_format), Locale.getDefault());
        Date finalDate = null;
        try {
            finalDate = simpleDateFormat.parse(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat(context.getString(R.string.needed_date_format),
                Locale.getDefault());
        return simpleDateFormat.format(finalDate);
    }


    /**
     * Static class {@link MainViewHolder} describes an item view and metadata about its place
     * within the {@link RecyclerView}.
     * Class extends {@link ViewHolder}.
     *
     * @author Lidchanin
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class MainViewHolder extends ViewHolder {

        private CardView cvMain;
        private TextView tvName;
        private TextView tvDate;
        private RecyclerView rvChild;
        private Button buttonChildAdd;
        private ImageButton buttonEdit;
        private ImageButton buttonDelete;
        private TextView tvEstimatedSum;

        /**
         * Constructor.
         *
         * @param itemView - item on {@link RecyclerView}.
         */
        MainViewHolder(View itemView) {
            super(itemView);
            cvMain = (CardView) itemView.findViewById(R.id.cv_in_main_rv);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_in_main_rv);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_in_main_rv);
            rvChild = (RecyclerView) itemView.findViewById(R.id.main_screen_child_rv);
            buttonChildAdd = (Button) itemView.findViewById(R.id.main_screen_child_add_button);
            buttonEdit = (ImageButton) itemView.findViewById(R.id.button_edit_in_main_rv);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete_in_main_rv);
            tvEstimatedSum = (TextView) itemView.findViewById(R.id.tv_estimated_sum);
        }
    }
}
