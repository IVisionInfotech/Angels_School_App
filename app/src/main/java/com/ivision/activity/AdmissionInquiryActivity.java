package com.ivision.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.JsonObject;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.ivision.R;
import com.ivision.databinding.ActivityAdmissionInquiryBinding;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdmissionInquiryActivity extends BaseActivity {

    private ActivityAdmissionInquiryBinding binding;
    private String fName, mName, lName, lstStudy, admission, percentage, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdmissionInquiryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = AdmissionInquiryActivity.this;

        setToolbar("Admission Inquiry");

        init();

        bindSpinner();
    }

    private void init() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void bindSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Play Group");
        list.add("Jr K.G.");
        list.add("Sr K.G");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item, list);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                lstStudy = itemAtPosition;
            }
        });
    }

    private void validate() {
        fName = binding.etFName.getText().toString();
        mName = binding.etMName.getText().toString();
        lName = binding.etLName.getText().toString();
        admission = binding.etLastSchool.getText().toString();
        percentage = binding.etPercentage.getText().toString();
        mobile = binding.etContact.getText().toString();

        if (fName.isEmpty()) {
            binding.etFName.setError("Enter name");
        } else if (mName.isEmpty()) {
            binding.etMName.setError("Enter father name");
        } else if (lName.isEmpty()) {
            binding.etLName.setError("Enter last name");
        } else if (lstStudy.isEmpty()) {
            Common.showToast("Select last standard");
        } else if (admission.isEmpty()) {
            binding.etLastSchool.setError("Enter last school");
        } else if (percentage.isEmpty()) {
            binding.etPercentage.setError("Enter percentage");
        } else if (mobile.isEmpty()) {
            binding.etContact.setError("Enter mobile no");
        } else {
            admissionInquiry();
        }
    }

    private void admissionInquiry() {

        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.admissionInquiry(Constant.admissionInquiryUrl, fName, mName, lName, lstStudy, admission, percentage, mobile).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());

                    if (jsonObject.get("status").getAsString().equals("1")) {
                        finish();
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