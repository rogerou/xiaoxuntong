package com.roger.xxt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.roger.xxt.R;
import com.roger.xxt.data.module.RxLeanCloud;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
public class RegisterActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.Appname)
    TextView Appname;
    @Bind(R.id.username)
    TextInputLayout username;
    @Bind(R.id.passwd)
    TextInputLayout passwd;
    @Bind(R.id.repeatasswd)
    TextInputLayout repeatasswd;
    @Bind(R.id.register_button)
    Button registerButton;
    @Bind(R.id.email_login_form)
    LinearLayout emailLoginForm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.register_button)
    public void onClick() {

        String usrname = username.getEditText().getText().toString();
        String pass = passwd.getEditText().getText().toString();
        String repeatpasswd = repeatasswd.getEditText().getText().toString();

        if (TextUtils.isEmpty(usrname) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repeatpasswd)) {
            showToast("请正确输入账号密码");
            return;
        }
        if (!pass.equals(repeatpasswd)) {
            showToast("两段密码不一致");
            return;
        }
        RxLeanCloud.getInstance().Register(usrname, pass)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog("正在注册中...");
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Logger.e("注册成功");
                            finish();
                        }
                    }
                });
    }
}
