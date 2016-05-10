package com.roger.xxt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.roger.xxt.R;
import com.roger.xxt.data.module.RxLeanCloud;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class LoginActivity extends BaseActivity {


    @Bind(R.id.Appname)
    TextView Appname;
    @Bind(R.id.username)
    TextInputLayout username;
    @Bind(R.id.passwd)
    TextInputLayout passwd;
    @Bind(R.id.sign_in_button)
    Button signInButton;
    @Bind(R.id.register_button)
    Button registerButton;
    @Bind(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initViews() {
        setSupportActionBar(toolbar);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.sign_in_button, R.id.register_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                Login();
                break;
            case R.id.register_button:
                toRegister();

                break;

            default:
                break;

        }
    }

    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void Login() {
        String usrname = username.getEditText().getText().toString();
        String pass = passwd.getEditText().getText().toString();
        if (TextUtils.isEmpty(usrname) || TextUtils.isEmpty(pass)) {
            Toast.makeText(LoginActivity.this, "请正确填写你的帐号密码", Toast.LENGTH_SHORT).show();
            return;
        }
        RxLeanCloud.getInstance().Login(usrname, pass)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog("正在登录中...");
                    }
                })
                .subscribe(new Observer<AVUser>() {
                    @Override
                    public void onCompleted() {
                        dismissDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        dismissDialog();
                    }

                    @Override
                    public void onNext(AVUser avUser) {
                        showToast("登录成功");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }
}

