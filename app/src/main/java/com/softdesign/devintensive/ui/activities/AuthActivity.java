package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "AuthActivity";
    private DataManager mDataManager;
    @BindView(R.id.login_button)
    Button mSignIn;
    @BindView(R.id.et_login_email)
    EditText mLoginEmail;
    @BindView(R.id.et_login_password)
    EditText mLoginPassword;
    @BindView(R.id.remembe_txt)
    TextView mRemembePassword;
    @BindView(R.id.main_coordinator_conteiner)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.main_coordinator_conteiner_image)
    ImageView mCoordinatorLayoutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        mRemembePassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        switch (v.getId()) {
            case R.id.login_button:
                signIn();
                break;
            case R.id.remembe_txt:
                remembePassword();
                break;
        }
    }

    private void showSnackBar(String message) {
        Log.d(TAG, "showSnackBar");
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void remembePassword() {
        Log.d(TAG, "remembePassword");
        Intent remembeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(remembeIntent);
    }

    private void loginSuccess(UserModelRes userModelRes) {
        Log.d(TAG, "loginSuccess");
        showSnackBar(userModelRes.getData().getToken());
        mDataManager.getPreferenceManager().saveAuthToken(userModelRes.getData().getToken());
        mDataManager.getPreferenceManager().saveUserId(userModelRes.getData().getUser().getId());
        saveUserValues(userModelRes);
        saveUserContacts(userModelRes);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLoginEmail.getText().toString(), mLoginPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else {
                        if (response.code() == 404) {
                            showSnackBar("неверный логин или пароль");
                        } else {
                            showSnackBar("Все пропало!");
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    showSnackBar(t.getMessage());
                }
            });
        } else {
            showSnackBar("Сеть недоступна");
        }
    }
    private void saveUserValues(UserModelRes userModelRes){
        Log.d(TAG, "saveUserValues");
        int[] userValues={
                userModelRes.getData().getUser().getProfileValues().getRaiting(),
                userModelRes.getData().getUser().getProfileValues().getLinesCode(),
                userModelRes.getData().getUser().getProfileValues().getProjects(),
        };
        mDataManager.getPreferenceManager().saveUserProfileValues(userValues);
    }
    private void saveUserContacts(UserModelRes userModelRes){
        Log.d(TAG, "saveUserContacts");
        mDataManager.getPreferenceManager().saveUserPhoto(Uri.parse(userModelRes.getData().getUser().getPublicInfo().getPhoto()));
        mDataManager.getPreferenceManager().saveUserAvatar(Uri.parse(userModelRes.getData().getUser().getPublicInfo().getAvatar()));
        List<String> list=new ArrayList<>();
        list.add(userModelRes.getData().getUser().getContacts().getPhone());
        list.add(userModelRes.getData().getUser().getContacts().getEmail());
        list.add(userModelRes.getData().getUser().getContacts().getVk().replaceFirst("https://",""));
        list.add(userModelRes.getData().getUser().getRepositories().getRepo().get(0).getGit().replaceFirst("https://",""));
        list.add(userModelRes.getData().getUser().getPublicInfo().getBio());
        mDataManager.getPreferenceManager().saveUserProfileData(list);
    }
}

