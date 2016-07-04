package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Игорь on 29.06.2016.
 */

public class UserScrollBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserScrollBehavior";
    private int mMaxHeight = 0;

    public UserScrollBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View view) {
        Log.d(TAG, "onDependentViewChanged");
        if (mMaxHeight < view.getY()) mMaxHeight = (int) view.getY();
        child.setY(view.getY());
        View bigSeparFirst = child.getChildAt(0);
        View bigSeparSecond = child.getChildAt(2);
        if (view.getY() < mMaxHeight / 2) {
            bigSeparFirst.setVisibility(View.GONE);
            bigSeparSecond.setVisibility(View.GONE);
        } else {
            bigSeparFirst.setVisibility(View.VISIBLE);
            bigSeparSecond.setVisibility(View.VISIBLE);
        }
        int hh = (int) child.getHeight();
        view.setPadding(view.getPaddingLeft(), hh, view.getPaddingRight(), view.getPaddingBottom());
        return true;
    }
}