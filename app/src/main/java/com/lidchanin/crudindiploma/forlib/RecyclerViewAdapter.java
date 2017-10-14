package com.lidchanin.crudindiploma.forlib;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.MainRVAdapter;

import java.util.List;

/**
 * Created by Alexander Destroyed on 13.10.2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    List<RecyclerViewItems> recyclerViewItemses;

    public RecyclerViewAdapter (List<RecyclerViewItems> recyclerViewItemses){
        this.recyclerViewItemses = recyclerViewItemses;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.themeName.setText(recyclerViewItemses.get(position).getName());
        holder.themeCost.setText(recyclerViewItemses.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return recyclerViewItemses.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CardView rootView;
        private TextView themeName;
        private TextView themeCost;
        private ImageView themeImage;

        /**
         * Constructor.
         *
         * @param itemView - item in {@link RecyclerView}.
         */
        RecyclerViewHolder(View itemView) {
            super(itemView);
            rootView = (CardView) itemView.findViewById(R.id.theme_root_view);
            themeName = (TextView) itemView.findViewById(R.id.theme_name);
            themeCost = (TextView) itemView.findViewById(R.id.theme_cost);
            themeImage = (ImageView) itemView.findViewById(R.id.theme_image);
        }
    }
}


