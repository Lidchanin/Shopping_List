package com.lidchanin.crudindiploma.adapters;

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
 * Class {@link ChildRVAdapter} provide a binding from an app-specific data set to views that are
 * displayed within a {@link RecyclerView}.
 * Class extends {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class ChildRVAdapter extends RecyclerView.Adapter<ChildRVAdapter.ChildViewHolder> {

    private static final String TAG = "ChildRVAdapter";

    private final Context context;

    private final ShoppingListDao shoppingListDao;
    private final ProductDao productDao;
    private final ExistingProductDao existingProductDao;

    private List<ShoppingList> shoppingLists;
    private int mainAdapterPosition;

    private ChildRVAdapter.OnDataChangeListener mOnDataChangeListener;

    public ChildRVAdapter(final Context context,
                          final ShoppingListDao shoppingListDao,
                          final ProductDao productDao,
                          final ExistingProductDao existingProductDao,
                          final List<ShoppingList> shoppingLists,
                          final int mainAdapterPosition) {
        this.context = context;

        this.shoppingListDao = shoppingListDao;
        this.productDao = productDao;
        this.existingProductDao = existingProductDao;

        this.shoppingLists = shoppingLists;
        this.mainAdapterPosition = mainAdapterPosition;
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

    @Override
    public ChildViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_child_rv, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChildViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final ExistingProduct existingProduct = shoppingLists.get(mainAdapterPosition)
                .getExistingProducts().get(adapterPosition);

        holder.cbExistence.setChecked(existingProduct.getIsPurchased());

        holder.cbExistence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingProduct.setIsPurchased(holder.cbExistence.isChecked());
                shoppingLists.get(mainAdapterPosition).getExistingProducts()
                        .set(adapterPosition, existingProduct);
                existingProductDao.update(existingProduct);
                notifyItemChanged(adapterPosition);
            }
        });

        holder.tvName.setText(existingProduct.getProduct().getName());
        holder.tvCost.setText(new DecimalFormat("#0.00")
                .format(existingProduct.getProduct().getCost()));
        double totalCost = existingProduct.getProduct().getCost() * existingProduct.getQuantity();
        holder.tvTotalCost.setText(new DecimalFormat("#0.00").format(totalCost));
        holder.tvQuantity.setText(context.getString(R.string.tv_quantity,
                String.valueOf(existingProduct.getQuantity()),
                existingProduct.getUnit() ? "kg" : "unit"));

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(adapterPosition, existingProduct);
            }
        });

        holder.cvChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(adapterPosition, existingProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingLists.get(mainAdapterPosition).getExistingProducts().size();
    }

    /**
     * The method <b>createAndShowAlertDialogForDelete</b> creates and shows a {@link AlertDialog}
     * {@link ExistingProduct} deleting.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ExistingProduct} are located.
     * @param existingProduct required {@link ExistingProduct} for deleting.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition,
                                                   final ExistingProduct existingProduct) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_delete_product,
                existingProduct.getProduct().getName()));
        builder.setMessage(context.getString(R.string.you_are_sure_you_want_to_delete_this_product));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                existingProductDao.delete(existingProduct);

                shoppingLists.get(mainAdapterPosition).getExistingProducts().remove(existingProduct);
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onDataChanged(shoppingLists);
                }
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition,
                        shoppingLists.get(mainAdapterPosition).getExistingProducts().size());
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
     * which need to update {@link Product} and {@link ExistingProduct}.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ExistingProduct} are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition,
                                                   final ExistingProduct existingProduct) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_update_product, shoppingLists.get(mainAdapterPosition).getExistingProducts().get(adapterPosition).getProduct().getName()));
        builder.setMessage(context.getString(R.string.ask_update_product_from_database));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(context);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(context.getString(R.string.enter_name));
        editTextName.setSelectAllOnFocus(true);
        editTextName.setText(existingProduct.getProduct().getName());

        final TextInputLayout textInputLayoutName = new TextInputLayout(context);
        textInputLayoutName.addView(editTextName);

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setText(String.valueOf(existingProduct.getProduct().getCost()));
        editTextCost.setSelectAllOnFocus(true);

        final TextInputLayout textInputLayoutCost = new TextInputLayout(context);
        textInputLayoutCost.addView(editTextCost);

        final EditText editTextQuantity = new EditText(context);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(R.string.enter_quantity);
        editTextQuantity.setText(String.valueOf(existingProduct.getQuantity()));
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
                existingProduct.getUnit() ? radioButtonKg.getId() : radioButtonUnit.getId())
        );

        layout.addView(textInputLayoutName);
        layout.addView(textInputLayoutCost);
        layout.addView(textInputLayoutQuantity);
        layout.addView(radioGroup);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextName.getText() != null
                        && editTextName.getText().toString().length() != 0
                        && editTextCost.getText() != null
                        && editTextCost.getText().toString().length() != 0
                        && editTextQuantity.getText() != null
                        && editTextQuantity.getText().toString().length() != 0) {

                    Product product = existingProduct.getProduct();
                    product.setCost(Double.valueOf(editTextCost.getText().toString()));
                    try {
                        if (isProductExists(productDao.loadAll(), 
                                editTextName.getText().toString())) {
                            Log.i(TAG, "Product \"" + editTextName.getText().toString()
                                    + "\" exists in database. The product is being updated.");
                            productDao.update(product);
                        } else {
                            Log.i(TAG, "Product \"" + editTextName.getText().toString()
                                    + "\" not exists in database. The product is being created.");
                            product.setName(editTextName.getText().toString());
                            product.setId(null);
                            product.setId(productDao.insert(product));
                        }
                    } finally {
                        existingProduct.setProduct(product);
                        existingProduct.setQuantity(Double.parseDouble(editTextQuantity.getText()
                                .toString()));
                        existingProduct.setUnit(radioButtonKg.isChecked());
                        existingProductDao.update(existingProduct);
                        shoppingLists.get(mainAdapterPosition).getExistingProducts()
                                .set(adapterPosition, existingProduct);

                        if (mOnDataChangeListener != null) {
                            mOnDataChangeListener.onDataChanged(shoppingLists);
                        }
                        notifyItemChanged(adapterPosition);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data, Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForUpdate(adapterPosition, existingProduct);
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
     * Static class {@link ChildViewHolder} describes an item view and metadata about its place
     * within the {@link RecyclerView}.
     * Class extends {@link ViewHolder}.
     *
     * @author Lidchanin
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class ChildViewHolder extends ViewHolder {

        private CardView cvChild;
        private TextView tvName;
        private TextView tvCost;
        private ImageButton buttonDelete;
        private TextView tvTotalCost;
        private TextView tvQuantity;
        private CheckBox cbExistence;

        ChildViewHolder(View itemView) {
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
