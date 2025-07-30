package com.ivision.activity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.databinding.ActivityContactUsBinding;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity {

    private ActivityContactUsBinding binding;
    private String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ContactUsActivity.this;

        setToolbar("Contact Us");

        getContactUs();

        binding.llWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openWhatsApp(context, "9924426361", "");
            }
        });
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.school_name)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            }
        });
    }

    private void getContactUs() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.contactUsUrl).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("1")) {
                        JsonArray result = jsonObject.get("result").getAsJsonArray();
                        if (!result.isEmpty()) {
                            JsonObject object = result.get(0).getAsJsonObject();
                            bindData(object);
                        }
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

    private void bindData(JsonObject object) {
        if (object.has("address")) binding.tvAddress.setText(object.get("address").getAsString());
        if (object.has("contact")) binding.tvContact.setText(object.get("contact").getAsString());
        if (object.has("email")) binding.tvEmail.setText(object.get("email").getAsString());
        if (object.has("website")) binding.tvWebsite.setText(object.get("website").getAsString());
        if (object.has("latitude")) latitude = (object.get("latitude").getAsString());
        if (object.has("longitude")) longitude = (object.get("longitude").getAsString());

        binding.llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.dialNumber(context, object.get("contact").getAsString());
            }
        });
        binding.llWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openWhatsApp(context, object.get("contact").getAsString(), "");
            }
        });
        binding.llEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.sendEmail(context, object.get("email").getAsString());
            }
        });
        binding.llWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openBrowser(context, object.get("website").getAsString());
            }
        });

        if (!latitude.isEmpty() && !longitude.isEmpty()) {
            init();
        }
    }
}