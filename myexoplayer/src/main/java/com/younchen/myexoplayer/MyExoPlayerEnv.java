package com.younchen.myexoplayer;

import android.content.Context;

import com.younchen.myexoplayer.util.Singleton;

/**
 * Created by 龙泉 on 2016/12/28.
 */

public class MyExoPlayerEnv {

    private Context mContext;

    private static Singleton<MyExoPlayerEnv> sInstance = new Singleton<MyExoPlayerEnv>() {
        @Override
        protected MyExoPlayerEnv create() {
            return new MyExoPlayerEnv();
        }
    };

    public static void init(Context context) {
        final MyExoPlayerEnv playerEnv = sInstance.get();
        if (null != playerEnv) {
            playerEnv.setContext(context);
        }
    }


    private void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return getIns().mContext;
    }

    public static MyExoPlayerEnv getIns() {
        return sInstance.get();
    }
}
