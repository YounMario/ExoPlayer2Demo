package com.example.exoplayersample.video.utils;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */

public class TimeUtils {

    public static String timeToString(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        int minutes = (int) totalSeconds / 60;
        int seconds = (int) totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

}
