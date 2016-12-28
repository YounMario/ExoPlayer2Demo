package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.exoplayersample.R;
import com.younchen.myexoplayer.player.Player;

import java.lang.ref.WeakReference;

/**
 * Created by 龙泉 on 2016/12/15.
 */
public class VideoProgressBar extends View {


    private Paint mPaint;
    private Paint mBackGroundPaint;
    private Paint mThumbPaint;
    private Paint mBufferPaint;

    private WeakReference<Player> weakReference;


    private ProgressListener mProgressListener;
    private final String colorBlue = "#3488EB";
    private final String colorWhite = "#ffffffff";


    private boolean mHitThumb;

    private static final int DEFAULT_PROGRESS_HEIGHT = 4;
    private static final int DEFAULT_THUMB_SIZE = 10;

    private int mProgressBarHeight;
    private int mThumbRadius;

    private SeekListener mSeekListener;

    private Point mThumbPoint;
    private int mLastProgress;

    private boolean mIsDragging;
    private boolean mNeedShowBufferBar;

    public VideoProgressBar(Context context) {
        this(context, null);
    }

    public VideoProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = getResources();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackGroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBufferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setColor(Color.parseColor(colorBlue));
        mBackGroundPaint.setColor(Color.parseColor("#565656"));
        mThumbPaint.setColor(Color.parseColor(colorBlue));
        mBufferPaint.setColor(Color.parseColor(colorWhite));

        mThumbPoint = new Point();
        mThumbPoint.x = 0;
        mThumbPoint.y = getHeight() / 2;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VideoProgressBar);
        mProgressBarHeight = array.getDimensionPixelSize(R.styleable.VideoProgressBar_progress_height, DEFAULT_PROGRESS_HEIGHT);
        mThumbRadius = array.getDimensionPixelSize(R.styleable.VideoProgressBar_thumb_radius, DEFAULT_THUMB_SIZE);
        array.recycle();

        mIsDragging = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int currentProgress = getPlayProgress();

        int currentPositionX = (int) (currentProgress * 1.0 * getWidth() / 100);
        int progressBarTop = getHeight() / 2 - mProgressBarHeight / 2;
        int progressBarHeight = progressBarTop + mProgressBarHeight;


        float fixedX = mIsDragging ? getFixedThumbPosition(mThumbPoint.x) : getFixedThumbPosition(currentPositionX);
        canvas.drawRect(0, progressBarTop, getWidth(), progressBarHeight, mBackGroundPaint);
        if (mNeedShowBufferBar) {
            int bufferPercent = getBufferProgress();
            int bufferX = (int) (bufferPercent * 1.0 * getWidth() / 100);
            canvas.drawRect(0, progressBarTop, bufferX, progressBarHeight, mBufferPaint);
        }
        canvas.drawRect(0, progressBarTop, fixedX, progressBarHeight, mPaint);
        canvas.drawCircle(fixedX, getHeight() / 2, mThumbRadius, mThumbPaint);

        if (mProgressListener != null) {
            mProgressListener.updateProgress(currentProgress);
        }

        if (currentProgress <= 100) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float newX = event.getX();
        float newY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isHintThumb(newX, newY)) {
                    mThumbPoint.x = getFixedX(newX);
                    mHitThumb = true;
                } else {
                    mHitThumb = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mHitThumb) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mIsDragging = true;
                    mThumbPoint.x = getFixedX(newX);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mHitThumb = false;
                mIsDragging = false;
                if (mSeekListener != null) {
                    int progress = (int) (mThumbPoint.x * 100 * 1.0f / getWidth());
                    if(progress != mLastProgress){
                        mSeekListener.onSeek(progress);
                        mLastProgress = progress;
                    }
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                invalidate();
                break;
        }
        return mHitThumb;
    }

    private float getFixedThumbPosition(float thumbPosition) {
        int maxThumbPosition = getWidth() - mThumbRadius;
        int minThumbPosition = mThumbRadius;
        if (thumbPosition > maxThumbPosition) return maxThumbPosition;
        if (thumbPosition < minThumbPosition) return minThumbPosition;
        return thumbPosition;
    }

    private int getFixedX(float newX) {
        if (newX < 0) return 0;
        if (newX > getWidth()) return getWidth();
        return (int) newX;
    }

    private boolean isHintThumb(float touchX, float touchY) {
        boolean matchedY = touchY >= 0 && touchY <= getHeight();
        boolean matchedX = touchX >= 0 && touchX <= getWidth();
        return matchedX && matchedY;
    }


    private int getPlayProgress() {
        if (weakReference == null || weakReference.get() == null) return 0;
        long duration = weakReference.get().getDuration();
        if (duration == 0) return 0;
        return (int) (100.f * weakReference.get().getCurrentPosition() / duration);
    }

    private int getBufferProgress() {
        if (weakReference == null || weakReference.get() == null) return 0;
        final Player player = weakReference.get();
        long percentage = player.getBufferedPercentage();
        return (int) percentage;
    }

    public void setController(WeakReference<Player> playerControlReference) {
        weakReference = playerControlReference;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void releseController() {
        if (weakReference != null && weakReference.get() != null) {
            weakReference.clear();
        }
    }

    public void setSeekListener(SeekListener seekListener){
        this.mSeekListener = seekListener;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }


    public void setNeedShowBufferBar(boolean mNeedShowBufferBar) {
        this.mNeedShowBufferBar = mNeedShowBufferBar;
    }

    public interface ProgressListener {
        void updateProgress(int progress);
    }

    public interface SeekListener {
        void onSeek(int progress);
    }
}
