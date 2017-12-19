package com.lidchanin.shoppinglist.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.shoppinglist.R;
import com.lidchanin.shoppinglist.database.Product;
import com.lidchanin.shoppinglist.database.ShoppingList;
import com.lidchanin.shoppinglist.database.UsedProduct;
import com.lidchanin.shoppinglist.database.dao.ProductDao;
import com.lidchanin.shoppinglist.database.dao.ShoppingListDao;
import com.lidchanin.shoppinglist.database.dao.UsedProductDao;
import com.lidchanin.shoppinglist.utils.ModelUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Class {@link ListsChildRVAdapter} provide a binding from an app-specific data set to views that
 * are displayed within a {@link RecyclerView}.
 * Class extends {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class ListsChildRVAdapter
        extends RecyclerView.Adapter<ListsChildRVAdapter.ListsChildViewHolder> {

    private static final String TAG = "ListsChildRVAdapter";

    private final Context context;

    private final ShoppingListDao shoppingListDao;
    private final ProductDao productDao;
    private final UsedProductDao usedProductDao;

    private List<ShoppingList> shoppingLists;
    private int mainAdapterPosition;

    private ListsChildRVAdapter.OnDataChangeListener mOnDataChangeListener;

    public ListsChildRVAdapter(final Context context,
                               final ShoppingListDao shoppingListDao,
                               final ProductDao productDao,
                               final UsedProductDao usedProductDao,
                               final List<ShoppingList> shoppingLists,
                               final int mainAdapterPosition) {
        this.context = context;

        this.shoppingListDao = shoppingListDao;
        this.productDao = productDao;
        this.usedProductDao = usedProductDao;

        this.shoppingLists = shoppingLists;
        this.mainAdapterPosition = mainAdapterPosition;
    }

    @Override
    public ListsChildViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_lists_child_rv, parent, false);
        return new ListsChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListsChildViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final UsedProduct usedProduct = shoppingLists.get(mainAdapterPosition)
                .getUsedProducts().get(adapterPosition);

        holder.cbExistence.setChecked(usedProduct.getIsPurchased());

        holder.cbExistence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usedProduct.setIsPurchased(holder.cbExistence.isChecked());
                shoppingLists.get(mainAdapterPosition).getUsedProducts()
                        .set(adapterPosition, usedProduct);
                usedProductDao.update(usedProduct);
                notifyItemChanged(adapterPosition);
            }
        });

        holder.tvName.setText(usedProduct.getProduct().getName());
        holder.tvCost.setText(new DecimalFormat("#0.00")
                .format(usedProduct.getProduct().getCost()));
        double totalCost = usedProduct.getProduct().getCost() * usedProduct.getQuantity();
        holder.tvTotalCost.setText(new DecimalFormat("#0.00").format(totalCost));
        holder.tvQuantity.setText(context.getString(R.string.tv_quantity,
                String.valueOf(usedProduct.getQuantity()),
                usedProduct.getUnit() ? context.getString(R.string.kg)  : context.getString(R.string.pieces)));

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(adapterPosition, usedProduct);
            }
        });

        holder.cvChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(adapterPosition, usedProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingLists.get(mainAdapterPosition).getUsedProducts().size();
    }

    /**
     * The method <b>createAndShowAlertDialogForDelete</b> creates and shows a {@link AlertDialog}
     * {@link UsedProduct} deleting.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link UsedProduct} are located.
     * @param usedProduct     required {@link UsedProduct} for deleting.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition,
                                                   final UsedProduct usedProduct) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_delete_product,
                usedProduct.getProduct().getName()));
        builder.setMessage(context.getString(R.string.you_are_sure_you_want_to_delete_this_product));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                usedProductDao.delete(usedProduct);

                shoppingLists.get(mainAdapterPosition).getUsedProducts().remove(usedProduct);
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onDataChanged(shoppingLists);
                }
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition,
                        shoppingLists.get(mainAdapterPosition).getUsedProducts().size());
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
     * The method <b>createAndShowAlertDialogForUpdate</b> creates and shows a {@link AlertDialog},
     * which need to update {@link Product} and {@link UsedProduct}.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link UsedProduct} are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition,
                                                   final UsedProduct usedProduct) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_update_product,
                shoppingLists.get(mainAdapterPosition).getUsedProducts().get(adapterPosition)
                        .getProduct().getName()));
        builder.setMessage(context.getString(R.string.ask_update_product_from_database));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(context);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(context.getString(R.string.enter_name));
        editTextName.setSelectAllOnFocus(true);
        editTextName.setText(usedProduct.getProduct().getName());

        final TextInputLayout textInputLayoutName = new TextInputLayout(context);
        textInputLayoutName.addView(editTextName);

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setText(String.valueOf(usedProduct.getProduct().getCost()));
        editTextCost.setSelectAllOnFocus(true);

        final TextInputLayout textInputLayoutCost = new TextInputLayout(context);
        textInputLayoutCost.addView(editTextCost);

        final EditText editTextQuantity = new EditText(context);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(R.string.enter_quantity);
        editTextQuantity.setText(String.valueOf(usedProduct.getQuantity()));
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
        radioGroup.check((
                usedProduct.getUnit() ? radioButtonKg.getId() : radioButtonUnit.getId())
        );

        layout.addView(textInputLayoutName);
        layout.addView(textInputLayoutCost);
        layout.addView(textInputLayoutQuantity);
        layout.addView(radioGroup);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredName = editTextName.getText().toString();
                double enteredCost = Double.valueOf(editTextCost.getText().toString());
                double enteredQuantity = Double.valueOf(editTextQuantity.getText().toString());
                boolean enteredUnit = radioButtonKg.isChecked();

                if (ModelUtils.isProductExists(productDao.loadAll(), enteredName)) {
                    Log.i(TAG, "Product \"" + enteredName + "\" exists in the db.");
                    if (usedProduct.getProduct().getName().equals(enteredName)) {
                        Log.i(TAG, "Product \"" + enteredName + "\" equals entered name.");
                        usedProduct.getProduct().setCost(enteredCost);
                        productDao.update(usedProduct.getProduct());
                        updateUsedProductAndUI(usedProduct, enteredUnit, enteredQuantity,
                                usedProduct.getProduct(), adapterPosition);
                    } else {
                        Log.i(TAG, "Product \"" + enteredName + "\" not equals entered name.");
                        List<Product> productsInList = new ArrayList<>();
                        for (UsedProduct up :
                                shoppingLists.get(mainAdapterPosition).getUsedProducts()) {
                            productsInList.add(up.getProduct());
                        }
                        if (ModelUtils.isProductExists(productsInList, enteredName)) {
                            Log.i(TAG, "Product \"" + enteredName + "\" exists in current " +
                                    "shopping list.");
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.product_name_exists_in_list, enteredName),
                                    Toast.LENGTH_SHORT
                            ).show();
                            createAndShowAlertDialogForUpdate(adapterPosition, usedProduct);
                        } else {
                            Log.i(TAG, "Product \"" + enteredName + "\" not exists in current " +
                                    "shopping list.");
                            Product productInDB = productDao.queryBuilder()
                                    .where(ProductDao.Properties.Name.eq(enteredName)).unique();
                            productInDB.setCost(enteredCost);
                            productDao.update(productInDB);
                            updateUsedProductAndUI(usedProduct, enteredUnit, enteredQuantity,
                                    productInDB, adapterPosition);
                        }
                    }
                } else {
                    Log.i(TAG, "Product \"" + enteredName + "\" not exists in the db.");
                    Product product = new Product();
                    product.setName(enteredName);
                    product.setCost(enteredCost);
                    product.setId(productDao.insert(product));
                    updateUsedProductAndUI(usedProduct, enteredUnit, enteredQuantity, product,
                            adapterPosition);
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

    private void updateUsedProductAndUI(final UsedProduct usedProduct,
                                        final boolean enteredUnit,
                                        final double enteredQuantity,
                                        final Product product,
                                        final int adapterPosition) {
        usedProduct.setUnit(enteredUnit);
        usedProduct.setQuantity(enteredQuantity);
        usedProduct.setProductId(product.getId());
        usedProduct.setProduct(product);
        usedProductDao.update(usedProduct);
        shoppingLists.get(mainAdapterPosition).getUsedProducts()
                .set(adapterPosition, usedProduct);
        if (mOnDataChangeListener != null) {
            mOnDataChangeListener.onDataChanged(shoppingLists);
        }
        notifyItemChanged(adapterPosition);
    }

    public void setOnDataChangeListener(OnDataChangeListener mOnDataChangeListener) {
        this.mOnDataChangeListener = mOnDataChangeListener;
    }

    public interface OnDataChangeListener {
        void onDataChanged(List<ShoppingList> shoppingLists);
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    /**
     * Static class {@link ListsChildViewHolder} describes an item view and metadata about its place
     * within the {@link RecyclerView}.
     * Class extends {@link ViewHolder}.
     *
     * @author Lidchanin
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class ListsChildViewHolder extends ViewHolder {

        private CardView cvChild;
        private TextView tvName;
        private TextView tvCost;
        private ImageButton buttonDelete;
        private TextView tvTotalCost;
        private TextView tvQuantity;
        private CheckBox cbExistence;

        ListsChildViewHolder(View itemView) {
            super(itemView);
            cvChild = (CardView) itemView.findViewById(R.id.cv_in_child_rv);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_in_child_rv);
            tvCost = (TextView) itemView.findViewById(R.id.tv_cost_in_child_rv);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity_in_child_rv);
            tvTotalCost = (TextView) itemView.findViewById(R.id.tv_total_cost_in_child_rv);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete_in_child_rv);
            cbExistence = (CheckBox) itemView.findViewById(R.id.cb_existence_in_child_rv);
        }
    }
}
