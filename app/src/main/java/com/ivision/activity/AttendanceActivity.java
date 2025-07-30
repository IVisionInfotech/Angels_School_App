package com.ivision.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.databinding.ActivityAttendanceBinding;
import com.ivision.model.Attendance;
import com.ivision.model.MonthData;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;

import com.ivision.zcustomcalendar.CustomCalendar;
import com.ivision.zcustomcalendar.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends BaseActivity {

    private ActivityAttendanceBinding binding;
    private ArrayList<Attendance> monthList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = AttendanceActivity.this;

        setToolbar("Attendance");


        getList();

    }


    private void getList() {

        Common.showProgressDialog(context, context.getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.attendanceUrl, Common.getStudentId(context)).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();

                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                        bindData(jsonArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Common.hideProgressDialog();
            }
        });
    }

    private void bindData(JsonArray jsonArray) {

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Attendance model = CommonParsing.bindAttendanceData(object);
            monthList.add(model);
        }
        if (!monthList.isEmpty()) bindChartData();
    }

    private void bindChartData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0, Float.parseFloat(monthList.get(0).getJun().getTotal())));
        barEntries.add(new BarEntry(1, Float.parseFloat(monthList.get(0).getJul().getTotal())));
        barEntries.add(new BarEntry(2, Float.parseFloat(monthList.get(0).getAug().getTotal())));
        barEntries.add(new BarEntry(3, Float.parseFloat(monthList.get(0).getSep().getTotal())));
        barEntries.add(new BarEntry(4, Float.parseFloat(monthList.get(0).getOct().getTotal())));
        barEntries.add(new BarEntry(5, Float.parseFloat(monthList.get(0).getNov().getTotal())));
        barEntries.add(new BarEntry(6, Float.parseFloat(monthList.get(0).getDec().getTotal())));
        barEntries.add(new BarEntry(7, Float.parseFloat(monthList.get(0).getJan().getTotal())));
        barEntries.add(new BarEntry(8, Float.parseFloat(monthList.get(0).getFeb().getTotal())));
        barEntries.add(new BarEntry(9, Float.parseFloat(monthList.get(0).getMar().getTotal())));
        barEntries.add(new BarEntry(10, Float.parseFloat(monthList.get(0).getApr().getTotal())));
        barEntries.add(new BarEntry(11, Float.parseFloat(monthList.get(0).getMay().getTotal())));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Attendance");
        barDataSet.setColors(getResources().getColor(R.color.colorPrimary));
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        binding.chartView.setData(barData);

        binding.chartView.getAxisRight().setDrawGridLines(false);
        binding.chartView.getAxisLeft().setDrawGridLines(true);
        binding.chartView.getXAxis().setDrawGridLines(false);

        YAxis rightYAxis = binding.chartView.getAxisRight();
        rightYAxis.setEnabled(false);

        binding.chartView.setTouchEnabled(false);

        List<String> labels = Arrays.asList("Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May");

        binding.chartView.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        XAxis xAxis = binding.chartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        binding.chartView.setDrawValueAboveBar(true);
        binding.chartView.getDescription().setEnabled(false);
        binding.chartView.animateXY(500, 1000);
        bindMonths();
    }

    private void bindMonths() {
        List<String> list = new ArrayList<>();

        list.add(monthList.get(0).getJun().getMonth());
        list.add(monthList.get(0).getJul().getMonth());
        list.add(monthList.get(0).getAug().getMonth());
        list.add(monthList.get(0).getSep().getMonth());
        list.add(monthList.get(0).getOct().getMonth());
        list.add(monthList.get(0).getNov().getMonth());
        list.add(monthList.get(0).getDec().getMonth());
        list.add(monthList.get(0).getJan().getMonth());
        list.add(monthList.get(0).getFeb().getMonth());
        list.add(monthList.get(0).getMar().getMonth());
        list.add(monthList.get(0).getApr().getMonth());
        list.add(monthList.get(0).getMay().getMonth());


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_list_item, list);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnSpinnerItemClickListener((position, itemAtPosition) -> {
            try {
                Calendar calendar1 = Calendar.getInstance();
                Date d = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH).parse(itemAtPosition);
                calendar1.setTime(Objects.requireNonNull(d));
                bindCalendarData(calendar1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        binding.spinner.setSelection(0);
    }

    private void bindCalendarData(Calendar calendar) {
        // Initialize description hashmap
        HashMap<Object, Property> descHashMap = new HashMap<>();

        // Initialize default property
        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.calendar_default_view;
        defaultProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("default", defaultProperty);

        // for current date
        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.calendar_current_view;
        currentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("current", currentProperty);

        // for present date
        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.calendar_present_view;
        presentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("P", presentProperty);

        // For absent
        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.calendar_absent_view;
        absentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("A", absentProperty);

        // set desc hashmap on custom calendar
        binding.calendarView.setMapDescToProp(descHashMap);

        // Put values
        HashMap<Integer, Object> dateHashmap = putValues(calendar);
        // set date
        binding.calendarView.setDate(calendar, dateHashmap);

        binding.calendarView.setOnDateSelectedListener((view, selectedDate, desc) -> {
            // get string date
            String sDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                    + "-" + (selectedDate.get(Calendar.MONTH) + 1)
                    + "-" + selectedDate.get(Calendar.YEAR);

            // display date in toast
            Common.showToast(sDate);
        });

        binding.calendarView.getMonthYearTextView().setTextColor(getResources().getColor(R.color.darkBlack));
        binding.calendarView.getMonthYearTextView().setAllCaps(true);
        binding.calendarView.getMonthYearTextView().setTextSize(15f);
        binding.calendarView.setNavigationButtonEnabled(CustomCalendar.NEXT, false);
        binding.calendarView.setNavigationButtonEnabled(CustomCalendar.PREVIOUS, false);
    }

    private HashMap<Integer, Object> putValues(Calendar calendar) {
        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");
        String monthName = Objects.requireNonNull(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())).toUpperCase();

        MonthData monthData = null;
        switch (monthName) {
            case "JANUARY":
                monthData = monthList.get(0).getJan();
                break;
            case " FEBRUARY":
                monthData = monthList.get(0).getFeb();
                break;
            case "MARCH":
                monthData = monthList.get(0).getMar();
                break;
            case "APRIL":
                monthData = monthList.get(0).getApr();
                break;
            case "MAY":
                monthData = monthList.get(0).getMay();
                break;
            case "JUNE":
                monthData = monthList.get(0).getJun();
                break;
            case "JULY":
                monthData = monthList.get(0).getJul();
                break;
            case "AUGUST":
                monthData = monthList.get(0).getAug();
                break;
            case "SEPTEMBER":
                monthData = monthList.get(0).getSep();
                break;
            case "OCTOBER":
                monthData = monthList.get(0).getOct();
                break;
            case "NOVEMBER":
                monthData = monthList.get(0).getNov();
                break;
            case "DECEMBER":
                monthData = monthList.get(0).getDec();
                break;
            default:
                break;

        }
        if (monthData != null) {
            dateHashmap.put(1, monthData.getDay1());
            dateHashmap.put(2, monthData.getDay2());
            dateHashmap.put(3, monthData.getDay3());
            dateHashmap.put(4, monthData.getDay4());
            dateHashmap.put(5, monthData.getDay5());
            dateHashmap.put(6, monthData.getDay6());
            dateHashmap.put(7, monthData.getDay7());
            dateHashmap.put(8, monthData.getDay8());
            dateHashmap.put(9, monthData.getDay9());
            dateHashmap.put(10, monthData.getDay10());
            dateHashmap.put(11, monthData.getDay11());
            dateHashmap.put(12, monthData.getDay12());
            dateHashmap.put(13, monthData.getDay13());
            dateHashmap.put(14, monthData.getDay14());
            dateHashmap.put(15, monthData.getDay15());
            dateHashmap.put(16, monthData.getDay16());
            dateHashmap.put(17, monthData.getDay17());
            dateHashmap.put(18, monthData.getDay18());
            dateHashmap.put(19, monthData.getDay19());
            dateHashmap.put(20, monthData.getDay20());
            dateHashmap.put(21, monthData.getDay21());
            dateHashmap.put(22, monthData.getDay22());
            dateHashmap.put(23, monthData.getDay23());
            dateHashmap.put(24, monthData.getDay24());
            dateHashmap.put(25, monthData.getDay25());
            dateHashmap.put(26, monthData.getDay26());
            dateHashmap.put(27, monthData.getDay27());
            dateHashmap.put(28, monthData.getDay28());
            dateHashmap.put(29, monthData.getDay29());
            dateHashmap.put(30, monthData.getDay30());
            dateHashmap.put(31, monthData.getDay31());
            return dateHashmap;
        } else return null;
    }
}