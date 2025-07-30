package com.ivision.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.ivision.databinding.ActivitySchoolFeesBinding;

public class SchoolFeesActivity extends BaseActivity {

    private ActivitySchoolFeesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchoolFeesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = SchoolFeesActivity.this;

        setToolbar("School Fees");

        init();
    }

    private void init() {

    }
}