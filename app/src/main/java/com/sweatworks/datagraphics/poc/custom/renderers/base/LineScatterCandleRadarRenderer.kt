package com.sweatworks.datagraphics.poc.custom.renderers.base

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Path
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet
import com.github.mikephil.charting.renderer.BarLineScatterCandleBubbleRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 11/07/15.
 * IMPORTANT: all lines with comments starting with ## indicates what changes I have been made for Speede Project
 */

abstract class LineScatterCandleRadarRenderer(
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) :
    BarLineScatterCandleBubbleRenderer(animator, viewPortHandler) {
    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of
     * dashes)
     */
    private val mHighlightLinePath = Path()

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param c
     * @param x x-position of the highlight line intersection
     * @param y y-position of the highlight line intersection
     * @param set the currently drawn dataset
     */
    protected fun drawHighlightLines(
        c: Canvas,
        x: Float,
        y: Float,
        set: ILineScatterCandleRadarDataSet<*>
    ) {

        // set color and stroke-width
        mHighlightPaint.color = set.highLightColor
        mHighlightPaint.strokeWidth = set.highlightLineWidth

        // draw highlighted lines (if enabled)
        mHighlightPaint.pathEffect = set.dashPathEffectHighlight

        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled) {

            // create vertical path
            mHighlightLinePath.reset()
            mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop())
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom())
            c.drawPath(mHighlightLinePath, mHighlightPaint)
        }

        // draw horizontal highlight lines
        if (set.isHorizontalHighlightIndicatorEnabled) {

            // create horizontal path
            mHighlightLinePath.reset()
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y)
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y)

            // ## change-start: added dashPathEffect to the line
            val lineLength = 10f
            val spaceLength = 5f
            val phase = 0f
            mHighlightPaint.pathEffect = DashPathEffect(floatArrayOf( lineLength, spaceLength), phase)
            // ## change-end: added dashPathEffect to the line
            c.drawPath(mHighlightLinePath, mHighlightPaint)
        }
    }
}
