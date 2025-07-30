package com.ivision.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.VideoListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.loadmore.OnLoadMoreListener;
import com.ivision.loadmore.RecyclerViewLoadMoreScroll;
import com.ivision.model.Subject;
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

public class VideoActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private VideoListAdapter adapter;
    private ArrayList<Subject> list = new ArrayList<>();
    private int offset = 0, limit = 10;
    private RecyclerViewLoadMoreScroll scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = VideoActivity.this;

        adapter = new VideoListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                /*goToActivityForResult(new Intent(context, ProductDetailsActivity.class).putExtra("id", list.get(position).getId()), new BaseActivity.OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });*/
            }
        });

        setToolbar("Video");

        init();

        bindData(new JsonArray());
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
//                getList(false);
            }
        });
    }

    private void bindData(JsonArray jsonArray) {

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Subject model = CommonParsing.bindSubjectData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }
}