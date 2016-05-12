package com.roger.xxt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.roger.xxt.R;
import com.roger.xxt.data.bean.Comment;
import com.roger.xxt.data.bean.Information;
import com.roger.xxt.data.module.RxBus;
import com.roger.xxt.data.module.RxLeanCloud;
import com.roger.xxt.event.AddCommentEvent;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by YX201603-6 on 2016/5/12.
 */
public class CommitCommentActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_comment)
    EditText mEtComment;
    Information mInformationid;
    Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_commit_comment);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_commit) {
            Comment com = new Comment();
            com.setAuthor(AVUser.getCurrentUser());
            com.setComment(mEtComment.getText().toString());
            com.setInformation(mInformationid);
            com.setUsername(AVUser.getCurrentUser().getUsername());
            mSubscription = RxLeanCloud.getInstance().CommitComment(com)
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            showDialog("正在提交...");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Comment>() {
                        @Override
                        public void onCompleted() {
                            dismissDialog();
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissDialog();
                            showToast("出错啦 ：" + e.getMessage());
                        }

                        @Override
                        public void onNext(Comment comment) {
                            RxBus.getDefaultInstance().post(new AddCommentEvent(comment));
                            showToast("评论成功");
                        }
                    });
        }
        return true;
    }

    @Override
    public void initData() {
        mInformationid = getIntent().getParcelableExtra("info");
        mTvTitle.setText(mInformationid.getTilte());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
