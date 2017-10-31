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
import android.widget.Button;

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

    private static final String TAG = StatisticsFragment.class.getSimpleName();

    private StatisticsMainRVAdapter mainRVAdapter;

    private DaoMaster.DevOpenHelper helper;
    private Database database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StatisticDao statisticDao;

    private List<List<Statistic>> statisticsByMonths;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        helper = new DaoMaster.DevOpenHelper(
                getContext(),
                "db",
                null
        );
        database = helper.getWritableDb();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        statisticDao = daoSession.getStatisticDao();

        statisticsByMonths = initData();
        initRV(view, statisticsByMonths);
        initButtons(view);

        return view;
    }

    private List<List<Statistic>> initData() {
        List<Statistic> statistics = statisticDao.queryBuilder()
                .orderAsc(StatisticDao.Properties.Date).list();
        statisticsByMonths = ModelUtils.divideStatisticsByMonths(statistics);
        for (int i = 0; i < statisticsByMonths.size(); i++) {
            statisticsByMonths.set(i,
                    ModelUtils.removeDuplicatesInStatistics(statisticsByMonths.get(i)));
        }
        database.close();
        helper = new DaoMaster.DevOpenHelper(
                getContext(),
                "db",
                null
        );
        database = helper.getWritableDb();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        statisticDao = daoSession.getStatisticDao();
        return statisticsByMonths;
    }

    private void initRV(View view, List<List<Statistic>> statisticsByMonths) {
        RecyclerView mainRV = (RecyclerView) view.findViewById(R.id.statistics_main_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mainRV.setLayoutManager(layoutManager);
        mainRVAdapter = new StatisticsMainRVAdapter(getContext(), statisticDao, statisticsByMonths);
        mainRV.setAdapter(mainRVAdapter);
    }

    private void initButtons(View view) {
        initButtonMonth(view);
        initButtonHalfYear(view);
        initButtonYear(view);
    }

    private void initButtonMonth(final View view) {
        Button button = (Button) view.findViewById(R.id.button_show_month);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.close();
                helper = new DaoMaster.DevOpenHelper(
                        getContext(),
                        "db",
                        null
                );
                database = helper.getWritableDb();
                daoMaster = new DaoMaster(database);
                daoSession = daoMaster.newSession();
                statisticDao = daoSession.getStatisticDao();

                statisticsByMonths.clear();
                //
                Log.d(TAG, "---- in DB");
                for (Statistic s : statisticDao.loadAll()) {
                    Log.d(TAG, "- " + s.getDate() + "\t" + s.getName() + "\t" + s.getTotalCost());
                }
                //
                String sMonth = ModelUtils.convertLongDateToString(System.currentTimeMillis());
                long lMonth = ModelUtils.convertStringDateToLong(getContext(), sMonth);
                List<Statistic> statistics = statisticDao.queryBuilder()
                        .where(StatisticDao.Properties.Date.ge(lMonth)).list();
                Log.d(TAG, "---- in List");
                for (Statistic s : statistics) {
                    Log.d(TAG, "- " + s.getDate() + "\t" + s.getName() + "\t" + s.getTotalCost());
                }
                for (int i = 0; i < statistics.size(); i++) {
                    if (!ModelUtils.convertLongDateToString(statistics.get(i).getDate())
                            .equals(sMonth)) {
                        statistics.subList(i, statistics.size()).clear();
                        break;
                    }
                }
                statistics = ModelUtils.removeDuplicatesInStatistics(statistics);
                statisticsByMonths.add(statistics);
                mainRVAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initButtonHalfYear(View view) {

    }

    private void initButtonYear(View view) {

    }
}
