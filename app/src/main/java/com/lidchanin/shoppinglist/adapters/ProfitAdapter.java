package com.lidchanin.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidchanin.shoppinglist.R;
import com.lidchanin.shoppinglist.models.ProfitItems;

import java.util.ArrayList;
import java.util.List;

public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.ProfitViewHolder> {

    private static OnSumChangeListener onSumChangeListener;
    private List<ProfitItems> profitItemsList;

    public ProfitAdapter (){
        profitItemsList = new ArrayList<>();
        profitItemsList.add(new ProfitItems());
        profitItemsList.add(new ProfitItems());
    }

    public ProfitAdapter (List<ProfitItems> profitItemsList){
        this.profitItemsList = profitItemsList;
    }

    public void setOnSumChangeListener(OnSumChangeListener vOnSumChangeListener) {
        onSumChangeListener = vOnSumChangeListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProfitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item_inside_profit, parent, false);
        return new ProfitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProfitViewHolder holder, final int position) {
       /* Log.d("POSITIONS", "onBindViewHolder: " + holder.getAdapterPosition());
        if(profitItemsList.get(holder.getAdapterPosition()).isCustomized()){
            holder.backgroundProfit.setBackgroundColor(Color.BLACK);
        }else {
            holder.backgroundProfit.setBackgroundColor(Color.WHITE);
        }
        holder.editTextWeight.setText(String.valueOf(profitItemsList.get(holder.getAdapterPosition()).getWeiht()));
        holder.editTextCost.setText(String.valueOf(profitItemsList.get(holder.getAdapterPosition()).getCost()));
        holder.textViewSum.setText(String.valueOf(profitItemsList.get(holder.getAdapterPosition()).getCost()));
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profitItemsList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });*/
        holder.editTextCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ProfitItems profitItem = profitItemsList.get(holder.getAdapterPosition());
                profitItem.setCost(Double.parseDouble(String.valueOf(s)));
                calculate(holder,profitItem);
            }
        });
        holder.editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ProfitItems profitItem = profitItemsList.get(holder.getAdapterPosition());
                profitItem.setWeiht(Double.parseDouble(String.valueOf(s)));
                calculate(holder,profitItem);
            }
        });
    }

    @Override
    public int getItemCount() {

        Log.d("NEWSIZE", "counter: " + profitItemsList.size());
        return profitItemsList.size();
    }

    private void calculate(ProfitViewHolder holder, ProfitItems profitItem) {
        double weight = Double.parseDouble(holder.editTextWeight.getText().toString());
        double cost = Double.parseDouble(holder.editTextCost.getText().toString());
        //holder.textViewSum.setText(new DecimalFormat("#####.##").format(cost / weight));
        profitItem.setSum(cost / weight);
        profitItemsList.set(holder.getAdapterPosition(), profitItem);
        if (cost / weight > 0) {
            onSumChangeListener.onSumChanged(holder.getAdapterPosition(), profitItemsList);
        }
    }

    public void addNewItem() {
        profitItemsList.add(new ProfitItems());
        notifyItemInserted(profitItemsList.size());
        Log.d("NEWSIZE", "addNewItem: " + profitItemsList.size());
    }

    public interface OnSumChangeListener {
        void onSumChanged(int key, List<ProfitItems> profitTtem);
    }

    static class ProfitViewHolder extends RecyclerView.ViewHolder {
        EditText editTextCost;
        EditText editTextWeight;
        ImageView betterChoice;
        ImageView redCross;

        ProfitViewHolder(final View view) {
            super(view);
            betterChoice = (ImageView) view.findViewById(R.id.best_choice);
            redCross = (ImageView) view.findViewById(R.id.red_cross);
            editTextCost = (EditText) view.findViewById(R.id.cost);
            editTextWeight = (EditText) view.findViewById(R.id.weight);

        }
    }

}
