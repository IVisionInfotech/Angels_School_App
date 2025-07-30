package com.ivision.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.ResultAdapter;
import com.ivision.adapter.ReviewAdapter;
import com.ivision.databinding.ActivityReviewBinding;
import com.ivision.model.Result;
import com.ivision.model.Review;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends BaseActivity {

    ActivityReviewBinding binding;
    private ReviewAdapter adapter;
    private ArrayList<Review> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ReviewActivity.this;

        adapter = new ReviewAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
            }
        });

        setToolbar("Review");

        init();
        getList();
    }

    private void init() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvReview.setLayoutManager(layoutManager);
        binding.rvReview.setItemAnimator(new DefaultItemAnimator());
        binding.rvReview.setDrawingCacheEnabled(true);
        binding.rvReview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.rvReview.setItemViewCacheSize(20);
        binding.rvReview.setHasFixedSize(false);
        binding.rvReview.setAdapter(adapter);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                getList();
            }
        });
    }

    private void getList() {

        Common.showProgressDialog(context, getString(R.string.please_wait));
        list.clear();

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getReviewList(Constant.reviewUrl, Common.getStudentId(context)).enqueue(new Callback<JsonObject>() {

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
                    } else {
                        list.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
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
            Review model = CommonParsing.bindReviewData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }
}