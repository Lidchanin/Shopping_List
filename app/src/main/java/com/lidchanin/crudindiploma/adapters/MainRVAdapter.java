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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.database.ExistingProduct;
import com.lidchanin.crudindiploma.database.ExistingProductDao;
import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ProductDao;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.ShoppingListDao;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Class {@link MainRVAdapter} provide a binding from an app-specific data set to views that are
 * displayed within a {@link RecyclerView}.
 * Class extends {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.MainViewHolder> {

    private static final String TAG = "MainRVAdapter";

    private Context context;

    private ShoppingListDao shoppingListDao;
    private ProductDao productDao;
    private ExistingProductDao existingProductDao;

    private List<ShoppingList> shoppingLists;

    private ChildRVAdapter childRVAdapter;

    /**
     * Constructor.
     *
     * @param context            {@link Context}.
     * @param shoppingLists      {@link List} with all {@link ShoppingList}s.
     * @param shoppingListDao    {@link ShoppingListDao} exemplar.
     * @param productDao         {@link ProductDao} exemplar.
     * @param existingProductDao {@link ExistingProductDao} exemplar.
     */
    public MainRVAdapter(Context context,
                         ShoppingListDao shoppingListDao,
                         ProductDao productDao,
                         ExistingProductDao existingProductDao,
                         List<ShoppingList> shoppingLists) {
        this.context = context;

        this.shoppingListDao = shoppingListDao;
        this.productDao = productDao;
        this.existingProductDao = existingProductDao;

        this.shoppingLists = shoppingLists;

    }

    /**
     * The method <b>isProductExists</b> checks is {@link Product} exists in database or not.
     *
     * @param products all {@link Product}s in database.
     * @param name     needed {@link Product} name.
     * @return <i>true</i> - if {@link Product} exists <br> <i>false</i> - if not exists.
     */
    private static boolean isProductExists(final List<Product> products,
                                           final String name) {
        for (Product product : products) {
            if (product != null && product.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method <b>isExProductExists</b> checks is {@link ExistingProduct} exists in database or
     * not.
     *
     * @param existingProducts all {@link ExistingProduct}s in database.
     * @param shoppingListId   needed {@link ShoppingList} id.
     * @param productId        needed {@link Product} id.
     * @return <i>true</i> - if {@link ExistingProduct} exists <br> <i>false</i> - if not exists.
     */
    private static boolean isExProductExists(final List<ExistingProduct> existingProducts,
                                             final long shoppingListId,
                                             final long productId) {
        for (ExistingProduct existingProduct : existingProducts) {
            if (existingProduct != null && existingProduct.getShoppingListId() == shoppingListId
                    && existingProduct.getProductId().equals(productId)) {
                return true;
            }
        }
        return false;
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
        final ShoppingList shoppingList = shoppingLists.get(adapterPosition);

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                holder.rvChild.setLayoutManager(layoutManager);

                childRVAdapter = new ChildRVAdapter(context, shoppingListDao, productDao,
                        existingProductDao, shoppingLists, adapterPosition);
                childRVAdapter.setOnDataChangeListener(new ChildRVAdapter
                        .OnDataChangeListener() {
                    @Override
                    public void onDataChanged(List<ShoppingList> shoppingLists) {
                        holder.tvEstimatedSum
                                .setText(context.getString(R.string.estimated_amount,
                                        new DecimalFormat("#.##").format(
                                                calculationOfEstimatedAmount(shoppingList
                                                        .getExistingProducts()))));
                    }
                });
                holder.rvChild.setAdapter(childRVAdapter);

                holder.tvEstimatedSum.setText(context.getString(R.string.estimated_amount,
                        new DecimalFormat("#.##").format(calculationOfEstimatedAmount(
                                shoppingList.getExistingProducts()))));

                if (holder.rvChild.getVisibility() == View.GONE) {
                    holder.rvChild.setVisibility(View.VISIBLE);
                    holder.buttonChildAdd.setVisibility(View.VISIBLE);
                    holder.tvEstimatedSum.setVisibility(View.VISIBLE);
                } else {
                    holder.rvChild.setVisibility(View.GONE);
                    holder.buttonChildAdd.setVisibility(View.GONE);
                    holder.tvEstimatedSum.setVisibility(View.GONE);
                }
            }
        });

        holder.tvName.setText(shoppingList.getName());
        holder.tvDate.setText(shoppingList.getDate());

        holder.buttonChildAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForManualType(adapterPosition, holder);
            }
        });

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(adapterPosition, shoppingList);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(adapterPosition, shoppingList);
            }
        });
    }

    /**
     * Method <b>createAndShowAlertDialogForManualType</b> creates and displays {@link AlertDialog}
     * for {@link ShoppingList} manual type.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} will located.
     * @param holder          current {@link MainViewHolder}.
     */
    private void createAndShowAlertDialogForManualType(final int adapterPosition,
                                                       final MainViewHolder holder) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(R.string.add_new_product);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setText("0");
        editTextCost.setSelectAllOnFocus(true);
        final TextInputLayout textInputLayoutCost = new TextInputLayout(context);
        textInputLayoutCost.addView(editTextCost);

        final AutoCompleteTextView autoCompleteTextViewName = new AutoCompleteTextView(context);
        autoCompleteTextViewName.setInputType(InputType.TYPE_CLASS_TEXT);
        autoCompleteTextViewName.setHint(context.getString(R.string.enter_name));
        autoCompleteTextViewName.setHintTextColor(Color.BLACK);
        autoCompleteTextViewName.setTextColor(Color.BLACK);
        final List<Product> allProductsInDB = productDao.loadAll();
        AutoCompleteProductNamesAndCostsAdapter autoCompleteAdapter
                = new AutoCompleteProductNamesAndCostsAdapter(context, allProductsInDB);
        autoCompleteTextViewName.setAdapter(autoCompleteAdapter);
        autoCompleteTextViewName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selected = (Product) parent.getAdapter().getItem(position);
                autoCompleteTextViewName.setText(selected.getName());
                editTextCost.setText(String.valueOf(selected.getCost()));
            }
        });
        final TextInputLayout textInputLayoutAuto = new TextInputLayout(context);
        textInputLayoutAuto.addView(autoCompleteTextViewName);

        final EditText editTextQuantity = new EditText(context);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(context.getString(R.string.enter_quantity));
        editTextQuantity.setHintTextColor(Color.BLACK);
        editTextQuantity.setText("1");
        editTextQuantity.setSelectAllOnFocus(true);
        final TextInputLayout textInputLayoutQuantity = new TextInputLayout(context);
        textInputLayoutQuantity.addView(editTextQuantity);

        final RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        final RadioButton radioButtonKg = new RadioButton(context);
        radioButtonKg.setText(context.getString(R.string.kg));

        final RadioButton radioButtonUnit = new RadioButton(context);
        radioButtonUnit.setText(context.getString(R.string.unit));

        radioGroup.addView(radioButtonKg);
        radioGroup.addView(radioButtonUnit);
        radioGroup.check(radioButtonKg.getId());

        layout.addView(textInputLayoutAuto);
        layout.addView(textInputLayoutCost);
        layout.addView(textInputLayoutQuantity);
        layout.addView(radioGroup);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (autoCompleteTextViewName.getText() != null
                        && autoCompleteTextViewName.getText().toString().length() != 0) {

                    if (editTextCost.getText() != null
                            && editTextCost.getText().toString().length() != 0
                            && editTextQuantity.getText() != null
                            && editTextQuantity.getText().toString().length() != 0) {

                        Product tempProduct = new Product();
                        tempProduct.setName(autoCompleteTextViewName.getText().toString());
                        tempProduct.setCost(Double.valueOf(editTextCost.getText().toString()));

                        ExistingProduct tempExistingProduct = new ExistingProduct();
                        tempExistingProduct.setQuantity(Double
                                .parseDouble(editTextQuantity.getText().toString()));
                        tempExistingProduct.setShoppingListId(shoppingLists.get(adapterPosition)
                                .getId());
                        tempExistingProduct.setIsPurchased(false);
                        tempExistingProduct.setUnit(radioButtonKg.isChecked());

                        boolean productExistence = isProductExists(allProductsInDB,
                                tempProduct.getName());
                        if (productExistence) {
                            Log.i(TAG, "Product " + tempProduct.getName() + " exists in the db.");
                            tempProduct.setId(productDao.queryBuilder()
                                    .where(ProductDao.Properties.Name.eq(tempProduct.getName()))
                                    .unique().getId());
                            tempProduct.setPopularity(productDao.queryBuilder()
                                    .where(ProductDao.Properties.Name.eq(tempProduct.getName()))
                                    .unique().getPopularity());
                            productDao.update(tempProduct);
                        } else {
                            Log.i(TAG, "Product " + tempProduct.getName() + " is not in the db.");
                            tempProduct.setPopularity((long) 0);
                            tempProduct.setId(productDao.insert(tempProduct));
                        }

                        tempExistingProduct.setProductId(tempProduct.getId());
                        tempExistingProduct.setProduct(tempProduct);

                        boolean exProductExistence = isExProductExists(
                                shoppingLists.get(adapterPosition).getExistingProducts(),
                                tempExistingProduct.getShoppingListId(),
                                tempExistingProduct.getProductId());
                        if (exProductExistence) {
                            Log.i(TAG, "ExProduct " + tempExistingProduct.getProduct().getName()
                                    + " in " + tempExistingProduct.getShoppingListId()
                                    + " exists.");
                            tempExistingProduct.setId(existingProductDao.queryBuilder()
                                    .where(ExistingProductDao.Properties.ShoppingListId
                                                    .eq(tempExistingProduct.getShoppingListId()),
                                            ExistingProductDao.Properties.ProductId
                                                    .eq(tempExistingProduct.getProductId()))
                                    .unique().getId());
                            existingProductDao.update(tempExistingProduct);

                            for (ExistingProduct ep :
                                    shoppingLists.get(adapterPosition).getExistingProducts()) {
                                if (ep.getProduct().getName() != null && ep.getProduct().getName()
                                        .contains(tempProduct.getName())) {
                                    int position = shoppingLists.get(adapterPosition)
                                            .getExistingProducts().indexOf(ep);
                                    shoppingLists.get(adapterPosition).getExistingProducts()
                                            .set(position, tempExistingProduct);
                                    childRVAdapter.notifyItemChanged(position);
                                    break;
                                }
                            }
                        } else {
                            Log.i(TAG, "ExProduct " + tempExistingProduct.getProduct().getName()
                                    + " not exists.");
                            tempExistingProduct.setId(existingProductDao.insert(tempExistingProduct));
                            shoppingLists.get(adapterPosition).getExistingProducts()
                                    .add(tempExistingProduct);
                            childRVAdapter.notifyItemInserted(shoppingLists.get(adapterPosition)
                                    .getExistingProducts().size());
                        }
                        holder.tvEstimatedSum
                                .setText(context.getString(R.string.estimated_amount,
                                        new DecimalFormat("#.##").format(
                                                calculationOfEstimatedAmount(shoppingLists
                                                        .get(adapterPosition)
                                                        .getExistingProducts()))));
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForManualType(adapterPosition, holder);
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
     * The method <b>createAndShowAlertDialogForDelete</b> creates and shows a {@link AlertDialog},
     * for {@link ShoppingList} deleting.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} are located.
     * @param shoppingList    required {@link ShoppingList} for deleting.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition,
                                                   final ShoppingList shoppingList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.delete_shopping_list, shoppingList.getName()));
        builder.setMessage(context.getString(R.string.are_you_sure_you_want_to_delete_this_shopping_list));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingListDao.delete(shoppingList);
                existingProductDao.deleteInTx(shoppingList.getExistingProducts());
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
     * The method <b>createAndShowAlertDialogForUpdate</b> creates and shows a {@link AlertDialog}
     * for {@link ShoppingList} updating.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} are located.
     * @param shoppingList    required {@link ShoppingList} for updating.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition,
                                                   final ShoppingList shoppingList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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
                    shoppingList.setName(editTextName.getText().toString());
                    shoppingLists.set(adapterPosition, shoppingList);
                    shoppingListDao.update(shoppingLists.get(adapterPosition));
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, R.string.please_enter_name, Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForUpdate(adapterPosition, shoppingList);
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
     * The method <b>calculationOfEstimatedAmount</b> calculates of estimated amounts all costs
     * {@link ExistingProduct}s.
     *
     * @return estimated amount of all {@link ExistingProduct}s in {@link ShoppingList}.
     */
    private double calculationOfEstimatedAmount(List<ExistingProduct> existingProducts) {
        double estimatedAmount = 0;
        for (ExistingProduct ep : existingProducts) {
            estimatedAmount += ep.getQuantity() * ep.getProduct().getCost();
        }
        return estimatedAmount;
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    /**
     * Static class {@link MainViewHolder} describes an item view and metadata about its place
     * within the {@link RecyclerView}.
     * Class extends {@link ViewHolder}.
     *
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
         * @param itemView - item in {@link RecyclerView}.
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
