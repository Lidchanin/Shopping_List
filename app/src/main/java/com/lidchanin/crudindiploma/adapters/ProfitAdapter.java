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

public class ProfitAdapter extends RecyclerView.Adapter {
    private int counter=2;
    @Override
    public ProfitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inside_profit_recyclerview,parent,false);
        ProfitAdapter.ProfitViewHolder profitViewHolder= new ProfitViewHolder(view);
        return profitViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return counter;
    }

    private static class ProfitViewHolder extends RecyclerView.ViewHolder {
        EditText editTextCost;
        EditText editTextWeight;
        EditText editTextQuantity;
        TextView textViewSum;
        ImageButton buttonDelete;

        ProfitViewHolder(final View view) {
            super(view);
            editTextCost = (EditText) view.findViewById(R.id.cost);
            editTextWeight = (EditText) view.findViewById(R.id.weight);
            editTextQuantity = (EditText) view.findViewById(R.id.quantity);
            textViewSum = (TextView) view.findViewById(R.id.sum);
            buttonDelete = (ImageButton) view.findViewById(R.id.profit_delete);
            TextWatcher setSum = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(editTextWeight.getText().toString().trim().length()>=1&&editTextCost.getText().toString().trim().length()>=1&&editTextQuantity.getText().toString().trim().length()>=1){
                        double weight = Double.parseDouble(editTextWeight.getText().toString());
                        double cost = Double.parseDouble(editTextCost.getText().toString());
                        textViewSum.setText(new DecimalFormat("#####.##").format(cost*1000/weight));
                    }
                }
            };
            editTextCost.addTextChangedListener(setSum);
            editTextWeight.addTextChangedListener(setSum);
            editTextQuantity.addTextChangedListener(setSum);

        }
    }

    public void addNewItem(){
        counter++;
        notifyDataSetChanged();
    }
}
