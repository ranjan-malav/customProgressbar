package com.example.customprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.RectF
import kotlin.math.cos
import kotlin.math.sin


class CustomProgressbar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var blobPaint: Paint
    private lateinit var backCirclePaint: Paint
    private lateinit var frontCirclePaint: Paint
    private var progress: Int = 0
    private var endAngle: Float = 0f
    private var blobRadius: Float = 30f
    private var mStrokeWidth: Float = 20f
    private var availableWidth: Float = 0f
    private var availableHeight: Float = 0f
    private var animationStartTime: Long = 0
    private val animationDuration: Int = 1000

    init {
        setupPaint()
    }

    // Reset view back to default values
    fun reset() {
        this.progress = 0
        endAngle = 0f
        animationStartTime = System.currentTimeMillis()
        invalidate()
    }

    // Set progress and start animating
    fun setProgress(progress: Int) {
        this.progress = if (progress > 100) 100 else progress
        endAngle = (360f * this.progress) / 100
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // Need to reduce width and height otherwise blob circle will not fit inside
        availableWidth = width - blobRadius * 2
        availableHeight = height - blobRadius * 2

        if (animationStartTime == 0L) {
            animationStartTime = System.currentTimeMillis()
        }

        // Draw back circle
        canvas?.drawCircle(
            blobRadius + availableWidth / 2,
            blobRadius + availableHeight / 2,
            availableWidth / 2,
            backCirclePaint
        )

        // Find angle from -90 for the current progress
        val mCurrentAngle = findCurrentAngle()

        // Draw front circle
        val rectF = RectF(
            blobRadius, blobRadius,
            blobRadius + availableWidth,
            availableHeight + blobRadius
        )
        canvas?.drawArc(
            rectF,
            -90f,
            mCurrentAngle,
            false,
            frontCirclePaint
        )

        // X and Y coordinates for the blob
        val xForBlob = findXOfBlob(mCurrentAngle)
        val yForBlob = findYOfBlob(mCurrentAngle)

        // Although we are drawing a circle, we can pass any image source in xml and draw it here
        canvas?.drawCircle(
            xForBlob,
            yForBlob,
            blobRadius,
            blobPaint
        )

        // Invalidate and re-render if we haven't reached end angle
        if (mCurrentAngle < endAngle) {
            invalidate()
        }
    }

    private fun findXOfBlob(progressAngle: Float): Float {
        val x =
            blobRadius + (availableHeight) / 2 + ((availableHeight) / 2) * sin(
                Math.toRadians(
                    progressAngle.toDouble()
                )
            )
        return x.toFloat()
    }

    private fun findYOfBlob(progressAngle: Float): Float {
        val y =
            blobRadius + (availableWidth) / 2 - ((availableWidth) / 2) * cos(
                Math.toRadians(
                    progressAngle.toDouble()
                )
            )
        return y.toFloat()
    }

    // Finds current angle according to animation duration and starting time
    private fun findCurrentAngle(): Float {
        val now = System.currentTimeMillis()
        val pathGone = (now - animationStartTime).toFloat() / animationDuration

        if (pathGone < 1.0f) {
            return endAngle * pathGone
        } else {
            return endAngle
        }
    }

    private fun setupPaint() {
        setupFrontCirclePaint()
        setupBackCirclePaint()
        setupBlobPaint()
    }

    private fun setupFrontCirclePaint() {
        frontCirclePaint = Paint().apply {
            color = resources.getColor(R.color.colorPrimary)
            style = Paint.Style.STROKE
            strokeWidth = mStrokeWidth
        }
    }

    private fun setupBackCirclePaint() {
        backCirclePaint = Paint().apply {
            color = resources.getColor(R.color.grey)
            style = Paint.Style.STROKE
            strokeWidth = mStrokeWidth
        }
    }

    private fun setupBlobPaint() {
        blobPaint = Paint().apply {
            color = resources.getColor(R.color.orange)
            style = Paint.Style.FILL
        }
    }
}