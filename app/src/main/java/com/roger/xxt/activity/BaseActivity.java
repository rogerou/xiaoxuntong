package com.roger.xxt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.roger.xxt.R;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
public abstract class BaseActivity extends AppCompatActivity {

    SweetAlertDialog mDialog;


    public abstract void initViews();

    public abstract void initData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initViews();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtil.setColor(this, R.color.colorPrimary);
    }

    void showDialog(String content) {
        if (mDialog == null) {
            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(content);

        }
        mDialog.show();

    }

    public void showToast(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismissWithAnimation();
        }
    }
}
