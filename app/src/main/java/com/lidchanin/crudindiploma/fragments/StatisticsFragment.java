package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

/**
 * Class extends {@link Fragment}.
 *
 * @author Lidchanin
 * @see android.support.v4.app.Fragment
 */
public class StatisticsFragment extends Fragment {

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

        List<List<Statistic>> statisticsByMonths = initData(statisticDao);
        initRV(view, statisticDao, statisticsByMonths);

        return view;
    }

    private List<List<Statistic>> initData(StatisticDao statisticDao) {
        List<Statistic> statistics = statisticDao.queryBuilder()
                .orderAsc(StatisticDao.Properties.Date).list();
        List<List<Statistic>> statisticsByMonths = ModelUtils.divideStatisticsByMonths(statistics);
        ModelUtils.sortStatisticsByName(statisticsByMonths);
        return statisticsByMonths;
    }

    private void initRV(View view, StatisticDao statisticDao,
                        List<List<Statistic>> statisticsByMonths) {
        RecyclerView mainRV = (RecyclerView) view.findViewById(R.id.statistics_main_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mainRV.setLayoutManager(layoutManager);
        StatisticsMainRVAdapter mainRVAdapter = new StatisticsMainRVAdapter(getContext(),
                statisticDao, statisticsByMonths);
        mainRV.setAdapter(mainRVAdapter);
    }
}
