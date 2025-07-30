package com.ivision.fragment;

import static com.ivision.utils.Constant.ARG_PARAM1;
import static com.ivision.utils.Constant.ARG_PARAM2;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ivision.R;
import com.ivision.activity.BaseActivity;
import com.ivision.adapter.StudentsListAdapter;
import com.ivision.databinding.FragmentProfileBinding;
import com.ivision.model.User;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.RealmController;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private Context context;
    private FragmentProfileBinding binding;
    private String sId = "";
    private BaseActivity.OnActivityResultLauncher resultLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            resultLauncher.onActivityResultData(data, result.getResultCode());
        }
    });

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        context = getActivity();

        init();

        bindRecyclerView();

        return binding.getRoot();
    }

    private void init() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.confirmationDialog(context, context.getString(R.string.confirmation_logout), "No", "Yes", new Runnable() {
                    @Override
                    public void run() {
                        Common.logout(context, "");
                    }
                });
            }
        });
    }

    private void bindRecyclerView() {

        ArrayList<User> list = new ArrayList<>(RealmController.with(context).getAllUser());

        if (Common.getStudentId(context).isEmpty()) {
            sId = String.valueOf(list.get(0).getId());
        } else {
            sId = Common.getStudentId(context);
        }
        if (sId.isEmpty()) {
            bindData(null);
        } else {
            bindData(RealmController.with(context).getUser(Integer.parseInt(sId)));
        }

        StudentsListAdapter adapter = new StudentsListAdapter(context, list, sId, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                bindData(list.get(position));
            }
        });

        StaggeredGridLayoutManager mLayoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(mLayoutManager1);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
    }

    private void bindData(User model) {
        if (model == null) {
            ArrayList<User> list = new ArrayList<>(RealmController.with(context).getAllUser());
            Common.setStudentId(context, String.valueOf(list.get(0).getId()));
        } else {
            Common.setStudentId(context, String.valueOf(model.getId()));
        }

        Common.loadImage(context, binding.ivImage, model.getImage());
        binding.tvName.setText(model.getName());
        binding.tvRollNo.setText(String.valueOf(model.getRollNo()));
        binding.tvContact.setText(model.getContact());
    }

    public void goToActivityForResult(Intent intent, BaseActivity.OnActivityResultLauncher resultLauncher) {
        activityResultLauncher.launch(intent);
        intent.putExtra("id", "id");
        intent.putExtra("title", "title");
        this.resultLauncher = resultLauncher;
    }
}