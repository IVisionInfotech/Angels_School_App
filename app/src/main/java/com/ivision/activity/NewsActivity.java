package com.ivision.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.NewsListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.loadmore.OnLoadMoreListener;
import com.ivision.loadmore.RecyclerViewLoadMoreScroll;
import com.ivision.model.News;
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

public class NewsActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private NewsListAdapter adapter;
    private ArrayList<News> list = new ArrayList<>();
    private int offset = 0, limit = 10;
    private String urlToDownload = "";
    private RecyclerViewLoadMoreScroll scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = NewsActivity.this;

        adapter = new NewsListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                /*goToActivityForResult(new Intent(context, ProductDetailsActivity.class).putExtra("id", list.get(position).getId()), new BaseActivity.OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });*/
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                if (list.get(position).getFile() != null || !list.get(position).getFile().isEmpty()) {
                    urlToDownload = list.get(position).getFile();
                    getPermission();
                }
            }
        });

        setToolbar("News");

        init();

        getList(false);
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
                getList(false);
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
        apiInterface.getList(Constant.newsListUrl).enqueue(new Callback<JsonObject>() {

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
                        JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
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

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            News model = CommonParsing.bindNewsData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }

    public void downloadFile() {
        String fileName = urlToDownload.substring(urlToDownload.lastIndexOf('/') + 1);

        Common.downloadFile(context, urlToDownload, fileName);
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            downloadFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadFile();
            } else {
                Common.showToast("Permission Denied");
            }
        }
    }

}