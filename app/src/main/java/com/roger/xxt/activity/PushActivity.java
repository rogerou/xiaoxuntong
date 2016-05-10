package com.roger.xxt.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVUser;
import com.roger.xxt.R;
import com.roger.xxt.data.bean.Comment;
import com.roger.xxt.data.bean.Information;
import com.roger.xxt.data.module.RxBus;
import com.roger.xxt.data.module.RxLeanCloud;
import com.roger.xxt.event.AddInformationEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class PushActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title)
    TextInputLayout titlelayout;
    @Bind(R.id.tv_content)
    EditText tvContent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_push);
        super.onCreate(savedInstanceState);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.push_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.push) {
            commit();
        }


        return true;
    }

    private void commit() {

        String title = titlelayout.getEditText().getText().toString();
        String content = tvContent.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            showToast("请正确填写资讯");
            return;
        }
        Information info = new Information();
        info.setAuthor(AVUser.getCurrentUser());
        info.setContent(content);
        info.setTilte(title);
        RxLeanCloud.getInstance().CreateInformation(info)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog("正在发布...");
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Information>() {
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
                    public void onNext(Information information) {
                        RxBus.getDefaultInstance().post(new AddInformationEvent(information));
                        showToast("发布成功");
                        finish();
                    }
                });

    }
}