package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.dao.StatisticDao;

import java.util.List;

/**
 * Class {@link StatisticsChildRVAdapter} provide a binding from an app-specific data set to views
 * that are displayed within a {@link RecyclerView}.
 * Class extends {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class StatisticsChildRVAdapter
        extends RecyclerView.Adapter<StatisticsChildRVAdapter.StatisticsChildViewHolder> {

    private Context context;

    private StatisticDao statisticDao;

    private List<Statistic> statistics;

    /**
     * Constructor.
     *
     * @param context      {@link Context}.
     * @param statistics   {@link List} with all {@link Statistic}s.
     * @param statisticDao {@link StatisticDao} exemplar.
     */
    public StatisticsChildRVAdapter(final Context context,
                                    final StatisticDao statisticDao,
                                    final List<Statistic> statistics) {
        this.context = context;
        this.statistics = statistics;
        this.statisticDao = statisticDao;
    }

    @Override
    public StatisticsChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_statistics_child_rv, parent, false);
        return new StatisticsChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatisticsChildViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();

        holder.tvName.setText(statistics.get(adapterPosition).getName());
        holder.tvTotalCost.setText(String.valueOf(statistics.get(adapterPosition).getTotalCost()));
        holder.tvQuantity.setText(String.valueOf(statistics.get(adapterPosition).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }

    /**
     * Static class {@link StatisticsChildViewHolder} describes an item view and metadata about
     * its place within the {@link RecyclerView}.
     * Class extends {@link RecyclerView.ViewHolder}.
     *
     * @author Lidchanin
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class StatisticsChildViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvTotalCost;
        private TextView tvQuantity;

        StatisticsChildViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_in_statistics_child_rv);
            tvTotalCost = (TextView) itemView.findViewById(R.id.tv_total_cost_in_statistics_child_rv);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity_in_statistics_child_rv);
        }
    }
}
