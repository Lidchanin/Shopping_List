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
import java.util.ArrayList;
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

    private ShoppingList shoppingList;
    private List<Product> products;
    private List<ExistingProduct> existingProducts;

    private ChildRVAdapter.OnDataChangeListener mOnDataChangeListener;

    public ChildRVAdapter(final Context context,
                          final ShoppingListDao shoppingListDao,
                          final ProductDao productDao,
                          final ExistingProductDao existingProductDao,
                          final ShoppingList shoppingList) {
        this.context = context;

        this.shoppingListDao = shoppingListDao;
        this.productDao = productDao;
        this.existingProductDao = existingProductDao;

        this.shoppingList = shoppingList;
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

        existingProducts = shoppingList.getExistingProducts();
        products = new ArrayList<>();
        for (ExistingProduct ep : existingProducts) {
            products.add(ep.getProduct());
        }

        final ExistingProduct existingProduct = existingProducts.get(adapterPosition);
        final Product product = existingProduct.getProduct();

        holder.cbExistence.setChecked(existingProduct.getIsPurchased());
        holder.cbExistence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingProduct.setIsPurchased(holder.cbExistence.isChecked());
                existingProductDao.update(existingProduct);
                existingProducts.set(holder.getAdapterPosition(), existingProduct);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.tvName.setText(product.getName());
        holder.tvCost.setText(new DecimalFormat("#0.00").format(product.getCost()));
        double totalCost = product.getCost() * existingProduct.getQuantity();
        holder.tvTotalCost.setText(new DecimalFormat("#0.00")
                .format(totalCost));
        holder.tvQuantity.setText(String.valueOf(existingProduct.getQuantity()));

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(adapterPosition, existingProduct, product);
            }
        });

        holder.cvChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(adapterPosition, existingProduct, product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.getExistingProducts().size();
    }

    /**
     * The method <b>createAndShowAlertDialogForDelete</b> creates and shows a dialog, which need
     * to confirm deleting {@link Product}.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link Product} are located.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition,
                                                   final ExistingProduct existingProduct,
                                                   final Product product) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_delete_product,
                product));
        builder.setMessage(context.getString(R.string.you_are_sure_you_want_to_delete_this_product));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                existingProductDao.delete(existingProduct);
//                products.remove(adapterPosition);
                existingProducts.remove(adapterPosition);
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onDataChanged(existingProducts);
                }
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, existingProducts.size());
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
     * The method <b>createAndShowAlertDialogForUpdate</b> creates and shows a dialog, which
     * need to update {@link Product} and {@link ExistingProduct}.
     * <p>
     * param adapterPosition the {@link RecyclerView} item position, where record about
     * {@link Product} and {@link ExistingProduct} are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition,
                                                   final ExistingProduct existingProduct,
                                                   final Product product) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.ask_update_product,
                product.getName()));
        builder.setMessage(context.getString(R.string.ask_update_product_from_database));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(context);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(context.getString(R.string.enter_name));
        editTextName.setText(product.getName());

        final TextInputLayout textInputLayoutName = new TextInputLayout(context);
        textInputLayoutName.addView(editTextName);

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setText(String.valueOf(product.getCost()));
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

        layout.addView(textInputLayoutName);
        layout.addView(textInputLayoutCost);
        layout.addView(textInputLayoutQuantity);

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
                    product.setName(editTextName.getText().toString());
                    product.setCost(Double.valueOf(editTextCost.getText().toString()));
                    productDao.update(product);
                    products.set(adapterPosition, product);

                    existingProduct.setQuantity(Double
                            .parseDouble(editTextQuantity.getText().toString()));
                    existingProductDao.update(existingProduct);
                    existingProducts.set(adapterPosition, existingProduct);

                    if (mOnDataChangeListener != null) {
                        mOnDataChangeListener.onDataChanged(existingProducts);
                    }
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data, Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForUpdate(adapterPosition, existingProduct, product);
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
        void onDataChanged(List<ExistingProduct> existingProducts);
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
