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
     * @param context       context
     * @param shoppingLists List with all shopping lists
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

    private static boolean isProductExists(final List<Product> products,
                                           final String name) {
        for (Product product : products) {
            if (product != null && product.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

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
                    public void onDataChanged(List<ExistingProduct> existingProducts) {
                        holder.tvEstimatedSum
                                .setText(context.getString(R.string.estimated_amount,
                                        new DecimalFormat("#.##").format(calculationOfEstimatedAmount(shoppingLists.get(adapterPosition).getExistingProducts()))));
                    }
                });
                holder.rvChild.setAdapter(childRVAdapter);

                holder.tvEstimatedSum.setText(context.getString(R.string.estimated_amount,
                        new DecimalFormat("#.##").format(calculationOfEstimatedAmount(shoppingLists.get(adapterPosition).getExistingProducts()))));

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
                        createAndShowAlertDialogForManualType(adapterPosition);
                    }
                });
            }
        });

        holder.tvName.setText(shoppingLists.get(adapterPosition).getName());
        holder.tvDate.setText(shoppingLists.get(adapterPosition).getDate());

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
     */
    private void createAndShowAlertDialogForManualType(final int adapterPosition) {
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

        final List<Product> allProductsInDB = productDao.loadAll();

        AutoCompleteProductNamesAndCostsAdapter autoCompleteAdapter
                = new AutoCompleteProductNamesAndCostsAdapter(context, allProductsInDB);
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
                        tempExistingProduct.setShoppingListId(shoppingLists.get(adapterPosition).getId());
                        tempExistingProduct.setIsPurchased(false);

                        boolean productExistence = isProductExists(allProductsInDB, tempProduct.getName());
                        if (productExistence) {
                            Log.d(TAG, "Product " + tempProduct.getName() + " exists in the db");
                            tempProduct.setId(productDao.queryBuilder()
                                    .where(ProductDao.Properties.Name.eq(tempProduct.getName()))
                                    .unique().getId());
                            tempProduct.setPopularity(productDao.queryBuilder()
                                    .where(ProductDao.Properties.Name.eq(tempProduct.getName()))
                                    .unique().getPopularity());
                            productDao.update(tempProduct);
                        } else {
                            Log.d(TAG, "Product " + tempProduct.getName() + " is not in the db");
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
                            tempExistingProduct.setId(existingProductDao.queryBuilder()
                                    .where(ExistingProductDao.Properties.ShoppingListId.eq(tempExistingProduct.getShoppingListId()),
                                            ExistingProductDao.Properties.ProductId.eq(tempExistingProduct.getProductId()))
                                    .unique().getId());
                            existingProductDao.update(tempExistingProduct);

                            for (ExistingProduct ep : shoppingLists.get(adapterPosition).getExistingProducts()) {
                                if (ep.getProduct().getName() != null && ep.getProduct().getName()
                                        .contains(tempProduct.getName())) {
                                    int position = shoppingLists.get(adapterPosition).getExistingProducts().indexOf(ep);
                                    shoppingLists.get(adapterPosition).getExistingProducts().set(position, tempExistingProduct);
                                    childRVAdapter.notifyItemChanged(position);
                                    break;
                                }
                            }
                        } else {
                            tempExistingProduct.setId(existingProductDao.insert(tempExistingProduct));
                            shoppingLists.get(adapterPosition).getExistingProducts().add(tempExistingProduct);
                            childRVAdapter.notifyItemInserted(shoppingLists.get(adapterPosition).getExistingProducts().size());
                        }

                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForManualType(adapterPosition);
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
        builder.setMessage(context.getString(R.string.are_you_sure_you_want_to_delete_this_shopping_list));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingListDao.delete(shoppingLists.get(adapterPosition));
                existingProductDao.deleteInTx(shoppingLists.get(adapterPosition).getExistingProducts());
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
                    shoppingLists.get(adapterPosition).setName(editTextName.getText().toString());
                    shoppingListDao.update(shoppingLists.get(adapterPosition));
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
     * The method <b>calculationOfEstimatedAmount</b> calculates of estimated amounts all costs
     * {@link Product}s.
     *
     * @return estimated amount of all {@link Product}s in {@link ShoppingList}
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
