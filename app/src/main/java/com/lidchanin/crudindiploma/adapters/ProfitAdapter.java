package com.lidchanin.crudindiploma.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;

import java.text.DecimalFormat;

public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.ProfitViewHolder> {

    private static OnSumChangeListener onSumChangeListener;
    private int counter = 2;

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
    public void onBindViewHolder(final ProfitViewHolder holder, int position) {
        TextWatcher setSum = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.editTextWeight.getText().toString().trim().length() >= 1 && holder.editTextCost.getText().toString().trim().length() >= 1) {
                    double weight = Double.parseDouble(holder.editTextWeight.getText().toString());
                    double cost = Double.parseDouble(holder.editTextCost.getText().toString());
                    holder.textViewSum.setText(new DecimalFormat("#####.##").format(cost / weight));
                    onSumChangeListener.onSumChanged(holder.getAdapterPosition(), (cost / weight));
                }
            }

        };

        holder.editTextCost.addTextChangedListener(setSum);
        holder.editTextWeight.addTextChangedListener(setSum);
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return counter;
    }

    public void addNewItem() {
        counter++;
        notifyDataSetChanged();
    }

    public interface getOneProductById {
        void getOneProduct(String cost, String weight);
    }

    public interface OnSumChangeListener {
        void onSumChanged(int key, double sum);
    }

    static class ProfitViewHolder extends RecyclerView.ViewHolder {
        EditText editTextCost;
        EditText editTextWeight;
        TextView textViewSum;
        ImageButton buttonDelete;

        ProfitViewHolder(final View view) {
            super(view);
            editTextCost = (EditText) view.findViewById(R.id.cost);
            editTextWeight = (EditText) view.findViewById(R.id.weight);
            textViewSum = (TextView) view.findViewById(R.id.sum);
            buttonDelete = (ImageButton) view.findViewById(R.id.profit_delete);
        }
    }


}
