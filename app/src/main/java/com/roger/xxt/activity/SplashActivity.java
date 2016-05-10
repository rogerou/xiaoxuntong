package com.roger.xxt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.roger.xxt.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


public class SplashActivity extends BaseActivity {


    @Bind(R.id.Appname)
    HTextView Appname;
    @Bind(R.id.content)
    HTextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initViews() {
//        Appname.setText("校讯通");
//        content.setText("实时掌握校园信息");
        Appname.setAnimateType(HTextViewType.LINE);
        content.setAnimateType(HTextViewType.TYPER);
        Appname.animateText("校讯通");
        content.animateText("实时掌握校园资讯");

    }

    @Override
    public void initData() {
        Observable.timer(3, TimeUnit.SECONDS)
                .map(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return isLogined();
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            toMain();
                        } else {
                            toLogin();
                        }
                    }
                });
    }


    public Boolean isLogined() {
        return !(AVUser.getCurrentUser() == null);
    }

    public void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
