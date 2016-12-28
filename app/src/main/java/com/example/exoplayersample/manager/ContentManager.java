package com.example.exoplayersample.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.exoplayersample.App;


/**
 * Created by 龙泉 on 2016/12/18.
 */

public class ContentManager {

    private static ContentManager mContentManager;
    private SharedPreferences mShardPreferences;

    private static final String KEY_BIT_RATE = "key_bit_rate";


    public static ContentManager getInstance() {
        if (mContentManager == null) {
            mContentManager = new ContentManager();
        }
        return mContentManager;
    }

    public ContentManager() {
        String spName = App.getInstance().getPackageName() + "_preferences";
        mShardPreferences = App.getInstance().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = mShardPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mShardPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mShardPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = mShardPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

}
