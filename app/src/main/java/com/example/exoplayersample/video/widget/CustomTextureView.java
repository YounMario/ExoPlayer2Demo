package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by wangye on 16-11-2.
 */

public class CustomTextureView extends TextureView {
    private static final String TAG = "CustomTextureView";
    private int mVideoWidth;
    private int mVideoHeight;

    public CustomTextureView(Context context) {
        super(context);
    }

    public CustomTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float scaledX = 1.f * mVideoWidth / w;
        float scaledY = 1.f * mVideoHeight / h;
        float scale = Math.max(1 / scaledX, 1 / scaledY);

        // Calculate pivot points, in our case crop from center
        int pivotPointX = w / 2;
        int pivotPointY = h / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(scaledX * scale, scaledY * scale, pivotPointX, pivotPointY);

        setTransform(matrix);
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;
    }
}
