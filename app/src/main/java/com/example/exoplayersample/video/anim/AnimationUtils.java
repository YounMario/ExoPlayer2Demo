package com.example.exoplayersample.video.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Created by 龙泉 on 2016/12/21.
 */

public class AnimationUtils {

    public static ObjectAnimator createRotationAnimation(View view) {
        ObjectAnimator roatingAnimation = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 360);
        roatingAnimation.setDuration(1000);
        roatingAnimation.setInterpolator(new LinearInterpolator());
        roatingAnimation.setRepeatCount(ValueAnimator.INFINITE);
        roatingAnimation.setRepeatMode(ValueAnimator.RESTART);
        return roatingAnimation;
    }

    public static ObjectAnimator createShowGradientAnimation(View view) {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1f);
        alphaAnimation.setDuration(400);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        return alphaAnimation;
    }

    public static ObjectAnimator createHideGradientAnimation(final View view) {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0f);
        alphaAnimation.setDuration(400);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return alphaAnimation;
    }
}
