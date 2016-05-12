package com.roger.xxt.data.module;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.roger.xxt.data.bean.Comment;
import com.roger.xxt.data.bean.Information;
import com.roger.xxt.data.dao.CommentDao;
import com.roger.xxt.data.dao.InformationDao;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
public class RxLeanCloud {

    private static volatile RxLeanCloud mleancloud;

    private RxLeanCloud() {

    }


    public static RxLeanCloud getInstance() {
        if (mleancloud == null) {
            synchronized (RxLeanCloud.class) {
                if (mleancloud == null) {
                    mleancloud = new RxLeanCloud();
                }
            }
        }

        return mleancloud;
    }

    public Observable<AVUser> Login(final String username, final String passwd) {

        return Observable.create(new Observable.OnSubscribe<AVUser>() {
            @Override
            public void call(final Subscriber<? super AVUser> subscriber) {
                AVUser.logInInBackground(username, passwd, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            subscriber.onNext(avUser);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Boolean> Register(final String username, final String passwd) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                AVUser user = new AVUser();
                user.setUsername(username);
                user.setPassword(passwd);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(true);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });

            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Information> CreateInformation(final Information information) {
        information.setFetchWhenSave(true);
        return Observable.create(new Observable.OnSubscribe<Information>() {
            @Override
            public void call(final Subscriber<? super Information> subscriber) {
                information.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(information);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

    }


    public Observable<List<Information>> FetchALlInformation(final int size, final int page) {
        return Observable.create(new Observable.OnSubscribe<List<Information>>() {
            @Override
            public void call(final Subscriber<? super List<Information>> subscriber) {
                AVQuery<Information> query = AVObject.getQuery(Information.class);
                query.setLimit(size);
                query.setSkip(size * page);
                query.orderByDescending("createdAt");
                query.include(InformationDao.AUTHOR);
                query.findInBackground(new FindCallback<Information>() {
                    @Override
                    public void done(List<Information> list, AVException e) {
                        if (e == null) {
                            subscriber.onNext(list);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Comment> CommitComment(final Comment comment) {
        return Observable.create(new Observable.OnSubscribe<Comment>() {
            @Override
            public void call(final Subscriber<? super Comment> subscriber) {
                comment.setFetchWhenSave(true);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(comment);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Comment>> getAllComment(final Information information) {
        return Observable.create(new Observable.OnSubscribe<List<Comment>>() {
            @Override
            public void call(final Subscriber<? super List<Comment>> subscriber) {
                AVQuery<Comment> query = AVObject.getQuery(Comment.class);
                query.orderByDescending("createdAt");
                query.whereEqualTo(CommentDao.INFORMATION, information);
                query.include(CommentDao.AUTHOR);
                query.include(CommentDao.INFORMATION);
                query.findInBackground(new FindCallback<Comment>() {
                    @Override
                    public void done(List<Comment> list, AVException e) {
                        if (e == null) {
                            subscriber.onNext(list);

                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());


    }
}
