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

    public ManagingExistingProductsRecyclerViewAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public ManagingExistingProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item_managing_existing_products, parent, false);
        return new ManagingExistingProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManagingExistingProductsViewHolder holder, int position) {
        final Product product = products.get(holder.getAdapterPosition());
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductCost.setText(String.valueOf(product.getCost()));
        holder.textViewProductPopularity.setText(String.valueOf(product.getPopularity()));
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 08.06.2017 code to delete button
            }
        });
        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 08.06.2017 code to edit button
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
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
