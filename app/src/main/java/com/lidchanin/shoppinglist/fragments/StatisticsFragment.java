package com.lidchanin.shoppinglist.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidchanin.shoppinglist.R;
import com.lidchanin.shoppinglist.adapters.StatisticsMainRVAdapter;
import com.lidchanin.shoppinglist.database.Statistic;
import com.lidchanin.shoppinglist.database.dao.DaoMaster;
import com.lidchanin.shoppinglist.database.dao.DaoSession;
import com.lidchanin.shoppinglist.database.dao.StatisticDao;
import com.lidchanin.shoppinglist.utils.ModelUtils;

import org.greenrobot.greendao.database.Database;

import java.util.Calendar;
import java.util.List;

import static com.lidchanin.shoppinglist.utils.ModelUtils.convertLongDateToString;
import static com.lidchanin.shoppinglist.utils.ModelUtils.convertStringDateToLong;
import static com.lidchanin.shoppinglist.utils.ModelUtils.divideStatisticsByMonths;
import static com.lidchanin.shoppinglist.utils.ModelUtils.getCurrentMonth;
import static com.lidchanin.shoppinglist.utils.ModelUtils.getMonthWithStep;
import static com.lidchanin.shoppinglist.utils.ModelUtils.removeDuplicatesInStatistics;

/**
 * Class extends {@link Fragment}. Implements {@link android.view.View.OnClickListener}.
 *
 * @author Lidchanin
 * @see android.support.v4.app.Fragment
 * @see android.view.View.OnClickListener
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = StatisticsFragment.class.getSimpleName();

    private EditText etFirstDate;
    private EditText etSecondDate;

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
            case R.id.button_show_alternate:
                showDialogForOtherStatistics();
                break;
            case R.id.button_first_date_in_alternate_stat_dialog:
                setDateInViewFromPicker(etFirstDate);
                break;
            case R.id.button_second_date_in_alternate_stat_dialog:
                setDateInViewFromPicker(etSecondDate);
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
        Button buttonOther = (Button) view.findViewById(R.id.button_show_alternate);
        buttonOther.setOnClickListener(this);
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

    private void showDialogForOtherStatistics() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                R.style.MyDialogTheme);
        builder.setTitle(R.string.alternate_date);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Drawable drawable = getContext().getResources()
                .getDrawable(R.drawable.ic_date_range_black_24dp);


        etFirstDate = new EditText(getContext());
        etFirstDate.setId(R.id.button_first_date_in_alternate_stat_dialog);
        etFirstDate.setHint(R.string.date_picker_dialog_first_date);
        etFirstDate.setInputType(0);
        etFirstDate.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable,
                null);
        etFirstDate.setOnClickListener(this);

        etSecondDate = new EditText(getContext());
        etSecondDate.setId(R.id.button_second_date_in_alternate_stat_dialog);
        etSecondDate.setHint(R.string.date_picker_dialog_second_date);
        etSecondDate.setInputType(0);
        etSecondDate.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable,
                null);
        etSecondDate.setOnClickListener(this);

        linearLayout.addView(etFirstDate);
        linearLayout.addView(etSecondDate);
        builder.setView(linearLayout);

        builder.setPositiveButton(R.string.show, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: " + etFirstDate.getText().toString()
                        + "\t" + etSecondDate.getText().toString());
                Log.d(TAG, "onClick: "
                        + convertStringDateToLong(etFirstDate.getText().toString())
                        + "\t"
                        + convertStringDateToLong(etSecondDate.getText().toString())
                );
                if (etFirstDate.getText().toString()
                        .equals(getString(R.string.date_picker_dialog_first_date))
                        || etSecondDate.getText().toString()
                        .equals(getString(R.string.date_picker_dialog_second_date))) {
                    Toast.makeText(
                            getContext(),
                            getString(R.string.please_enter_all_data),
                            Toast.LENGTH_SHORT
                    ).show();
                    showDialogForOtherStatistics();
                } else {
                    long firstDate = convertStringDateToLong(etFirstDate.getText().toString());
                    long secondDate = convertStringDateToLong(etSecondDate.getText().toString());
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
                    Log.i(TAG, "onClick: selected period: " + firstDate + " - " + secondDate);
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void setDateInViewFromPicker(final EditText et) {
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
                et.setText(convertLongDateToString(date));
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
