package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.StatisticsMainRVAdapter;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.dao.DaoMaster;
import com.lidchanin.crudindiploma.database.dao.DaoSession;
import com.lidchanin.crudindiploma.database.dao.StatisticDao;
import com.lidchanin.crudindiploma.utils.ModelUtils;

import org.greenrobot.greendao.database.Database;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private static final String TAG = StatisticsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "db", null);
        final Database database = helper.getWritableDb();
        final DaoMaster daoMaster = new DaoMaster(database);
        final DaoSession daoSession = daoMaster.newSession();
        final StatisticDao statisticDao = daoSession.getStatisticDao();

        List<Statistic> statistics = statisticDao.queryBuilder()
                .orderAsc(StatisticDao.Properties.Date).list();
        Log.d(TAG, "onCreateView: " + statistics.size());
        Log.d(TAG, "\n_______________in db ");
        for (Statistic s : statistics) {
            Log.d(TAG, s.getName() + "\t" + s.getDate() + "\t" + s.getTotalCost());
        }
        Log.d(TAG, "______________________________\n");

        List<List<Statistic>> statisticsByMonths = ModelUtils.divideStatisticsByMonths(statistics);
        Log.d(TAG, "\n_______________after divide");
        for (List<Statistic> sl : statisticsByMonths) {
            Log.d(TAG, "--- list");
            for (Statistic s : sl) {
                Log.d(TAG, s.getName() + "\t" + s.getDate() + "\t" + s.getTotalCost());
            }
        }
        Log.d(TAG, "______________________________\n");

        for (List<Statistic> sl : statisticsByMonths) {
            if (sl.size() > 0) {
                Collections.sort(sl, new Comparator<Statistic>() {
                    @Override
                    public int compare(final Statistic object1, final Statistic object2) {
                        return object1.getName().compareTo(object2.getName());
                    }
                });
            }
        }
        Log.d(TAG, "\n_______________after sorting");
        for (List<Statistic> sl : statisticsByMonths) {
            Log.d(TAG, "--- list");
            for (Statistic s : sl) {
                Log.d(TAG, s.getName() + "\t" + s.getDate() + "\t" + s.getTotalCost());
            }
        }
        Log.d(TAG, "______________________________\n");

        RecyclerView mainRV = (RecyclerView) view.findViewById(R.id.statistics_main_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mainRV.setLayoutManager(layoutManager);
        StatisticsMainRVAdapter mainRVAdapter = new StatisticsMainRVAdapter(getContext(),
                statisticDao, statisticsByMonths);
        mainRV.setAdapter(mainRVAdapter);

        return view;
    }
}
