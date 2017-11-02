package com.lidchanin.crudindiploma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.greenrobot.greendao.database.Database;

import java.util.List;

import static com.lidchanin.crudindiploma.utils.ModelUtils.divideStatisticsByMonths;
import static com.lidchanin.crudindiploma.utils.ModelUtils.getMonthWithStep;
import static com.lidchanin.crudindiploma.utils.ModelUtils.removeDuplicatesInStatistics;

/**
 * Class extends {@link Fragment}.
 *
 * @author Lidchanin
 * @see android.support.v4.app.Fragment
 */
public class StatisticsFragment extends Fragment {

    private static final String TAG = StatisticsFragment.class.getSimpleName();

    private StatisticsMainRVAdapter mainRVAdapter;

    private Database database;
    private StatisticDao statisticDao;

    private List<List<Statistic>> statisticsByMonths;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        statisticsByMonths = initData();
        initRV(view, statisticsByMonths);
        initButtons(view);

        return view;
    }

    private List<List<Statistic>> initData() {
        prepareDB();
        List<Statistic> statistics = statisticDao.queryBuilder()
                .orderAsc(StatisticDao.Properties.Date).list();
        database.close();

        statisticsByMonths = divideStatisticsByMonths(statistics);
        for (int i = 0; i < statisticsByMonths.size(); i++) {
            statisticsByMonths.set(i,
                    removeDuplicatesInStatistics(statisticsByMonths.get(i)));
        }
        return statisticsByMonths;
    }

    private void initRV(final View view, final List<List<Statistic>> statisticsByMonths) {
        RecyclerView mainRV = (RecyclerView) view.findViewById(R.id.statistics_main_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mainRV.setLayoutManager(layoutManager);
        mainRVAdapter = new StatisticsMainRVAdapter(getContext(), statisticDao, statisticsByMonths);
        mainRV.setAdapter(mainRVAdapter);
    }

    private void initButtons(final View view) {
        initButtonMonth(view);
        initButtonHalfYear(view);
        initButtonYear(view);
    }

    private void initButtonMonth(final View view) {
        Button button = (Button) view.findViewById(R.id.button_show_month);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillInAdapterWithStatistics(0);
            }
        });
    }

    private void initButtonHalfYear(final View view) {
        Button button = (Button) view.findViewById(R.id.button_show_half_year);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillInAdapterWithStatistics(-6);
            }
        });
    }

    private void initButtonYear(final View view) {
        Button button = (Button) view.findViewById(R.id.button_show_year);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillInAdapterWithStatistics(-12);
            }
        });
    }

    private void fillInAdapterWithStatistics(final int steps) {
        long currentMonth = getMonthWithStep(System.currentTimeMillis(), steps);
        prepareDB();
        List<Statistic> statistics = statisticDao.queryBuilder()
                .where(StatisticDao.Properties.Date.ge(currentMonth)).list();
        database.close();

        statisticsByMonths.clear();
        statisticsByMonths.addAll(divideStatisticsByMonths(statistics));
        for (int i = 0; i < statisticsByMonths.size(); i++) {
            statisticsByMonths.set(i,
                    removeDuplicatesInStatistics(statisticsByMonths.get(i)));
        }
        mainRVAdapter.notifyDataSetChanged();
    }

    private void prepareDB() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(
                getContext(),
                "db",
                null
        );
        database = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();
        statisticDao = daoSession.getStatisticDao();
    }
}
