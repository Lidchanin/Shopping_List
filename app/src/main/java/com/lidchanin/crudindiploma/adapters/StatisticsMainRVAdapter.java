package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.dao.StatisticDao;
import com.lidchanin.crudindiploma.utils.ModelUtils;

import java.util.List;

/**
 * Class {@link StatisticsMainRVAdapter} is an adapter for {@link RecyclerView}.
 *
 * @author Lidchanin
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public class StatisticsMainRVAdapter
        extends RecyclerView.Adapter<StatisticsMainRVAdapter.StatisticsMainVewHolder> {

    private static final String TAG = StatisticsMainRVAdapter.class.getSimpleName();

    private Context context;

    private StatisticDao statisticDao;

    private List<List<Statistic>> statistics;

    /**
     * Constructor.
     *
     * @param context      {@link Context}.
     * @param statisticDao {@link StatisticDao} exemplar.
     * @param statistics   {@link List} with sorted by date statistics.
     */
    public StatisticsMainRVAdapter(final Context context,
                                   final StatisticDao statisticDao,
                                   final List<List<Statistic>> statistics) {
        this.context = context;
        this.statisticDao = statisticDao;
        this.statistics = statistics;
    }

    @Override
    public StatisticsMainVewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_statistics_main_rv, parent, false);
        return new StatisticsMainVewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatisticsMainVewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();

        Log.d(TAG, "\n_____________________________________item " + adapterPosition + " before");
        for (Statistic s : statistics.get(adapterPosition)) {
            Log.d(TAG, s.getName() + "\t" + s.getDate() + "\t" + s.getTotalCost());
        }
        Log.d(TAG, "__________________________________________________________\n");

        Log.d(TAG, "\n_____________________________________item " + adapterPosition + " after");
        statistics.set(adapterPosition,
                ModelUtils.removeDuplicatesInStatistics(statistics.get(adapterPosition)));
        for (Statistic s : statistics.get(adapterPosition)) {
            Log.d(TAG, s.getName() + "\t" + s.getDate() + "\t" + s.getTotalCost());
        }
        Log.d(TAG, "__________________________________________________________\n");

        holder.tvName.setText(ModelUtils.convertLongDateToString(
                statistics.get(adapterPosition).get(0).getDate()));

        holder.tvCost.setText(String.valueOf(
                ModelUtils.calculateTotalCostInStatistics(statistics.get(adapterPosition))));

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.childRV.setLayoutManager(layoutManager);
        StatisticsChildRVAdapter childRVAdapter = new StatisticsChildRVAdapter(context,
                statisticDao, statistics.get(adapterPosition));
        holder.childRV.setAdapter(childRVAdapter);
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }

    /**
     * Class {@link StatisticsMainVewHolder} is the View Holder for
     * {@link android.support.v7.widget.RecyclerView.Adapter}.
     *
     * @author Lidchanin
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    static class StatisticsMainVewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvCost;
        private RecyclerView childRV;

        StatisticsMainVewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_in_statistics_main_rv);
            tvCost = (TextView) itemView.findViewById(R.id.tv_cost_in_statistics_main_rv);
            childRV = (RecyclerView) itemView.findViewById(R.id.statistics_child_rv);
        }
    }
}
