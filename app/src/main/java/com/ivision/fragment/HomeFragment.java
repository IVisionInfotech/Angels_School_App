package com.ivision.fragment;

import static com.ivision.utils.Constant.ARG_PARAM1;
import static com.ivision.utils.Constant.ARG_PARAM2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.activity.AboutUsActivity;
import com.ivision.activity.AdmissionInquiryActivity;
import com.ivision.activity.AttendanceActivity;
import com.ivision.activity.BaseActivity;
import com.ivision.activity.ContactUsActivity;
import com.ivision.activity.DownloadActivity;
import com.ivision.activity.GalleryActivity;
import com.ivision.activity.LoginActivity;
import com.ivision.activity.ManagementActivity;
import com.ivision.activity.NewsActivity;
import com.ivision.activity.ResultActivity;
import com.ivision.activity.ReviewActivity;
import com.ivision.activity.SchoolFeesActivity;
import com.ivision.activity.WebViewActivity;
import com.ivision.adapter.ViewPagerAdapter;
import com.ivision.databinding.FragmentHomeBinding;
import com.ivision.model.Image;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.Constant;
import com.ivision.utils.RealmController;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private Context context;
    private FragmentHomeBinding binding;
    private ArrayList<String> sliderList = new ArrayList();
    private Realm realm;
    private BaseActivity.OnActivityResultLauncher resultLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            resultLauncher.onActivityResultData(data, result.getResultCode());
        }
    });

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        context = getActivity();

        RealmController.with(context).refresh();
        realm = RealmController.with(this).getRealm();

        init();

        getHomeScreenList();

        return binding.getRoot();
    }

    private void init() {
        binding.cvFees.setOnClickListener(clickListener);
        binding.cvAboutUs.setOnClickListener(clickListener);
        binding.cvManagement.setOnClickListener(clickListener);
        binding.cvNews.setOnClickListener(clickListener);
        binding.cvGallery.setOnClickListener(clickListener);
        binding.cvAdmissionInquiry.setOnClickListener(clickListener);
        binding.cvDownload.setOnClickListener(clickListener);
        binding.cvContact.setOnClickListener(clickListener);
        binding.cvERP.setOnClickListener(clickListener);
        binding.cvOnlineTest.setOnClickListener(clickListener);
        binding.cvWebsite.setOnClickListener(clickListener);
        binding.cvAttendance.setOnClickListener(clickListener);
        binding.cvTrackVehicle.setOnClickListener(clickListener);
        binding.cvReview.setOnClickListener(clickListener);
        binding.cvResult.setOnClickListener(clickListener);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                getHomeScreenList();
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cvFees:
                    goToActivity(new Intent(context, SchoolFeesActivity.class));
                    break;
                case R.id.cvAboutUs:
                    goToActivity(new Intent(context, AboutUsActivity.class));
                    break;
                case R.id.cvManagement:
                    goToActivity(new Intent(context, ManagementActivity.class));
                    break;
                case R.id.cvNews:
                    goToActivity(new Intent(context, NewsActivity.class));
                    break;
                case R.id.cvGallery:
                    goToActivity(new Intent(context, GalleryActivity.class));
                    break;
                case R.id.cvAdmissionInquiry:
                    goToActivity(new Intent(context, AdmissionInquiryActivity.class));
                    break;
                case R.id.cvDownload:
                    goToActivity(new Intent(context, DownloadActivity.class));
                    break;
                case R.id.cvContact:
                    goToActivity(new Intent(context, ContactUsActivity.class));
                    break;
                case R.id.cvERP:
                    getLinks(2);
                    break;
                case R.id.cvOnlineTest:
                    getLinks(3);
                    break;
                case R.id.cvWebsite:
                    getLinks(1);
                    break;
                case R.id.cvReview:
                    if (Common.checkLogin(context)) {
                        goToActivity(new Intent(context, ReviewActivity.class));
                    } else {
                        goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new BaseActivity.OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {
                                if (resultCode == Activity.RESULT_OK) {
                                    goToActivity(new Intent(context, ReviewActivity.class));
                                }
                            }
                        });
                    }
                    break;
                case R.id.cvResult:
                    if (Common.checkLogin(context)) {
                        goToActivity(new Intent(context, ResultActivity.class));
                    } else {
                        goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new BaseActivity.OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {
                                if (resultCode == Activity.RESULT_OK) {
                                    goToActivity(new Intent(context, ResultActivity.class));
                                }
                            }
                        });
                    }
                    break;
                case R.id.cvAttendance:
                    if (Common.checkLogin(context)) {
                        goToActivity(new Intent(context, AttendanceActivity.class));
                    } else {
                        goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new BaseActivity.OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {
                                if (resultCode == Activity.RESULT_OK) {
                                    goToActivity(new Intent(context, AttendanceActivity.class));
                                }
                            }
                        });
                    }
                    break;
                case R.id.cvTrackVehicle:
                    if (Common.checkLogin(context)) {
//                        getTracking();
                        goToActivity(new Intent(context, WebViewActivity.class).putExtra("url", Constant.bannerUrl + Constant.vehicleTrackUrl));
                    } else {
                        goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new BaseActivity.OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {
                                if (resultCode == Activity.RESULT_OK) {
//                                    getTracking();
                                    goToActivity(new Intent(context, WebViewActivity.class).putExtra("url", Constant.bannerUrl + Constant.vehicleTrackUrl));
                                }
                            }
                        });
                    }
                    break;
            }
        }
    };

    private void getLinks(int type) {
        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.linksUrl).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.has("result")) {
                        JsonArray result = jsonObject.get("result").getAsJsonArray();
                        if (!result.isEmpty()) {
                            JsonObject object = result.get(0).getAsJsonObject();
                            bindData(object, type);
                        }
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

    private void bindData(JsonObject object, int type) {
        if (type == 1 && object.has("website"))
            Common.openBrowser(context, object.get("website").getAsString());

        if (type == 2 && object.has("erp"))
            Common.openBrowser(context, object.get("erp").getAsString());

        if (type == 3 && object.has("onlinetest"))
            Common.openBrowser(context, object.get("onlinetest").getAsString());
    }

    private void getTracking() {
        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.vehicleTrackUrl, Common.getStudentId(context)).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("1")) {
                        JsonArray result = jsonObject.get("result").getAsJsonArray();
                        if (!result.isEmpty()) {
                            JsonObject object = result.get(0).getAsJsonObject();
//                            bindTrackingData(object);
                        } else {
                            Common.showToast(jsonObject.get("message").getAsString());
                        }
                    } else {
                        Common.showToast(jsonObject.get("message").getAsString());
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

    private void bindTrackingData(JsonObject object) {
        double latitude = 0, longitude = 0;
        if (object.has("latitude"))
            latitude = Double.parseDouble(object.get("latitude").getAsString());
        if (object.has("longitude"))
            longitude = Double.parseDouble(object.get("longitude").getAsString());

        try {
            String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + context.getString(R.string.app_name) + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } catch (Exception e) {
            String uri = String.format(Locale.getDefault(), "geo:%f,%f", Float.parseFloat(String.valueOf(latitude)), Float.parseFloat(String.valueOf(longitude)));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }

    private void getHomeScreenList() {

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.bannerUrl).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        JsonArray result = jsonObject.get("result").getAsJsonArray();
                        bindSliderData(result);
                        saveSliderData(result);
                    } else {
                        bindSliderData(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bindSliderData(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                bindSliderData(null);
            }
        });
    }

    protected void bindSliderData(JsonArray jsonArray) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        if (jsonArray != null) {
            if (!jsonArray.isEmpty()) {
                sliderList.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                    sliderList.add(object.get("banner1").getAsString());
                    sliderList.add(object.get("banner2").getAsString());
                    sliderList.add(object.get("banner3").getAsString());
                }
            } else {
                ArrayList<Image> list = new ArrayList<>(RealmController.with(context).getAllSliderImages());
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        sliderList.add(list.get(i).getImage());
                    }
                }
            }
        } else {
            ArrayList<Image> list = new ArrayList<>(RealmController.with(context).getAllSliderImages());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    sliderList.add(list.get(i).getImage());
                }
            }
        }

        if (!sliderList.isEmpty()) {
            for (int i = 0; i < sliderList.size(); i++) {
                adapter.addFragment(SliderPageFragment.newInstance("", sliderList.get(i)), "");
            }
        }

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.viewPager.setPageTransformer(true, new DepthPageTransformer());
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TheSliderTimer(), 5000, 6000);
        binding.tabLayout.setupWithViewPager(binding.viewPager, true);
    }

    protected void saveSliderData(JsonArray jsonArray) {
        if (!jsonArray.isEmpty()) {
            RealmController.with(context).clearAllSliderImages();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();

                Image image = new Image(object.get("banner1").getAsString());
                Image image2 = new Image(object.get("banner2").getAsString());
                Image image3 = new Image(object.get("banner3").getAsString());

                realm.beginTransaction();
                realm.copyToRealm(image);
                realm.copyToRealm(image2);
                realm.copyToRealm(image3);
                realm.commitTransaction();
            }
        }
    }

    public class TheSliderTimer extends TimerTask {
        @Override
        public void run() {
            requireActivity().runOnUiThread(() -> {
                if (binding.viewPager.getCurrentItem() < sliderList.size() - 1)
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                else
                    binding.viewPager.setCurrentItem(0);
            });
        }
    }

    public void goToActivity(Intent intent) {
        context.startActivity(intent);
    }

    public void goToActivityForResult(Intent intent, BaseActivity.OnActivityResultLauncher resultLauncher) {
        activityResultLauncher.launch(intent);
        this.resultLauncher = resultLauncher;
    }

    @RequiresApi(21)
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                // Move it behind the left page
                view.setTranslationZ(-1f);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}