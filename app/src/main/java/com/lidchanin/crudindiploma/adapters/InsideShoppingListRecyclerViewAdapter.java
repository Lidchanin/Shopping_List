package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.activities.InsideShoppingListUpdateProductPopUpWindowActivity;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;
import com.lidchanin.crudindiploma.utils.filters.DecimalDigitsInputFilter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Class <code>InsideShoppingListRecyclerViewAdapter</code> is an adapter for {@link RecyclerView}
 * from {@link com.lidchanin.crudindiploma.activities.InsideShoppingListActivity}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class InsideShoppingListRecyclerViewAdapter extends RecyclerView
        .Adapter<InsideShoppingListRecyclerViewAdapter.InsideShoppingListViewHolder> {

    private List<Product> products;
    private List<ExistingProduct> existingProducts;
    private long shoppingListId;
    private Context context;
    private ExistingProductDAO existingProductDAO;

    public InsideShoppingListRecyclerViewAdapter(List<Product> products, List<ExistingProduct>
            existingProducts, Context context, long shoppingListId) {
        this.products = products;
        this.existingProducts = existingProducts;
        this.context = context;
        this.shoppingListId = shoppingListId;
        existingProductDAO = new ExistingProductDAO(context);
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
        if (existingProduct.getTotalCost() == 0.0) {
            existingProduct.setTotalCost(product.getCost());
        }
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductCost.setText(new DecimalFormat("#0.00").format(product.getCost()));
        holder.textViewTotalCost.setText(new DecimalFormat("#0.00")
                .format(existingProduct.getTotalCost()));
        holder.editTextQuantity.setText(String.valueOf(existingProduct.getQuantityOrWeight()));
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialog(holder.getAdapterPosition());
            }
        });
        holder.cardViewProduct.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context,
                        InsideShoppingListUpdateProductPopUpWindowActivity.class);
                intent.putExtra("shoppingListId", shoppingListId);
                intent.putExtra("productId", product.getId());
                context.startActivity(intent);
                return true;
            }
        });
        holder.imageButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingProduct.setQuantityOrWeight(Double
                        .valueOf(holder.editTextQuantity.getText().toString()));
                existingProduct
                        .setTotalCost(product.getCost() * existingProduct.getQuantityOrWeight());
                existingProductDAO.update(existingProduct);
                notifyDataSetChanged();
                // TODO: 01.06.2017 check this part
                if (onDataChangeListener != null) {
                    onDataChangeListener.onDataChanged(existingProducts.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Method <code>createAndShowAlertDialog</code> create and shows a dialog, which need to
     * confirm deleting product.
     *
     * @param adapterPosition is the position, where record about product are located.
     */
    private void createAndShowAlertDialog(final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_product);
        builder.setMessage(R.string.you_are_sure_you_want_to_delete_this_product);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProductDAO productDAO = new ProductDAO(context);
                productDAO.delete(shoppingListId, products.get(adapterPosition).getId());
                products.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, products.size());
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

    private OnDataChangeListener onDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    /**
     * Class <code>InsideShoppingListViewHolder</code> is the View Holder for
     * {@link android.support.v7.widget.RecyclerView.Adapter}
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class InsideShoppingListViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewProduct;
        private TextView textViewProductName;
        private TextView textViewProductCost;
        private TextView textViewTotalCost;
        private EditText editTextQuantity;
        private ImageButton imageButtonAccept;
        private ImageButton imageButtonDelete;

        InsideShoppingListViewHolder(View itemView) {
            super(itemView);
            cardViewProduct = (CardView)
                    itemView.findViewById(R.id.inside_shopping_list_card_view);
            textViewProductName = (TextView) itemView.
                    findViewById(R.id.inside_shopping_list_text_view_product_name_in_card_view);
            textViewProductCost = (TextView) itemView.
                    findViewById(R.id.inside_shopping_list_text_view_product_cost_in_card_view);
            textViewTotalCost = (TextView) itemView
                    .findViewById(R.id.inside_shopping_list_text_view_total_cost_in_card_view);
            editTextQuantity = (EditText) itemView.findViewById(
                    R.id.inside_shopping_list_edit_text_quantity_of_product_in_card_view);
            editTextQuantity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
            imageButtonAccept = (ImageButton) itemView
                    .findViewById(R.id.inside_shopping_list_image_button_accept_in_card_view);
            imageButtonDelete = (ImageButton) itemView
                    .findViewById(R.id.inside_shopping_list_image_button_delete_in_card_view);
        }
    }

    public interface OnDataChangeListener {
        public void onDataChanged(int size);
    }
}
