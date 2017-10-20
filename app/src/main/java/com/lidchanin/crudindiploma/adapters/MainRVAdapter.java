package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.UsedProduct;
import com.lidchanin.crudindiploma.database.dao.ProductDao;
import com.lidchanin.crudindiploma.database.dao.ShoppingListDao;
import com.lidchanin.crudindiploma.database.dao.StatisticDao;
import com.lidchanin.crudindiploma.database.dao.UsedProductDao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.lidchanin.crudindiploma.utils.DatabaseUtils.calculationOfEstimatedAmount;
import static com.lidchanin.crudindiploma.utils.DatabaseUtils.isUsedProductExists;

/**
 * Class {@link MainRVAdapter} provide a binding from an app-specific data set to views that are
 * displayed within a {@link RecyclerView}.
 * Class extends {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.MainViewHolder> {

    private static final String TAG = "MainRVAdapter";

    private Context context;

    private ShoppingListDao shoppingListDao;
    private ProductDao productDao;
    private UsedProductDao usedProductDao;
    private StatisticDao statisticDao;

    private List<ShoppingList> shoppingLists;

    private ChildRVAdapter childRVAdapter;

    /**
     * Constructor.
     *
     * @param context         {@link Context}.
     * @param shoppingLists   {@link List} with all {@link ShoppingList}s.
     * @param shoppingListDao {@link ShoppingListDao} exemplar.
     * @param productDao      {@link ProductDao} exemplar.
     * @param statisticDao    {@link StatisticDao} exemplar.
     * @param usedProductDao  {@link UsedProductDao} exemplar.
     */
    public MainRVAdapter(Context context,
                         ShoppingListDao shoppingListDao,
                         ProductDao productDao,
                         UsedProductDao usedProductDao,
                         StatisticDao statisticDao,
                         List<ShoppingList> shoppingLists) {
        this.context = context;

        this.shoppingListDao = shoppingListDao;
        this.productDao = productDao;
        this.usedProductDao = usedProductDao;
        this.statisticDao = statisticDao;

        this.shoppingLists = shoppingLists;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_main_rv, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final ShoppingList shoppingList = shoppingLists.get(adapterPosition);

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                holder.rvChild.setLayoutManager(layoutManager);

                childRVAdapter = new ChildRVAdapter(context, shoppingListDao, productDao,
                        usedProductDao, shoppingLists, adapterPosition);
                childRVAdapter.setOnDataChangeListener(new ChildRVAdapter
                        .OnDataChangeListener() {
                    @Override
                    public void onDataChanged(List<ShoppingList> shoppingLists) {
                        holder.tvEstimatedSum
                                .setText(context.getString(R.string.estimated_amount,
                                        new DecimalFormat("#.##").format(
                                                calculationOfEstimatedAmount(shoppingList
                                                        .getUsedProducts()))));
                    }
                });
                holder.rvChild.setAdapter(childRVAdapter);

                holder.tvEstimatedSum.setText(context.getString(R.string.estimated_amount,
                        new DecimalFormat("#.##").format(calculationOfEstimatedAmount(
                                shoppingList.getUsedProducts()))));

                if (holder.rvChild.getVisibility() == View.GONE) {
                    holder.rvChild.setVisibility(View.VISIBLE);
                    holder.buttonChildAdd.setVisibility(View.VISIBLE);
                    holder.tvEstimatedSum.setVisibility(View.VISIBLE);
                } else {
                    holder.rvChild.setVisibility(View.GONE);
                    holder.buttonChildAdd.setVisibility(View.GONE);
                    holder.tvEstimatedSum.setVisibility(View.GONE);
                }
            }
        });

        holder.tvName.setText(shoppingList.getName());

        holder.buttonChildAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForManualType(adapterPosition, holder);
            }
        });

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForUpdate(adapterPosition, shoppingList);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialogForDelete(adapterPosition, shoppingList);
            }
        });
    }

    /**
     * Method <b>createAndShowAlertDialogForManualType</b> creates and displays {@link AlertDialog}
     * for {@link ShoppingList} manual type.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} will located.
     * @param holder          current {@link MainViewHolder}.
     */
    private void createAndShowAlertDialogForManualType(final int adapterPosition,
                                                       final MainViewHolder holder) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(R.string.add_new_product);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editTextCost = new EditText(context);
        editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCost.setHint(context.getString(R.string.enter_cost));
        editTextCost.setText("0");
        editTextCost.setSelectAllOnFocus(true);

        final TextInputLayout textInputLayoutCost = new TextInputLayout(context);
        textInputLayoutCost.addView(editTextCost);

        final AutoCompleteTextView autoCompleteTextViewName = new AutoCompleteTextView(context);
        autoCompleteTextViewName.setInputType(InputType.TYPE_CLASS_TEXT);
        autoCompleteTextViewName.setHint(context.getString(R.string.enter_name));
        autoCompleteTextViewName.setHintTextColor(Color.BLACK);
        autoCompleteTextViewName.setTextColor(Color.BLACK);
        final List<Product> allProductsInDB = productDao.loadAll();
        AutoCompleteProductNamesAndCostsAdapter autoCompleteAdapter
                = new AutoCompleteProductNamesAndCostsAdapter(context, allProductsInDB);
        autoCompleteTextViewName.setAdapter(autoCompleteAdapter);
        autoCompleteTextViewName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selected = (Product) parent.getAdapter().getItem(position);
                autoCompleteTextViewName.setText(selected.getName());
                editTextCost.setText(String.valueOf(selected.getCost()));
            }
        });

        final TextInputLayout textInputLayoutAuto = new TextInputLayout(context);
        textInputLayoutAuto.addView(autoCompleteTextViewName);

        final EditText editTextQuantity = new EditText(context);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextQuantity.setHint(context.getString(R.string.enter_quantity));
        editTextQuantity.setHintTextColor(Color.BLACK);
        editTextQuantity.setText("1");
        editTextQuantity.setSelectAllOnFocus(true);

        final TextInputLayout textInputLayoutQuantity = new TextInputLayout(context);
        textInputLayoutQuantity.addView(editTextQuantity);

        final RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        final RadioButton radioButtonKg = new RadioButton(context);
        radioButtonKg.setText(context.getString(R.string.kg));

        final RadioButton radioButtonUnit = new RadioButton(context);
        radioButtonUnit.setText(context.getString(R.string.pieces));

        radioGroup.addView(radioButtonKg);
        radioGroup.addView(radioButtonUnit);
        radioGroup.check(radioButtonKg.getId());

        layout.addView(textInputLayoutAuto);
        layout.addView(textInputLayoutCost);
        layout.addView(textInputLayoutQuantity);
        layout.addView(radioGroup);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredName = autoCompleteTextViewName.getText().toString();
                double enteredCost = Double.valueOf(editTextCost.getText().toString());
                double enteredQuantity = Double.valueOf(editTextQuantity.getText().toString());
                boolean enteredUnit = radioButtonKg.isChecked();

                if (enteredName.length() > 0 && enteredCost >= 0 && enteredQuantity >= 0) {
                    Product newProduct = new Product();
                    newProduct.setName(enteredName);
                    newProduct.setCost(enteredCost);
                    try {
                        newProduct.setId(productDao.insert(newProduct));
                        Log.i(TAG, "Product \"" + newProduct.getName() + "\" not exists in the db.");
                        Log.i(TAG, "Product \"" + newProduct.getName() + "\" has been created.");
                    } catch (SQLiteConstraintException e) {
                        Log.e(TAG, "SQLiteConstraintException: " + e.getMessage());
                        Log.i(TAG, "Product \"" + newProduct.getName() + "\" exists in the db.");
                        newProduct.setId(productDao.queryBuilder()
                                .where(ProductDao.Properties.Name.eq(newProduct.getName()))
                                .unique().getId());
                        productDao.update(newProduct);
                        Log.i(TAG, "Product \"" + newProduct.getName() + "\" has been updated.");
                    } finally {
                        UsedProduct newUsedProduct = new UsedProduct();
                        newUsedProduct.setQuantity(enteredQuantity);
                        newUsedProduct.setUnit(enteredUnit);
                        newUsedProduct.setIsPurchased(false);
                        newUsedProduct.setDate(System.currentTimeMillis());
                        newUsedProduct.setProduct(newProduct);
                        newUsedProduct.setShoppingListId(shoppingLists.get(adapterPosition).getId());
                        newUsedProduct.setProductId(newProduct.getId());

                        boolean usedProductExistence = isUsedProductExists(
                                shoppingLists.get(adapterPosition).getUsedProducts(),
                                newUsedProduct.getShoppingListId(),
                                newUsedProduct.getProductId());
                        if (usedProductExistence) {
                            Log.i(TAG, "UsedProduct exists in the list.");
                            newUsedProduct.setId(
                                    usedProductDao.queryBuilder()
                                            .where(UsedProductDao.Properties.ShoppingListId
                                                            .eq(newUsedProduct.getShoppingListId()),
                                                    UsedProductDao.Properties.ProductId
                                                            .eq(newUsedProduct.getProductId()))
                                            .unique().getId()
                            );
                            usedProductDao.update(newUsedProduct);
                            Log.i(TAG, "UsedProduct has been updated.");

                            for (UsedProduct up :
                                    shoppingLists.get(adapterPosition).getUsedProducts()) {
                                if (up.getProduct().getName().contains(newProduct.getName())) {
                                    int position = shoppingLists.get(adapterPosition)
                                            .getUsedProducts().indexOf(up);
                                    shoppingLists.get(adapterPosition).getUsedProducts()
                                            .set(position, newUsedProduct);
                                    childRVAdapter.notifyItemChanged(position);
                                    break;
                                }
                            }
                        } else {
                            Log.i(TAG, "UsedProduct not exists in the list.");
                            newUsedProduct.setId(usedProductDao.insert(newUsedProduct));
                            Log.i(TAG, "UsedProduct has been created.");
                            shoppingLists.get(adapterPosition).getUsedProducts()
                                    .add(newUsedProduct);
                            childRVAdapter.notifyItemInserted(shoppingLists.get(adapterPosition)
                                    .getUsedProducts().size());
                        }
                        holder.tvEstimatedSum
                                .setText(context.getString(R.string.estimated_amount,
                                        new DecimalFormat("#.##").format(
                                                calculationOfEstimatedAmount(shoppingLists
                                                        .get(adapterPosition)
                                                        .getUsedProducts()))));
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, R.string.please_enter_all_data,
                            Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForManualType(adapterPosition, holder);
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
     * The method <b>createAndShowAlertDialogForDelete</b> creates and shows a {@link AlertDialog},
     * for {@link ShoppingList} deleting.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} are located.
     * @param shoppingList    required {@link ShoppingList} for deleting.
     */
    private void createAndShowAlertDialogForDelete(final int adapterPosition,
                                                   final ShoppingList shoppingList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.delete_shopping_list, shoppingList.getName()));
        builder.setMessage(context.getString(R.string.are_you_sure_you_want_to_delete_this_shopping_list));
        builder.setNeutralButton("Yes without statistic", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteShoppingList(adapterPosition, shoppingList);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Yes with statistic", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final List<UsedProduct> usedProducts = usedProductDao.queryBuilder()
                        .where(UsedProductDao.Properties.ShoppingListId.eq(shoppingList.getId()),
                                UsedProductDao.Properties.IsPurchased.eq(1)).list();
                final List<Statistic> statistics = new ArrayList<>();
                for (UsedProduct up : usedProducts) {
                    Statistic statistic = new Statistic();
                    statistic.setName(up.getProduct().getName());
                    statistic.setCost(up.getProduct().getCost());
                    statistic.setQuantity(up.getQuantity());
                    statistic.setUnit(up.getUnit());
                    statistic.setDate(up.getDate());
                    statistics.add(statistic);
                }
                statisticDao.insertInTx(statistics);
                deleteShoppingList(adapterPosition, shoppingList);
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
     * The method <b>deleteShoppingList</b> deletes {@link ShoppingList} from the database and
     * update {@link List}.
     *
     * @param adapterPosition the the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} are located.
     * @param shoppingList    required {@link ShoppingList} for deleting.
     */
    private void deleteShoppingList(final int adapterPosition,
                                    final ShoppingList shoppingList) {
        shoppingListDao.delete(shoppingList);
        usedProductDao.deleteInTx(shoppingList.getUsedProducts());
        shoppingLists.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, shoppingLists.size());
    }

    /**
     * The method <b>createAndShowAlertDialogForUpdate</b> creates and shows a {@link AlertDialog}
     * for {@link ShoppingList} updating.
     *
     * @param adapterPosition the {@link RecyclerView} item position, where record about
     *                        {@link ShoppingList} are located.
     * @param shoppingList    required {@link ShoppingList} for updating.
     */
    private void createAndShowAlertDialogForUpdate(final int adapterPosition,
                                                   final ShoppingList shoppingList) {
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
                    shoppingList.setName(editTextName.getText().toString());
                    shoppingLists.set(adapterPosition, shoppingList);
                    shoppingListDao.update(shoppingLists.get(adapterPosition));
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, R.string.please_enter_name, Toast.LENGTH_SHORT).show();
                    createAndShowAlertDialogForUpdate(adapterPosition, shoppingList);
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

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    /**
     * Static class {@link MainViewHolder} describes an item view and metadata about its place
     * within the {@link RecyclerView}.
     * Class extends {@link ViewHolder}.
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class MainViewHolder extends ViewHolder {

        private CardView cvMain;
        private TextView tvName;
        private RecyclerView rvChild;
        private Button buttonChildAdd;
        private ImageButton buttonEdit;
        private ImageButton buttonDelete;
        private TextView tvEstimatedSum;

        /**
         * Constructor.
         *
         * @param itemView - item in {@link RecyclerView}.
         */
        MainViewHolder(View itemView) {
            super(itemView);
            cvMain = (CardView) itemView.findViewById(R.id.cv_in_main_rv);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_in_main_rv);
            rvChild = (RecyclerView) itemView.findViewById(R.id.main_screen_child_rv);
            buttonChildAdd = (Button) itemView.findViewById(R.id.main_screen_child_add_button);
            buttonEdit = (ImageButton) itemView.findViewById(R.id.button_edit_in_main_rv);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete_in_main_rv);
            tvEstimatedSum = (TextView) itemView.findViewById(R.id.tv_estimated_sum);
        }
    }
}
