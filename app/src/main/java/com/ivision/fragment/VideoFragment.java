package com.ivision.fragment;

import static com.ivision.utils.Common.ARG_PARAM1;
import static com.ivision.utils.Common.ARG_PARAM2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.activity.BaseActivity;
import com.ivision.activity.VideoDetailsActivity;
import com.ivision.adapter.SubjectListAdapter;
import com.ivision.adapter.VideoListAdapter;
import com.ivision.databinding.FragmentRecyclerviewBinding;
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

public class VideoFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private Context context;
    private FragmentRecyclerviewBinding binding;
    private ArrayList<Subject> list = new ArrayList<>();
    private VideoListAdapter adapter;
    private int offset = 0, limit = 10;
    private String subjectId = "";
    private RecyclerViewLoadMoreScroll scrollListener;
    private BaseActivity.OnActivityResultLauncher resultLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            resultLauncher.onActivityResultData(data, result.getResultCode());
        }
    });

    public VideoFragment() {
    }

    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecyclerviewBinding.inflate(inflater, container, false);

        context = getActivity();

        adapter = new VideoListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                goToActivityForResult(new Intent(context, VideoDetailsActivity.class).putExtra("videoId", list.get(position).getVidCode()), new BaseActivity.OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        getList(false);
                    }
                });
            }
        });

        init();

        getSubjectList();

        return binding.getRoot();
    }

    private void init() {
        StaggeredGridLayoutManager mLayoutManager1 = new StaggeredGridLayoutManager(1, 1);
        binding.recyclerView.setLayoutManager(mLayoutManager1);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
        scrollListener = new RecyclerViewLoadMoreScroll(mLayoutManager1);
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
                getSubjectList();
            }
        });
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
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.ivNotFound.setVisibility(View.VISIBLE);
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
                getList(false);
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
        getList(false);
    }

    private void getList(boolean loadMore) {

        if (!loadMore) {
            Common.showProgressDialog(context, context.getString(R.string.please_wait));
            offset = 0;
            limit = 10;
            list.clear();
        } else {
            adapter.addLoadingView();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.videoListUrl, Common.getStudentId(context), subjectId).enqueue(new Callback<JsonObject>() {

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
                        bindProductData(jsonArray);
                        scrollListener.setLoaded();
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.ivNotFound.setVisibility(View.GONE);
                    } else {
                        if (loadMore) {
                            Common.showToast(jsonObject.get("message").getAsString());
                        } else {
                            list.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            binding.ivNotFound.setVisibility(View.VISIBLE);
                            binding.recyclerView.setVisibility(View.GONE);
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

    private void bindProductData(JsonArray jsonArray) {

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Subject model = CommonParsing.bindSubjectData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }

    public void goToActivityForResult(Intent intent, BaseActivity.OnActivityResultLauncher resultLauncher) {
        activityResultLauncher.launch(intent);
        intent.putExtra("id", "id");
        intent.putExtra("title", "title");
        this.resultLauncher = resultLauncher;
    }
}