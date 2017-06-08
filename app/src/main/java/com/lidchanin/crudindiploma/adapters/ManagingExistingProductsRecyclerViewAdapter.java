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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ProductDAO;
import com.lidchanin.crudindiploma.data.models.Product;

import java.util.List;

/**
 * Class <code>{@link ManagingExistingProductsRecyclerViewAdapter}</code> is an adapter
 * for {@link RecyclerView}
 * from {@link com.lidchanin.crudindiploma.activities.ManagingExistingProductsActivity}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class ManagingExistingProductsRecyclerViewAdapter extends RecyclerView
        .Adapter<ManagingExistingProductsRecyclerViewAdapter.ManagingExistingProductsViewHolder> {

    private List<Product> products;

    private Context context;

    private ProductDAO productDAO;

    public ManagingExistingProductsRecyclerViewAdapter(List<Product> products,
                                                       ProductDAO productDAO, Context context) {
        this.products = products;
        this.productDAO = productDAO;
        this.context = context;
    }

    @Override
    public ManagingExistingProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item_managing_existing_products, parent, false);
        return new ManagingExistingProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ManagingExistingProductsViewHolder holder, int position) {
        final Product product = products.get(holder.getAdapterPosition());
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductCost.setText(String.valueOf(product.getCost()));
        holder.textViewProductPopularity.setText(String.valueOf(product.getPopularity()));
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(holder.getAdapterPosition());
            }
        });
        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
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
     * The method <code>createAndShowAlertDialogForDelete</code> create and shows a dialog, which
     * need to confirm deleting shopping list.
     *
     * @param adapterPosition is the position, where record about shopping list are located.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.ask_delete_product,
                products.get(adapterPosition).getName()));
        builder.setMessage(R.string.ask_delete_product_from_database);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productDAO.deleteFromDatabase(products.get(adapterPosition).getId());
                products.remove(adapterPosition);
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

    // FIXME: 08.06.2017 update existing product

    /**
     * The method <code>createAndShowAlertDialogForUpdate</code> create and shows a dialog, which
     * need to confirm deleting shopping list.
     *
     * @param adapterPosition is the position, where record about shopping list are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.ask_update_product,
                products.get(adapterPosition).getName()));
        // FIXME: 08.06.2017 message string resource
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
                String name = editTextName.getText().toString();
                Double cost = Double.valueOf(editTextCost.getText().toString());
                if (name.length() == 0) {
                    Toast.makeText(context, context.getString(R.string.enter_name) + "!",
                            Toast.LENGTH_SHORT).show();
                } else if (cost <= 0) {
                    Toast.makeText(context, context.getString(R.string.enter_cost) + "!",
                            Toast.LENGTH_SHORT).show();
                } else if (name.length() == 0 && cost <= 0) {
                    Toast.makeText(context, context.getString(R.string.enter_name_and_cost) + "!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Product product = products.get(adapterPosition);
                    product.setName(name);
                    product.setCost(cost);
                    productDAO.update(product);
                    products.set(adapterPosition, product);
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
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
     * Class <code>ManagingExistingProductsViewHolder</code> is the View Holder for
     * {@link android.support.v7.widget.RecyclerView.Adapter}
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class ManagingExistingProductsViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewProduct;
        private TextView textViewProductName;
        private TextView textViewProductCost;
        private TextView textViewProductPopularity;
        private ImageButton imageButtonDelete;
        private ImageButton imageButtonEdit;

        ManagingExistingProductsViewHolder(View itemView) {
            super(itemView);
            cardViewProduct = (CardView) itemView
                    .findViewById(R.id.managing_existing_products_card_view);
            textViewProductName = (TextView) itemView.findViewById(
                    R.id.managing_existing_products_text_view_product_name_in_card_view
            );
            textViewProductCost = (TextView) itemView.findViewById(
                    R.id.managing_existing_products_text_view_product_cost_in_card_view
            );
            textViewProductPopularity = (TextView) itemView.findViewById(
                    R.id.managing_existing_products_text_view_product_popularity_in_card_view
            );
            imageButtonDelete = (ImageButton) itemView.findViewById(
                    R.id.managing_existing_products_image_button_delete_in_card_view
            );
            imageButtonEdit = (ImageButton) itemView.findViewById(
                    R.id.managing_existing_products_image_button_edit_in_card_view
            );
        }
    }
}
