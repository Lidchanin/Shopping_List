package com.lidchanin.crudindiploma.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.ProfitViewHolder> {
    private int counter=2;
    private static OnSumChangeListener onSumChangeListener;


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
                .inflate(R.layout.inside_profit_recyclerview, parent, false);
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
                if(holder.editTextWeight.getText().toString().trim().length()>=1&&holder.editTextCost.getText().toString().trim().length()>=1&&holder.editTextQuantity.getText().toString().trim().length()>=1){
                    double weight = Double.parseDouble(holder.editTextWeight.getText().toString());
                    double quantity = Double.parseDouble(holder.editTextQuantity.getText().toString());
                    double cost = Double.parseDouble(holder.editTextCost.getText().toString());
                    holder.textViewSum.setText(new DecimalFormat("#####.##").format(cost/weight));
                    onSumChangeListener.onSumChanged(holder.getAdapterPosition(),(cost/weight)*quantity);
                }
            }

        };

        holder.editTextCost.addTextChangedListener(setSum);
        holder.editTextWeight.addTextChangedListener(setSum);
        holder.editTextQuantity.addTextChangedListener(setSum);
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

    static class ProfitViewHolder extends RecyclerView.ViewHolder {
        EditText editTextCost;
        EditText editTextWeight;
        EditText editTextQuantity;
        TextView textViewSum;
        ImageButton buttonDelete;
        FrameLayout frameLayout;
        ProfitViewHolder(final View view) {
            super(view);
            frameLayout = (FrameLayout) view.findViewById(R.id.framelayout_profit);
            editTextCost = (EditText) view.findViewById(R.id.cost);
            editTextWeight = (EditText) view.findViewById(R.id.weight);
            editTextQuantity = (EditText) view.findViewById(R.id.quantity);
            textViewSum = (TextView) view.findViewById(R.id.sum);
            buttonDelete = (ImageButton) view.findViewById(R.id.profit_delete);
        }
        public void changeColor(){
            frameLayout.setBackgroundColor(Color.GREEN);
        }


    }
    public interface OnSumChangeListener {
        void onSumChanged(int key,double sum);
    }

    public void addNewItem(){
        counter++;
        notifyDataSetChanged();
    }


}
