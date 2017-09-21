package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.dao.ExistingProductDAO;
import com.lidchanin.crudindiploma.data.dao.ShoppingListDAO;
import com.lidchanin.crudindiploma.models.ShoppingList;
import com.lidchanin.crudindiploma.fragments.InsideShoppingListFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class <code>MainScreenRecyclerViewAdapter</code> is an adapter for {@link RecyclerView} from
 * {@link com.lidchanin.crudindiploma.fragments.ShoppingListFragment}. This class extends
 * {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class MainScreenRecyclerViewAdapter
        extends RecyclerView.Adapter<MainScreenRecyclerViewAdapter.MainScreenViewHolder> {

    private Context context;
    private List<ShoppingList> shoppingLists;

    private ShoppingListDAO shoppingListDAO;
    private ExistingProductDAO existingProductDAO;

    private short progressToProgressBar;
    private short maxToProgressBar;

    public MainScreenRecyclerViewAdapter(List<ShoppingList> shoppingLists,
                                         ShoppingListDAO shoppingListDAO,
                                         ExistingProductDAO existingProductDAO, Context context) {
        this.shoppingLists = shoppingLists;
        this.shoppingListDAO = shoppingListDAO;
        this.existingProductDAO = existingProductDAO;
        this.context = context;
    }

    @Override
    public MainScreenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item_shopping_list, parent, false);
        return new MainScreenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainScreenViewHolder holder, final int position) {
        holder.textViewShoppingListName.setText(shoppingLists.get(position).getName());
        holder.textViewDateOfCreation.setText(dateConverter(shoppingLists.get(position)
                .getDateOfCreation()));
        String numberOfProducts = existingProductDAO.getNumberOfPurchasedProducts(shoppingLists
                .get(holder.getAdapterPosition()).getId())
                + "/" + existingProductDAO.getNumberOfAllProducts(shoppingLists
                .get(holder.getAdapterPosition()).getId());
        holder.textViewNumberOfProducts.setText(numberOfProducts);
        holder.cardViewShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16.07.2017 try to remake this
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                InsideShoppingListFragment fragment=new InsideShoppingListFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.Bundles.SHOPPING_LIST_ID, shoppingLists.get(holder.getAdapterPosition()).getId());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.commit();
            }
        });
        holder.cardViewShoppingList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createAndShowAlertDialogForUpdate(holder.getAdapterPosition());
                return true;
            }
        });
        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(holder.getAdapterPosition());
            }
        });
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(holder.getAdapterPosition());
            }
        });

        // FIXME: 20.06.2017 theme for progress bar
        Drawable drawable = context.getResources().getDrawable(R.drawable.main_screen_progress_bar);
        holder.progressBar.setProgressDrawable(drawable);
        progressToProgressBar = existingProductDAO.getNumberOfPurchasedProducts(
                shoppingLists.get(holder.getAdapterPosition()).getId());
        maxToProgressBar = existingProductDAO.getNumberOfAllProducts(
                shoppingLists.get(holder.getAdapterPosition()).getId());
        holder.progressBar.setProgress(progressToProgressBar);
        holder.progressBar.setMax(maxToProgressBar);
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    /**
     * The method <code>createAndShowAlertDialogForDelete</code> create and shows a dialog, which
     * need to confirm deleting shopping list.
     *
     * @param adapterPosition is the position, where record about shopping list are located.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.delete_shopping_list,
                shoppingLists.get(adapterPosition).getName()));
        builder.setMessage(context
                .getString(R.string.are_you_sure_you_want_to_delete_this_shopping_list));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingListDAO.delete(shoppingLists.get(adapterPosition));
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
     * The method <code>createAndShowAlertDialogForUpdate</code> create and shows a dialog for
     * update shopping list.
     *
     * @param adapterPosition is the position, where record about shopping list are located.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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
                    ShoppingList shoppingList = shoppingLists.get(adapterPosition);
                    shoppingList.setName(editTextName.getText().toString());
                    shoppingListDAO.update(shoppingList);
                    shoppingLists.set(adapterPosition, shoppingList);
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    // FIXME: 09.06.2017 alert dialog for update
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
     * The method <code>dateConverter</code> converts date to needed format.
     *
     * @param previousDate is the date before converting.
     * @return date after converting.
     */
    private String dateConverter(String previousDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context
                .getString(R.string.database_date_format), Locale.getDefault());
        Date finalDate = null;
        try {
            finalDate = simpleDateFormat.parse(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat(context.getString(R.string.needed_date_format),
                Locale.getDefault());
        return simpleDateFormat.format(finalDate);
    }

    /**
     * The class <code>MainScreenViewHolder</code> is the View Holder for
     * {@link MainScreenRecyclerViewAdapter}.
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class MainScreenViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewShoppingList;
        private TextView textViewShoppingListName;
        private TextView textViewDateOfCreation;
        private TextView textViewNumberOfProducts;
        private ImageButton imageButtonDelete;
        private ImageButton imageButtonEdit;
        private ProgressBar progressBar;

        MainScreenViewHolder(View itemView) {
            super(itemView);
            cardViewShoppingList = (CardView)
                    itemView.findViewById(R.id.main_screen_card_view_shopping_list);
            textViewShoppingListName = (TextView) itemView
                    .findViewById(R.id.main_screen_text_view_name_shopping_list_in_card_view);
            textViewDateOfCreation = (TextView)
                    itemView.findViewById(R.id.main_screen_text_view_date_of_creation_in_card_view);
            textViewNumberOfProducts = (TextView)
                    itemView.findViewById(R.id.main_screen_text_view_products_in_card_view);
            imageButtonDelete = (ImageButton)
                    itemView.findViewById(R.id.main_screen_image_button_delete_in_card_view);
            imageButtonEdit = (ImageButton) itemView.findViewById(R.id.edit_list_name);
            progressBar = (ProgressBar)
                    itemView.findViewById(R.id.main_screen_progress_bar_in_card_view);
        }
    }
}
