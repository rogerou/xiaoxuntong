package com.roger.xxt.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.roger.xxt.R;
import com.roger.xxt.adapter.HeaderViewCommentAdapter;
import com.roger.xxt.data.bean.Comment;
import com.roger.xxt.data.bean.Information;
import com.roger.xxt.data.module.RxBus;
import com.roger.xxt.data.module.RxLeanCloud;
import com.roger.xxt.event.AddCommentEvent;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

public class DetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.ry_comment)
    RecyclerView mRyComment;


    HeaderViewCommentAdapter mAdapter;

    List<Comment> mlist;

    Subscription mEventsubscripition;
    Subscription mSubscription;

    Information mInformation;

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRyComment.setLayoutManager(new LinearLayoutManager(this));
        mRyComment.setItemAnimator(new FadeInUpAnimator());
        mRyComment.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initData() {
        mlist = new ArrayList<>();
        mInformation = getIntent().getParcelableExtra("info");
        mAdapter = new HeaderViewCommentAdapter(mlist, mInformation, this);
        mRyComment.setAdapter(mAdapter);
        mEventsubscripition = getSubscribe();
        mSubscription = getData();

    }

    private Subscription getSubscribe() {
        return RxBus.getDefaultInstance()
                .toObservable(AddCommentEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AddCommentEvent>() {
                    @Override
                    public void call(AddCommentEvent addCommentEvent) {
                        if (addCommentEvent.getComment() != null && !mlist.contains(addCommentEvent.getComment())) {
                            mlist.add(0, addCommentEvent.getComment());
                            mAdapter.notifyItemInserted(0);
                        }
                    }
                });
    }

    private Subscription getData() {
        return RxLeanCloud.getInstance().getAllComment(mInformation)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSwipeLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeLayout.setRefreshing(true);
                            }
                        });
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Comment> list) {
                        if (list != null && list.size() != 0) {
                            mlist.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onRefresh() {
        mlist.clear();
        getData();
    }
}
