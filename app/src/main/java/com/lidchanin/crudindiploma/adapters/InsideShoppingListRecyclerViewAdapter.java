package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
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
    private ProductDAO productDAO;
    private ExistingProductDAO existingProductDAO;
    private Context context;
    private long shoppingListId;

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
        // TODO: 09.06.2017 Delete button "Accept"
        holder.imageButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingProduct.setQuantityOrWeight(Double
                        .valueOf(holder.editTextQuantity.getText().toString()));
                existingProduct
                        .setTotalCost(product.getCost() * existingProduct.getQuantityOrWeight());
                existingProductDAO.update(existingProduct);
                notifyDataSetChanged();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.ask_delete_product,
                products.get(adapterPosition)));
        builder.setMessage(context.getString(R.string.you_are_sure_you_want_to_delete_this_product));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    /**
     * The method <code>createAndShowAlertDialogForUpdate</code> create and shows a dialog, which
     * need to update product.
     *
     * @param adapterPosition is the position, where record about product are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.ask_update_product,
                products.get(adapterPosition).getName()));
        builder.setMessage(context.getString(R.string.ask_update_product_from_database));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextName = new EditText(context);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setHint(context.getString(R.string.enter_name));
        editTextName.setText(products.get(adapterPosition).getName());
        layout.addView(editTextName);

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setText(String.valueOf(products.get(adapterPosition).getCost()));
        layout.addView(editTextCost);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextName.getText() != null
                        && editTextName.getText().toString().length() != 0
                        && editTextCost.getText() != null
                        && editTextCost.getText().toString().length() != 0) {
                    Product product = products.get(adapterPosition);
                    product.setName(editTextName.getText().toString());
                    product.setCost(Double.valueOf(editTextCost.getText().toString()));
                    productDAO.update(product);
                    products.set(adapterPosition, product);
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    // FIXME: 09.06.2017 alert dialog for update
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
            // FIXME: 09.06.2017 filter's work is not correct
            editTextQuantity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});
            imageButtonAccept = (ImageButton) itemView
                    .findViewById(R.id.inside_shopping_list_image_button_accept_in_card_view);
            imageButtonDelete = (ImageButton) itemView
                    .findViewById(R.id.inside_shopping_list_image_button_delete_in_card_view);
        }
    }
}
