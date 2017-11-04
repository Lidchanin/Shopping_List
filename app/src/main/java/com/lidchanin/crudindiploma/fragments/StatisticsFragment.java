package com.lidchanin.crudindiploma.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.StatisticsMainRVAdapter;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.dao.DaoMaster;
import com.lidchanin.crudindiploma.database.dao.DaoSession;
import com.lidchanin.crudindiploma.database.dao.StatisticDao;
import com.lidchanin.crudindiploma.utils.ModelUtils;

import org.greenrobot.greendao.database.Database;

import java.util.Calendar;
import java.util.List;

import static com.lidchanin.crudindiploma.utils.ModelUtils.convertLongDateToString;
import static com.lidchanin.crudindiploma.utils.ModelUtils.convertStringDateToLong;
import static com.lidchanin.crudindiploma.utils.ModelUtils.divideStatisticsByMonths;
import static com.lidchanin.crudindiploma.utils.ModelUtils.getCurrentMonth;
import static com.lidchanin.crudindiploma.utils.ModelUtils.getMonthWithStep;
import static com.lidchanin.crudindiploma.utils.ModelUtils.removeDuplicatesInStatistics;

/**
 * Class extends {@link Fragment}. Implements {@link android.view.View.OnClickListener}.
 *
 * @author Lidchanin
 * @see android.support.v4.app.Fragment
 * @see android.view.View.OnClickListener
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = StatisticsFragment.class.getSimpleName();

    private Button buttonFirstDate;
    private Button buttonSecondDate;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_show_month:
                fillInAdapterWithStatistics(getStatisticsWithStep(0));
                break;
            case R.id.button_show_half_year:
                fillInAdapterWithStatistics(getStatisticsWithStep(-6));
                break;
            case R.id.button_show_year:
                fillInAdapterWithStatistics(getStatisticsWithStep(-12));
                break;
            case R.id.button_show_custom:
                showDialogForCustomStatistics();
                break;
            case R.id.button_first_date_in_custom_stat_dialog:
                setDateInButtonFromPicker(buttonFirstDate);
                break;
            case R.id.button_second_date_in_custom_stat_dialog:
                setDateInButtonFromPicker(buttonSecondDate);
                break;
        }
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
        Button buttonMonth = (Button) view.findViewById(R.id.button_show_month);
        buttonMonth.setOnClickListener(this);
        Button buttonHalfYear = (Button) view.findViewById(R.id.button_show_half_year);
        buttonHalfYear.setOnClickListener(this);
        Button buttonYear = (Button) view.findViewById(R.id.button_show_year);
        buttonYear.setOnClickListener(this);
        Button buttonCustom = (Button) view.findViewById(R.id.button_show_custom);
        buttonCustom.setOnClickListener(this);
    }

    private void fillInAdapterWithStatistics(List<Statistic> statistics) {
        statisticsByMonths.clear();
        statisticsByMonths.addAll(divideStatisticsByMonths(statistics));
        for (int i = 0; i < statisticsByMonths.size(); i++) {
            statisticsByMonths.set(i,
                    removeDuplicatesInStatistics(statisticsByMonths.get(i)));
        }
        mainRVAdapter.notifyDataSetChanged();
    }

    private List<Statistic> getStatisticsWithStep(final int steps) {
        long currentMonth = getMonthWithStep(System.currentTimeMillis(), steps);
        prepareDB();
        List<Statistic> statistics = statisticDao.queryBuilder()
                .where(StatisticDao.Properties.Date.ge(currentMonth)).list();
        database.close();
        return statistics;
    }

    private void showDialogForCustomStatistics() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                R.style.MyDialogTheme);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        buttonFirstDate = new Button(getContext());
        buttonFirstDate.setId(R.id.button_first_date_in_custom_stat_dialog);
        buttonFirstDate.setText(R.string.date_picker_dialog_first_date);
        buttonFirstDate.setOnClickListener(this);

        buttonSecondDate = new Button(getContext());
        buttonSecondDate.setId(R.id.button_second_date_in_custom_stat_dialog);
        buttonSecondDate.setText(R.string.date_picker_dialog_second_date);
        buttonSecondDate.setOnClickListener(this);

        linearLayout.addView(buttonFirstDate);
        linearLayout.addView(buttonSecondDate);
        builder.setView(linearLayout);

        builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: " + buttonFirstDate.getText().toString()
                        + "\t" + buttonSecondDate.getText().toString());
                Log.d(TAG, "onClick: "
                        + convertStringDateToLong(buttonFirstDate.getText().toString())
                        + "\t"
                        + convertStringDateToLong(buttonSecondDate.getText().toString())
                );
                if (buttonFirstDate.getText().toString()
                        .equals(getString(R.string.date_picker_dialog_first_date))
                        || buttonSecondDate.getText().toString()
                        .equals(getString(R.string.date_picker_dialog_second_date))) {
                    Toast.makeText(
                            getContext(),
                            getString(R.string.please_enter_all_data),
                            Toast.LENGTH_SHORT
                    ).show();
                    showDialogForCustomStatistics();
                } else {
                    long firstDate = convertStringDateToLong(buttonFirstDate.getText().toString());
                    long secondDate = convertStringDateToLong(buttonSecondDate.getText().toString());
                    if (firstDate > secondDate) {
                        long temp = firstDate;
                        firstDate = secondDate;
                        secondDate = temp;
                    }
                    secondDate = ModelUtils.getLastMomentOfMonth(secondDate);

                    prepareDB();
                    List<Statistic> statistics = statisticDao.queryBuilder()
                            .where(StatisticDao.Properties.Date.ge(firstDate),
                                    StatisticDao.Properties.Date.le(secondDate))
                            .list();
                    database.close();

                    fillInAdapterWithStatistics(statistics);
                    Log.d(TAG, "onClick: selected period: " + firstDate + "-" + secondDate);
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void setDateInButtonFromPicker(final Button button) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                long date = getCurrentMonth(calendar.getTimeInMillis());
                button.setText(convertLongDateToString(date));
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().findViewById(Resources.getSystem()
                .getIdentifier("day", "id", "android"))
                .setVisibility(View.GONE);
        // 1262304000000L - 01.01.2010 00:00
        datePickerDialog.getDatePicker().setMinDate(1262304000000L);
        datePickerDialog.show();
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
