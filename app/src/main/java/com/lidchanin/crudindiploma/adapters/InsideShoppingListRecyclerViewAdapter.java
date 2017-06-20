package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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
import com.lidchanin.crudindiploma.activities.InsideShoppingListActivity;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Class <code>InsideShoppingListRecyclerViewAdapter</code> is an adapter for {@link RecyclerView}
 * from {@link InsideShoppingListActivity}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class InsideShoppingListRecyclerViewAdapter extends RecyclerView
        .Adapter<InsideShoppingListRecyclerViewAdapter.InsideShoppingListViewHolder> {

    private List<Product> products;
    private List<ExistingProduct> existingProducts;

    private ProductDAO productDAO;
    private ExistingProductDAO existingProductDAO;

    private Context context;
    private long shoppingListId;
    private OnDataChangeListener mOnDataChangeListener;

    public InsideShoppingListRecyclerViewAdapter(
            List<Product> products, List<ExistingProduct> existingProducts,
            ProductDAO productDAO, ExistingProductDAO existingProductDAO,
            Context context, long shoppingListId) {
        this.products = products;
        this.existingProducts = existingProducts;
        this.context = context;
        this.shoppingListId = shoppingListId;
        this.productDAO = productDAO;
        this.existingProductDAO = existingProductDAO;
    }

    @Override
    public InsideShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item_inside_shopping_list, parent, false);
        return new InsideShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InsideShoppingListViewHolder holder, final int position) {
        final Product product = products.get(holder.getAdapterPosition());
        final ExistingProduct existingProduct = existingProducts.get(holder.getAdapterPosition());

        holder.checkBoxIsPurchased.setChecked(existingProduct.isPurchased());
        holder.checkBoxIsPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingProduct.setPurchased(holder.checkBoxIsPurchased.isChecked());
                existingProductDAO.update(existingProduct);
                existingProducts.set(holder.getAdapterPosition(), existingProduct);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductCost.setText(new DecimalFormat("#0.00").format(product.getCost()));
        double totalCost = product.getCost() * existingProduct.getQuantityOrWeight();
        holder.textViewTotalCost.setText(new DecimalFormat("#0.00")
                .format(totalCost));
        holder.textViewQuantity.setText(String.valueOf(existingProduct.getQuantityOrWeight()));
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(holder.getAdapterPosition());
            }
        });
        holder.cardViewProduct.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createAndShowAlertDialogForUpdate(holder.getAdapterPosition());
                return true;
            }
        });
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Method <code>createAndShowAlertDialogForDelete</code> create and shows a dialog, which need
     * to confirm deleting product.
     *
     * @param adapterPosition is the position, where record about product are located.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_delete_product,
                products.get(adapterPosition)));
        builder.setMessage(context.getString(R.string.you_are_sure_you_want_to_delete_this_product));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                existingProductDAO.deleteOneFromCurrentShoppingList(shoppingListId, products.get(adapterPosition).getId());
                products.remove(adapterPosition);
                existingProducts.remove(adapterPosition);
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onDataChanged(existingProducts);
                }
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, products.size());
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
     * The method <code>createAndShowAlertDialogForUpdate</code> create and shows a dialog, which
     * need to update product.
     *
     * @param adapterPosition is the position, where record about product are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_update_product,
                products.get(adapterPosition).getName()));
        builder.setMessage(context.getString(R.string.ask_update_product_from_database));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(context);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(context.getString(R.string.enter_name));
        editTextName.setText(products.get(adapterPosition).getName());

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setText(String.valueOf(products.get(adapterPosition).getCost()));

        final EditText editTextQuantity = new EditText(context);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(R.string.enter_quantity);
        editTextQuantity.setText(String.valueOf(existingProducts.get(adapterPosition)
                .getQuantityOrWeight()));

        layout.addView(editTextName);
        layout.addView(editTextCost);
        layout.addView(editTextQuantity);

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
                    Product updatedProduct = products.get(adapterPosition);
                    updatedProduct.setName(editTextName.getText().toString());
                    updatedProduct.setCost(Double.valueOf(editTextCost.getText().toString()));
                    productDAO.update(updatedProduct);
                    products.set(adapterPosition, updatedProduct);

                    ExistingProduct updatedExistingProduct = existingProducts.get(adapterPosition);
                    updatedExistingProduct.setQuantityOrWeight(Double
                            .parseDouble(editTextQuantity.getText().toString()));
                    existingProductDAO.update(updatedExistingProduct);
                    existingProducts.set(adapterPosition, updatedExistingProduct);

                    if (mOnDataChangeListener != null) {
                        mOnDataChangeListener.onDataChanged(existingProducts);
                    }
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data, Toast.LENGTH_SHORT).show();
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

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {
        void onDataChanged(List<ExistingProduct> existingProducts);
    }

    /**
     * Class <code>InsideShoppingListViewHolder</code> is the View Holder for
     * {@link android.support.v7.widget.RecyclerView.Adapter}
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class InsideShoppingListViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewProduct;
        private CheckBox checkBoxIsPurchased;
        private TextView textViewProductName;
        private TextView textViewProductCost;
        private TextView textViewTotalCost;
        private TextView textViewQuantity;
        private ImageButton imageButtonDelete;

        InsideShoppingListViewHolder(View itemView) {
            super(itemView);
            cardViewProduct = (CardView)
                    itemView.findViewById(R.id.inside_shopping_list_card_view);
            checkBoxIsPurchased = (CheckBox) itemView.
                    findViewById(R.id.inside_shopping_list_check_box_in_card_view);
            textViewProductName = (TextView) itemView.
                    findViewById(R.id.inside_shopping_list_text_view_product_name_in_card_view);
            textViewProductCost = (TextView) itemView.
                    findViewById(R.id.inside_shopping_list_text_view_product_cost_in_card_view);
            textViewTotalCost = (TextView) itemView
                    .findViewById(R.id.inside_shopping_list_text_view_total_cost_in_card_view);
            textViewQuantity = (TextView) itemView.findViewById(
                    R.id.inside_shopping_list_edit_text_quantity_of_product_in_card_view);
            imageButtonDelete = (ImageButton) itemView
                    .findViewById(R.id.inside_shopping_list_image_button_delete_in_card_view);
        }
    }
}
