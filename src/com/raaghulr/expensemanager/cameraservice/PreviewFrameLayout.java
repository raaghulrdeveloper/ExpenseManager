   

package com.raaghulr.expensemanager.cameraservice;

import com.raaghulr.expensemanager.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class PreviewFrameLayout extends ViewGroup {
    private static final int MIN_HORIZONTAL_MARGIN = 10; // 10dp

   
    public interface OnSizeChangedListener {
        public void onSizeChanged();
    }

    private double mAspectRatio = 4.0 / 3.0;
    private FrameLayout mFrame;
    private OnSizeChangedListener mSizeListener;
    private final DisplayMetrics mMetrics = new DisplayMetrics();

    public PreviewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        mSizeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
        if (mFrame == null) {
            throw new IllegalStateException("must provide child with id as \"frame\"");
        }
    }

    public void setAspectRatio(double ratio) {
        if (ratio <= 0.0) throw new IllegalArgumentException();

        if (mAspectRatio != ratio) {
            mAspectRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
    	
        int frameWidth = getWidth();
        int frameHeight = getHeight();

        FrameLayout f = mFrame;

        int horizontalPadding = Math.max(f.getPaddingLeft() + f.getPaddingRight(),(int) (MIN_HORIZONTAL_MARGIN * mMetrics.density));
        int verticalPadding = f.getPaddingBottom() + f.getPaddingTop();

        int previewHeight = frameHeight;
        int previewWidth = frameWidth - horizontalPadding;

        
        if (previewWidth > previewHeight * mAspectRatio) {
            previewWidth = (int) (previewHeight * mAspectRatio + .5);
        } else {
            previewHeight = (int) (previewWidth / mAspectRatio + .5);
        }
        frameWidth = previewWidth + horizontalPadding;
        frameHeight = previewHeight + verticalPadding;
        int hSpace = ((r - l) - frameWidth) / 2;
        int vSpace = ((b - t) - frameHeight) / 2;
        mFrame.measure(MeasureSpec.makeMeasureSpec(frameWidth, MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(frameHeight, MeasureSpec.EXACTLY));
        mFrame.layout(l + hSpace, t + vSpace, r - hSpace, b - vSpace);
        if (mSizeListener != null) {
            mSizeListener.onSizeChanged();
        }
    }
}

