package com.ivision.fragment;

import static com.ivision.utils.Common.ARG_PARAM1;
import static com.ivision.utils.Common.ARG_PARAM2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.activity.BaseActivity;
import com.ivision.adapter.StudyMaterialListAdapter;
import com.ivision.databinding.FragmentRecyclerviewBinding;
import com.ivision.loadmore.OnLoadMoreListener;
import com.ivision.loadmore.RecyclerViewLoadMoreScroll;
import com.ivision.model.CommonModel;
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

public class StudyMaterialFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private Context context;
    private FragmentRecyclerviewBinding binding;
    private ArrayList<CommonModel> list = new ArrayList<>();
    private StudyMaterialListAdapter adapter;
    private int offset = 0, limit = 10;
    private String urlToDownload = "";
    private RecyclerViewLoadMoreScroll scrollListener;
    private BaseActivity.OnActivityResultLauncher resultLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            resultLauncher.onActivityResultData(data, result.getResultCode());
        }
    });

    public StudyMaterialFragment() {
    }

    public static StudyMaterialFragment newInstance(String param1, String param2) {
        StudyMaterialFragment fragment = new StudyMaterialFragment();
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

        adapter = new StudyMaterialListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                urlToDownload = list.get(position).getFile();
                getPermission();
            }
        });

        init();

        getList(false);

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
                getList(false);
            }
        });
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
        apiInterface.getList(Constant.studyMaterialListUrl, Common.getStudentId(context)).enqueue(new Callback<JsonObject>() {

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

    private void bindData(JsonArray jsonArray) {

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            CommonModel model = CommonParsing.bindCommonData(object);
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
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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


    public void goToActivityForResult(Intent intent, BaseActivity.OnActivityResultLauncher resultLauncher) {
        activityResultLauncher.launch(intent);
        intent.putExtra("id", "id");
        intent.putExtra("title", "title");
        this.resultLauncher = resultLauncher;
    }
}