package com.softdesign.devintensive.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.softdesign.devintensive.R;

/**
 * Created by Игорь on 14.07.2016.
 */
public class AspectRatioImageView extends ImageView {
    private static final float DEFAULT_ASPECT_RATIO = 1.73f;
    private final float mAspectRatio;

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        mAspectRatio=array.getFloat(R.styleable.AspectRatioImageView_aspect_ratio,DEFAULT_ASPECT_RATIO);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidht;
        int newHeight;
        newWidht=getMeasuredWidth();
        newHeight= (int) (newWidht/mAspectRatio);
        setMeasuredDimension(newWidht,newHeight);
    }
}
