package com.roger.xxt;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.roger.xxt.data.bean.Comment;
import com.roger.xxt.data.bean.Information;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
public class XXTApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVObject.registerSubclass(Comment.class);
        AVObject.registerSubclass(Information.class);
        AVOSCloud.initialize(this, "DiG0fsAkft8l0f5jcuDnp5mn-gzGzoHsz", "kxFJ5IWqQybSNJQtB61GGC80");
        AVAnalytics.enableCrashReport(this, true);
        Logger.init("校讯通").setLogLevel(LogLevel.FULL);
    }
}
