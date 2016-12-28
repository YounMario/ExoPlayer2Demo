package com.example.exoplayersample.video.utils;

/**
 * Created by 龙泉 on 2016/11/24.
 */

public class ClipHelper {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_TOP = 1;
    public static final int TYPE_MARING_TOP = 2;
    public static final int TYPE_CENTER = 3;
    public static final int TYPE_MARGIN_BOTOTM = 4;
    public static final int TYPE_BOTTOM = 5;

    public static float getTranslateOffset(int positionType, int contentW, int contentH, int viewW, int viewH) {
        float offset;

        float base = getContentRatio(contentW, viewW);
        switch (positionType) {
            case TYPE_TOP:
                offset = (contentH * base * 1.0f - viewH) / 2;
                break;
            case TYPE_MARING_TOP:
                offset = (contentH * base * 1.0f - viewH) / 2 - (viewH * 11f / 100);
                break;
            case TYPE_MARGIN_BOTOTM:
                offset = (viewH * 11f / 100) - (contentH * base * 1.0f - viewH) / 2;
                break;
            case TYPE_BOTTOM:
                offset = -(contentH * base * 1.0f - viewH) / 2;
                break;
            case TYPE_CENTER:
            case TYPE_DEFAULT:
            default:
                offset = 0;
        }
        return offset;
    }

    private static  float getContentRatio(int contentW, int viewWidth) {
        if (viewWidth <= 0 || contentW <= 0) {
            return 1;
        }
        return (float) (viewWidth * 1.0 / contentW);
    }

}
