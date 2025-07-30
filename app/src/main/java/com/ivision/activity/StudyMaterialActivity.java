package com.ivision.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.NewsListAdapter;
import com.ivision.adapter.StudyMaterialListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.loadmore.OnLoadMoreListener;
import com.ivision.loadmore.RecyclerViewLoadMoreScroll;
import com.ivision.model.CommonModel;
import com.ivision.model.News;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMaterialActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private StudyMaterialListAdapter adapter;
    private ArrayList<CommonModel> list = new ArrayList<>();
    private int offset = 0, limit = 10;
    private RecyclerViewLoadMoreScroll scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = StudyMaterialActivity.this;

        adapter = new StudyMaterialListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                /*goToActivityForResult(new Intent(context, ProductDetailsActivity.class).putExtra("id", list.get(position).getId()), new BaseActivity.OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });*/
            }
        });

        setToolbar("Study Material");

        init();

        bindData(new JsonArray());
    }

    private void init() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewLoadMoreScroll(layoutManager);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                getList(true);
            }
        });
        binding.recyclerView.addOnScrollListener(scrollListener);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
//                getList(false);
            }
        });
    }

    private void getList(boolean loadMore) {

        if (loadMore) {
            adapter.addLoadingView();
        } else {
            Common.showProgressDialog(context, getString(R.string.please_wait));
            offset = 0;
            limit = 10;
            list.clear();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.studyMaterialListUrl).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (loadMore) {
                    adapter.removeLoadingView();
                } else {
                    Common.hideProgressDialog();
                }
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        JsonArray jsonArray = result.get("listArray").getAsJsonArray();
                        offset = offset + jsonArray.size();
                        bindData(jsonArray);
                        scrollListener.setLoaded();
                    } else {
                        if (loadMore) {
                            Common.showToast(jsonObject.get("message").getAsString());
                        } else {
                            list.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                if (loadMore) {
                    adapter.removeLoadingView();
                } else {
                    Common.hideProgressDialog();
                }
            }
        });
    }

    private void bindData(JsonArray jsonArray) {

        /*for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            News model = CommonParsing.bindNewsData(object);
            list.add(model);
        }*/

        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());
        list.add(new CommonModel());

        adapter.notifyDataSetChanged();
    }
}