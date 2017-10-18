package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
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
import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.UsedProduct;
import com.lidchanin.crudindiploma.database.dao.ProductDao;
import com.lidchanin.crudindiploma.database.dao.UsedProductDao;

import java.util.List;

/**
 * Class <code>{@link ManagingProductsRVAdapter}</code> is an adapter
 * for {@link RecyclerView}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class ManagingProductsRVAdapter extends RecyclerView
        .Adapter<ManagingProductsRVAdapter.ManagingProductsRVViewHolder> {

    private Context context;

    private ProductDao productDao;
    private UsedProductDao usedProductDao;

    private List<Product> products;

    /**
     * Constructor.
     *
     * @param products       {@link List} which contains all {@link Product}s from the database.
     * @param productDao     {@link ProductDao} exemplar.
     * @param usedProductDao {@link UsedProductDao} exemplar.
     * @param context        {@link Context} exemplar.
     */
    public ManagingProductsRVAdapter(List<Product> products,
                                     ProductDao productDao,
                                     UsedProductDao usedProductDao,
                                     Context context) {
        this.products = products;
        this.productDao = productDao;
        this.usedProductDao = usedProductDao;
        this.context = context;
    }

    @Override
    public ManagingProductsRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_managing_products, parent, false);
        return new ManagingProductsRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ManagingProductsRVViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final Product product = products.get(holder.getAdapterPosition());

        holder.textViewProductName.setText(product.getName());
        holder.textViewProductCost.setText(String.valueOf(product.getCost()));
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(product, adapterPosition);
            }
        });
        holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(product, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * The method <code>createAndShowAlertDialogForDelete</code> creates and displays a
     * {@link AlertDialog} for {@link Product} deletes.
     *
     * @param product         {@link Product} which need to delete.
     * @param adapterPosition position, where record about {@link Product} are located in
     *                        {@link RecyclerView}.
     */
    private void createAndShowAlertDialogForDelete(final Product product,
                                                   final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_delete_product,
                products.get(adapterPosition).getName()));
        builder.setMessage(R.string.ask_delete_product_from_database);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productDao.delete(product);
                List<UsedProduct> usedProducts =
                        usedProductDao.queryBuilder().where(UsedProductDao.Properties.
                                ProductId.eq(product.getId())).list();
                usedProductDao.deleteInTx(usedProducts);
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

    /**
     * The method <code>createAndShowAlertDialogForUpdate</code> creates and displays a
     * {@link AlertDialog} for {@link Product} updates.
     *
     * @param product         {@link Product} which need to update.
     * @param adapterPosition position, where record about {@link Product} are located in
     *                        {@link RecyclerView}.
     */
    private void createAndShowAlertDialogForUpdate(final Product product,
                                                   final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.ask_update_product, product.getName()));
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

        layout.addView(textInputLayoutName);
        layout.addView(textInputLayoutCost);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextName.getText() != null
                        && editTextName.getText().toString().length() != 0
                        && editTextCost.getText() != null
                        && editTextCost.getText().toString().length() != 0) {
                    product.setName(editTextName.getText().toString());
                    product.setCost(Double.valueOf(editTextCost.getText().toString()));
                    productDao.update(product);
                    products.set(adapterPosition, product);
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data, Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForUpdate(product, adapterPosition);
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
     * Class <code>ManagingProductsRVViewHolder</code> is the View Holder for
     * {@link android.support.v7.widget.RecyclerView.Adapter}
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class ManagingProductsRVViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewProduct;
        private TextView textViewProductName;
        private TextView textViewProductCost;
        private ImageButton imageButtonDelete;

        ManagingProductsRVViewHolder(View itemView) {
            super(itemView);
            cardViewProduct = (CardView) itemView.findViewById(R.id.cv_in_managing_products);
            textViewProductName = (TextView) itemView.findViewById(R.id.product_name);
            textViewProductCost = (TextView) itemView.findViewById(R.id.product_cost);
            imageButtonDelete = (ImageButton) itemView.findViewById(R.id.product_delete);
        }
    }
}
