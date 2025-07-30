package com.ivision.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.databinding.ActivityLoginBinding;
import com.ivision.model.User;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;
import com.ivision.utils.RealmController;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private String username = "", password = "", playerId = "";
    private Realm realm;
    private boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = LoginActivity.this;

        RealmController.with(context).refresh();
        realm = RealmController.with(this).getRealm();

        OSDeviceState device = OneSignal.getDeviceState();
        playerId = device != null ? device.getUserId() : "";

        init();

        if (getIntent() != null) {
            if (getIntent().hasExtra("login")) {
                login = getIntent().getBooleanExtra("login", false);
            }
        }
    }

    private void init() {

        binding.ivOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivOff.setVisibility(View.GONE);
                binding.ivOn.setVisibility(View.VISIBLE);
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.etPassword.setSelection(binding.etPassword.getText().toString().length());
            }
        });
        binding.ivOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivOff.setVisibility(View.VISIBLE);
                binding.ivOn.setVisibility(View.GONE);
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.etPassword.setSelection(binding.etPassword.getText().toString().length());
            }
        });
        binding.ivOff.performClick();

        binding.llRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*goToActivityForResult(context, RegisterActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            goToMain();
                        }
                    }
                });*/
            }
        });
        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*goToActivityForResult(context, ForgotPasswordActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            session.setLoginStatus(true);
                            session.setLoginType("user");
                            goToActivity(context, MainActivity.class);
                            finish();
                        }
                    }
                });*/
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login) {
                    finish();
                } else {
                    goToActivity(context, MainActivity.class);
                    finish();
                }
            }
        });
    }

    private void goToMain() {
        session.setLoginStatus(true);
        if (login) {
            setResultOfActivity(1, true);
        } else {
            goToActivity(context, MainActivity.class);
            finish();
        }
    }

    private void validate() {

        username = binding.etContact.getText().toString();
        password = binding.etPassword.getText().toString();

        if (username.isEmpty()) {
            binding.etContact.setError("Enter contact");
            binding.etContact.requestFocus();
        } else if (password.isEmpty()) {
            binding.etPassword.setError("Enter password");
            binding.etPassword.requestFocus();
        } else {
            login();
        }
    }

    private void login() {

        Common.showProgressDialog(context, getString(R.string.please_wait));

        OSDeviceState device = OneSignal.getDeviceState();
        playerId = device != null ? device.getUserId() : "";

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.login(Constant.loginUrl, username, password, playerId).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());

                    if (jsonObject.get("status").getAsString().equals("1")) {
                        JsonArray result = jsonObject.getAsJsonArray("result");

                        for (int i = 0; i < result.size(); i++) {
                            JsonObject object = result.get(i).getAsJsonObject();

                            User user = new User();
                           
                            user = CommonParsing.bindUserData(object, user);

                            if (!updateFlag) {
                                realm.beginTransaction();
                                realm.copyToRealm(user);
                            }
                            realm.commitTransaction();
                        }

                        session.setLoginStatus(true);
                        session.setUsername(Common.encodeData(username));
                        session.setPassword(Common.encodeData(password));
                        goToMain();
                    } else if (jsonObject.get("status").getAsString().equals("0")) {
                        binding.etContact.setError(jsonObject.get("message").getAsString());
                    } else {
                        Common.showSnackBar(binding.btnSubmit, jsonObject.get("message").getAsString());
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
