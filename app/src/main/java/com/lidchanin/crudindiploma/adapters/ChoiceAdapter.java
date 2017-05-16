package com.lidchanin.crudindiploma.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.activities.NameAndCostEditActivity;

import java.util.ArrayList;
import java.util.List;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ChoiceViewHolder> {

    private List<String> CostList;
    private List<String> NameList;
    private String outputName;
    private String outputCost;
    final String TAG = this.getClass().getSimpleName();
    public ChoiceAdapter (final List<String> nameList,final List<String> costList) {
        this.NameList = new ArrayList<>(nameList);
        this.CostList = new ArrayList<>(costList);
    }


    @Override
    public ChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_inside_choice,parent,false);
        ChoiceViewHolder choiceViewHolder= new ChoiceViewHolder(view);
        return choiceViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ChoiceViewHolder holder, final int position) {
        holder.costOrNameChoiceTextBox.setText(NameList.get(position));
        holder.designedFrameChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, String.valueOf(position));
                if(NameList==CostList){
                    outputCost = CostList.get(position);
                    Toast.makeText(v.getContext(),outputName+"And"+outputCost,Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(v.getContext().getApplicationContext(), NameAndCostEditActivity.class);
                    intent.putExtra("OutPutName",outputName);
                    intent.putExtra("OutPutCost",outputCost);
                    v.getContext().getApplicationContext().startActivity(intent);
                }
                else {
                    outputName=NameList.get(position);
                    NameList.clear();
                    NameList = CostList;
                    ChoiceAdapter.this.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return NameList.size();
    }

    static class ChoiceViewHolder extends RecyclerView.ViewHolder {
        FrameLayout designedFrameChoice;
        TextView costOrNameChoiceTextBox;

        ChoiceViewHolder(final View view) {
            super(view);
            designedFrameChoice = (FrameLayout) view.findViewById(R.id.designed_frame_choice);
            costOrNameChoiceTextBox = (TextView) view.findViewById(R.id.cost_or_name_choice);
        }
    }
}