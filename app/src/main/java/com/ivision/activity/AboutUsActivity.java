package com.ivision.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.databinding.ActivityAboutUsBinding;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends BaseActivity {

    private ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = AboutUsActivity.this;

        setToolbar("About Us");

        getAboutUs();
    }

    private void bindData(String about) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvAboutUs.setText(Html.fromHtml(about, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.tvAboutUs.setText(Html.fromHtml(about));
        }
    }

    private void getAboutUs() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.aboutUsUrl).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("1")) {
                        bindData(jsonObject.get("about").getAsString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Common.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Common.hideProgressDialog();
            }
        });
    }
}