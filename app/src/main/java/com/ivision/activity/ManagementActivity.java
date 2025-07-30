package com.ivision.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.ManagementListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.model.Management;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagementActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private ManagementListAdapter adapter;
    private ArrayList<Management> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ManagementActivity.this;

        adapter = new ManagementListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
            }
        });

        setToolbar("Management");

        init();

        getList();
    }

    private void init() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);

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
        apiInterface.getList(Constant.managementListUrl).enqueue(new Callback<JsonObject>() {

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
            Management model = CommonParsing.bindManagementData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }
}