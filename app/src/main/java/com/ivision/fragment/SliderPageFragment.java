package com.ivision.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.ivision.activity.BaseActivity;
import com.ivision.databinding.ViewpagerSliderListItemBinding;
import com.ivision.utils.Common;

import static com.ivision.utils.Constant.ARG_PARAM1;
import static com.ivision.utils.Constant.ARG_PARAM2;

public class SliderPageFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private Context context;
    private ViewpagerSliderListItemBinding binding;
    private BaseActivity.OnActivityResultLauncher resultLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            resultLauncher.onActivityResultData(data, result.getResultCode());
        }
    });

    public SliderPageFragment() {
    }

    public static SliderPageFragment newInstance(String param1, String param2) {
        SliderPageFragment fragment = new SliderPageFragment();
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
        binding = ViewpagerSliderListItemBinding.inflate(inflater, container, false);

        context = getActivity();

        init();

        return binding.getRoot();
    }

    private void init() {
        binding.tvTitle.setText(mParam1);
        Common.loadImage(context, binding.ivImage, mParam2);
    }

    public void goToActivityForResult(Intent intent, BaseActivity.OnActivityResultLauncher resultLauncher) {
        activityResultLauncher.launch(intent);
        intent.putExtra("id", "id");
        intent.putExtra("title", "title");
        this.resultLauncher = resultLauncher;
    }
}
