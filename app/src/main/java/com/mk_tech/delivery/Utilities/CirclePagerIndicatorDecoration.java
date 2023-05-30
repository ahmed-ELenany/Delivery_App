package com.mk_tech.delivery.Utilities;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CirclePagerIndicatorDecoration extends RecyclerView.ItemDecoration {

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;
    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private final int mIndicatorHeight = (int) (DP * 5);
    /**
     * Indicator stroke width.
     */
    private final float mIndicatorStrokeWidth = DP * 5;
    /**
     * Indicator width.
     */
    private final float mIndicatorItemLength = DP * 5;
    /**
     * Padding between indicators.
     */
    private final float mIndicatorItemPadding = DP * 5;
    /**
     * Some more natural animation interpolation
     */

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final Paint mPaint = new Paint();
    private final int colorActive = 0xFF2CCCD3;
    private final int colorInactive = 0xFFC5F1F3;

    public CirclePagerIndicatorDecoration() {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount;
        try {
            itemCount = parent.getAdapter().getItemCount();

        } catch (Exception e) {
            itemCount = 0;

        }

        // center horizontally, calculate width and subtract half from center
        float totalLength = mIndicatorItemLength * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

        // center vertically in the allotted space
        float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);


        // find active page (which should be highlighted)
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition;
        try {
            activePosition = layoutManager.findFirstVisibleItemPosition();
        } catch (Exception e) {
            activePosition = 0;
        }
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // find offset of active page (if the user is scrolling)
        final View activeChild;
        int left = 0, width = 0;
        try {
            activeChild = layoutManager.findViewByPosition(activePosition);
            left = activeChild.getLeft();
            width = activeChild.getWidth();
        } catch (Exception e) {

        }


        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setColor(colorInactive);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            // draw the line for every item
            c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
            start += itemWidth;
        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(colorActive);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        if (progress == 0F) {
            // no swipe, draw a normal indicator
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            c.drawLine(highlightStart, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
        } else {
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            // calculate partial highlight
            float partialLength = mIndicatorItemLength * progress;

            // draw the cut off highlight
            c.drawLine(highlightStart + partialLength, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

            // draw the highlight overlapping to the next item as well
            if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth;
                c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + partialLength, indicatorPosY, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mIndicatorHeight;
    }
}