package com.ivision.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.ivision.adapter.ResultAdapter;
import com.ivision.adapter.SubjectListAdapter;
import com.ivision.databinding.ActivityResultBinding;
import com.ivision.model.Result;
import com.ivision.model.Subject;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends BaseActivity {

    private ActivityResultBinding binding;
    private ArrayList<Result> list = new ArrayList<>();
    private ResultAdapter adapter;
    private String subjectId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ResultActivity.this;

        adapter = new ResultAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
            }
        });

        setToolbar("Result");

        init();
        getSubjectList();
    }


    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvResult.setLayoutManager(layoutManager);
        binding.rvResult.setItemAnimator(new DefaultItemAnimator());
        binding.rvResult.setDrawingCacheEnabled(true);
        binding.rvResult.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.rvResult.setItemViewCacheSize(20);
        binding.rvResult.setHasFixedSize(false);
        binding.rvResult.setAdapter(adapter);

        binding.rvResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        recyclerView.getChildCount() == 0 ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            subjectId = "";
            getSubjectList();
        });
    }

    private void getList() {

        Common.showProgressDialog(context, getString(R.string.please_wait));
        list.clear();

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.resultUrl, Common.getStudentId(context), subjectId).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                    JsonArray jsonArray1 = new JsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonArray1.add(jsonArray.get(i));
                    }
                    bindData(jsonArray1);
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
        list.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Result model = CommonParsing.bindResultData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();

        if (!list.isEmpty()) bindChartData();
    }

    private void bindChartData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int sum = 0;
            for (int j = 0; j < list.get(i).getMark().size(); j++) {
                sum += Integer.parseInt(list.get(i).getMark().get(j));
            }
            barEntries.add(new BarEntry(i, Float.parseFloat(String.valueOf(sum))));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Result");
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

        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            labels.add(String.valueOf(Common.changeDateFormat(list.get(i).getDate(),"dd-MMM")));
        }
        binding.chartView.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        XAxis xAxis = binding.chartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        binding.chartView.setDrawValueAboveBar(true);
        binding.chartView.getDescription().setEnabled(false);
        binding.chartView.animateXY(1000, 2000);
    }

    private void getSubjectList() {

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.subjectListUrl, Common.getStudentId(context)).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                        bindSubjectData(jsonArray);
                    } else {
                        binding.llSubjectList.setVisibility(View.GONE);
                        binding.rvResult.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void bindSubjectData(JsonArray jsonArray) {

        ArrayList<Subject> subjectList = new ArrayList<>();

        Subject subject = new Subject();
        subject.setId("0");
        subject.setSubject("All");
        subjectList.add(subject);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Subject model = CommonParsing.bindSubjectData(object);
            subjectList.add(model);
        }

        subjectId = subjectList.get(0).getId();

        SubjectListAdapter subjectListAdapter = new SubjectListAdapter(context, subjectList, 1, subjectId, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                subjectId = subjectList.get(position).getId();
                getList();
            }
        });

        StaggeredGridLayoutManager mLayoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        binding.recyclerView2.setLayoutManager(mLayoutManager1);
        binding.recyclerView2.setItemViewCacheSize(20);
        binding.recyclerView2.setDrawingCacheEnabled(true);
        binding.recyclerView2.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView2.setAdapter(subjectListAdapter);
        binding.recyclerView2.setHasFixedSize(true);

        binding.llSubjectList.setVisibility(View.VISIBLE);

        getList();
    }
}