package com.ivision.activity;

import android.os.Bundle;
import android.view.View;

import com.ivision.databinding.ActivityViewImageBinding;
import com.ivision.utils.Common;

public class ViewImageActivity extends BaseActivity {

    ActivityViewImageBinding binding;
    String image, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ViewImageActivity.this;

        init();

        if (getIntent() != null) {
            if (getIntent().hasExtra("image")) {
                image = getIntent().getStringExtra("image");
                name = getIntent().getStringExtra("name");
            }
        }

        Common.loadImage(context, binding.viewImage, image);
        binding.tvname.setText(name);
    }

    private void init() {

        binding.cvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}