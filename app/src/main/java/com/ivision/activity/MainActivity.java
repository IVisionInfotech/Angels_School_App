package com.ivision.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.databinding.ActivityMainBinding;
import com.ivision.dialog.StudentBottomSheetDialog;
import com.ivision.fragment.HomeFragment;
import com.ivision.fragment.ProfileFragment;
import com.ivision.fragment.StudyMaterialFragment;
import com.ivision.fragment.VideoFragment;
import com.ivision.model.User;
import com.ivision.utils.BroadCastManager;
import com.ivision.utils.Common;
import com.ivision.utils.CommonAPI;
import com.ivision.utils.Constant;
import com.ivision.utils.RealmController;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements StudentBottomSheetDialog.ItemClickListener {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LocalReceiver localReceiver;
    private int itemId = R.id.navMenu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = MainActivity.this;

        if (getIntent() != null) {
            if (getIntent().hasExtra("action")) {
                String action = getIntent().getStringExtra("action");
                if (session.getLoginStatus()) {
                    if (action.equals("1")) {
                        goToActivityForResult(context, AttendanceActivity.class, new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {

                            }
                        });
                    } else if (action.equals("2")) {
                        goToActivityForResult(context, SchoolFeesActivity.class, new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {

                            }
                        });
                    } else if (action.equals("3")) {
                        goToActivityForResult(context, NewsActivity.class, new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {

                            }
                        });
                    } else if (action.equals("4")) {
                        goToActivityForResult(context, ResultActivity.class, new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {

                            }
                        });
                    } else if (action.equals("5")) {
                        goToActivityForResult(context, ReviewActivity.class, new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {

                            }
                        });
                    } else if (action.equals("6")) {
                        itemId = R.id.navMenu2;
                    } else if (action.equals("7")) {
                        /*goToActivityForResult(context, WalletActivity.class, new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {

                            }
                        });*/
                    }
                }
            }
        }

        init();

        Common.appUpdateDialog(context);

//        setToolbar(getString(R.string.app_name));

        bindCartData();
        if (session.getLoginStatus()) bindData(Common.getStudentId(context));

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.refreshCount);
            localReceiver = new LocalReceiver();
            BroadCastManager.getInstance().registerReceiver(this, localReceiver, filter);//Registered broadcast recipient
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        binding.tvNotificationCount.setSolidColor(getResources().getColor(R.color.colorAccent));
        binding.tvNotificationCount.setText("99");
        binding.tvNotificationCount.setVisibility(View.VISIBLE);

        binding.flStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.checkLogin(context)) {
                    StudentBottomSheetDialog dialog = new StudentBottomSheetDialog();
                    dialog.show(getSupportFragmentManager(), "");
                } else {
                    goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new OnActivityResultLauncher() {
                        @Override
                        public void onActivityResultData(Intent data, int resultCode) {
                            if (resultCode == Activity.RESULT_OK) {
                                StudentBottomSheetDialog dialog = new StudentBottomSheetDialog();
                                dialog.show(getSupportFragmentManager(), "");
                            }
                        }
                    });
                }
            }
        });

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        binding.bottomNavigationView.setSelectedItemId(itemId);

        binding.ivFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(context, NewsActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.frameContainer) instanceof HomeFragment)) {
            binding.bottomNavigationView.setSelectedItemId(R.id.navMenu1);
        } else {
            gotoBack();
        }
    }

    @Override
    protected void onDestroy() {
        BroadCastManager.getInstance().unregisterReceiver(this, localReceiver);//Logout broadcast recipient
        super.onDestroy();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llMenu1:
                    goToActivity(context, AboutUsActivity.class);
                    break;

                case R.id.llMenu2:
                    goToActivity(context, AttendanceActivity.class);
                    break;

                case R.id.llMenu3:
                    goToActivity(context, NewsActivity.class);
                    break;

                case R.id.llMenu4:
                    goToActivity(context, StudyMaterialActivity.class);
                    break;

                case R.id.llMenu5:
                    break;

                case R.id.llMenu6:
                    goToActivity(context, SchoolFeesActivity.class);
                    break;

                case R.id.llMenu7:
                    goToActivity(context, VideoActivity.class);
                    break;

                case R.id.llMenu8:
                    goToActivity(context, NotificationActivity.class);
                    break;

                case R.id.llMenu9:
                    goToActivity(context, GalleryActivity.class);
                    break;

                case R.id.llMenu10:
                    goToActivity(context, AdmissionInquiryActivity.class);
                    break;

                case R.id.llMenu11:
                    goToActivity(context, DownloadActivity.class);
                    break;

                case R.id.llMenu12:
                    goToActivity(context, ContactUsActivity.class);
                    break;

                case R.id.llMenu13:
//                    Common.openBrowser(context, getString(R.string.websiteUrl));
                    break;

            }
        }
    };

    private void selectFragment(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case R.id.navMenu2:
                    if (Common.checkLogin(context)) {
                        openFragment(StudyMaterialFragment.newInstance("", ""));
                    } else {
                        goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {
                                if (resultCode == Activity.RESULT_OK) {
                                    openFragment(StudyMaterialFragment.newInstance("", ""));
                                }
                            }
                        });
                    }
                    break;
                case R.id.navMenu3:
                    if (Common.checkLogin(context)) {
                        openFragment(VideoFragment.newInstance("", ""));
                    } else {
                        goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {
                                if (resultCode == Activity.RESULT_OK) {
                                    openFragment(VideoFragment.newInstance("", ""));
                                }
                            }
                        });
                    }
                    break;
                case R.id.navMenu4:
                    if (Common.checkLogin(context)) {
                        openFragment(ProfileFragment.newInstance("", ""));
                    } else {
                        goToActivityForResult(new Intent(context, LoginActivity.class).putExtra("login", true), new OnActivityResultLauncher() {
                            @Override
                            public void onActivityResultData(Intent data, int resultCode) {
                                if (resultCode == Activity.RESULT_OK) {
                                    openFragment(ProfileFragment.newInstance("", ""));
                                }
                            }
                        });
                    }
                    break;
                case R.id.navMenu1:
                default:
                    openFragment(HomeFragment.newInstance("", ""));
                    break;
            }
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void bindData(String id) {
        if (id.isEmpty()) {
            ArrayList<User> list = new ArrayList<>(RealmController.with(context).getAllUser());
            Common.setStudentId(context, String.valueOf(list.get(0).getId()));
        } else {
            Common.setStudentId(context, id);
        }
        User user = RealmController.with(context).getUser(Integer.parseInt(Common.getStudentId(context)));
        if (user != null) {
            binding.tvTitle.setText("Hi " + user.getName());
        }
    }

    private void bindCartData() {
        binding.flNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, NotificationActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (data != null) {
                                if (data.hasExtra("study")) {
                                    if (data.getIntExtra("study", 0) == 1) {
                                        itemId = R.id.navMenu2;
                                        binding.bottomNavigationView.setSelectedItemId(itemId);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
        binding.tvNotificationCount.setVisibility(View.GONE);
        /*if (Common.getCartCount(context) > 0) {
            binding.tvCartCount.setText(String.valueOf(Common.getCartCount(context)));
            binding.tvCartCount.setSolidColor(getResources().getColor(R.color.black));
            binding.tvCartCount.setVisibility(View.VISIBLE);
        }*/
    }

    private void getCounts() {
        /*FrameLayout flCart = findViewById(R.id.flCart);
        FrameLayout flNotification = findViewById(R.id.flNotification);
        flCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, CartListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        flNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, NotificationActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });*/
        CommonAPI.getCounts(context, new CommonAPI.OnResponseObject() {
            @Override
            public void OnResponse(JsonObject jsonObject) {
                /*CircularTextView tvCartCount = findViewById(R.id.tvCartCount);
                CircularTextView tvNotificationCount = findViewById(R.id.tvNotificationCount);
                tvCartCount.setVisibility(View.GONE);
                tvNotificationCount.setVisibility(View.GONE);
                tvCartCount.setText(jsonObject.get("cartCount").getAsString());
                tvNotificationCount.setText(jsonObject.get("notificationCount").getAsString());
                if (!jsonObject.get("cartCount").getAsString().isEmpty() && !jsonObject.get("cartCount").getAsString().equals("0")) {
                    tvCartCount.setSolidColor(getResources().getColor(R.color.colorAccent));
                    tvCartCount.setVisibility(View.VISIBLE);
                }
                if (!jsonObject.get("notificationCount").getAsString().isEmpty() && !jsonObject.get("notificationCount").getAsString().equals("0")) {
                    tvNotificationCount.setSolidColor(getResources().getColor(R.color.colorAccent));
                    tvNotificationCount.setVisibility(View.VISIBLE);
                }
                if (!jsonObject.get("quotationMainCount").getAsString().isEmpty() && !jsonObject.get("quotationMainCount").getAsString().equals("0")) {
                    Common.showBadge(context, bottomNavigationView, R.id.mQuotation, jsonObject.get("quotationMainCount").getAsString());
                }
                if (!jsonObject.get("orderMainCount").getAsString().isEmpty() && !jsonObject.get("orderMainCount").getAsString().equals("0")) {
                    Common.showBadge(context, bottomNavigationView, R.id.mOrder, jsonObject.get("orderMainCount").getAsString());
                }*/
            }
        });
    }

    @Override
    public void onItemClick(String model) {
        bindData(model);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Received the process after broadcast
            if (intent.hasExtra(Constant.refreshCount)) {
                if (intent.getStringExtra(Constant.refreshCount).equals(Constant.refreshCount)) {
                    bindCartData();
                }
            }
        }
    }
}