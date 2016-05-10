package com.roger.xxt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.roger.xxt.R;
import com.roger.xxt.adapter.xxtAdapter;
import com.roger.xxt.data.bean.Information;
import com.roger.xxt.data.module.RxBus;
import com.roger.xxt.data.module.RxLeanCloud;
import com.roger.xxt.event.AddInformationEvent;
import com.roger.xxt.uti.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ry_xxt)
    RecyclerView ryXxt;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    xxtAdapter mAdapter;
    int size = 20, page = 0;
    List<Information> mlist;

    boolean isFirst = true;
    LinearLayoutManager layoutManager;
    Subscription mSubscription;

    @Override
    public void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }


        });
        layoutManager = new LinearLayoutManager(this);
        ryXxt.setLayoutManager(layoutManager);
        ryXxt.setItemAnimator(new FadeInUpAnimator());
        ryXxt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && layoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {
                    swipeLayout.setRefreshing(true);
                    page++;
                    initData();
                }
            }
        });
        swipeLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                
            }
        });
    }

    @Override
    public void initData() {
        mSubscription = RxBus.getDefaultInstance().toObservable(AddInformationEvent.class)
                .subscribe(new Action1<AddInformationEvent>() {
                    @Override
                    public void call(AddInformationEvent addInformationEvent) {
                        if (addInformationEvent.getInformation() != null && !mlist.contains(addInformationEvent.getInformation())) {
                            mlist.add(0, addInformationEvent.getInformation());
                            mAdapter.notifyItemInserted(0);
                        }
                    }
                });
        if (isFirst) {
            mlist = new ArrayList<>();
            mAdapter = new xxtAdapter(mlist);
            ryXxt.setAdapter(mAdapter);
            isFirst = false;
        }
        RxLeanCloud.getInstance().FetchALlInformation(size, page)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        swipeLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeLayout.setRefreshing(true);
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Information>>() {
                    @Override
                    public void onCompleted() {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Information> list) {
                        mlist.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void logout() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("真的要退出吗？")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        AVUser.logOut();
                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                        finish();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
        dialog.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


    }


    @OnClick(R.id.fab)
    public void onClick() {
        startActivity(new Intent(MainActivity.this, PushActivity.class));
    }

    @Override
    public void onRefresh() {
        page = 0;
        mlist.clear();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
