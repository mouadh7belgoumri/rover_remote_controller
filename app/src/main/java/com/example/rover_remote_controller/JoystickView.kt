package com.example.rover_remote_controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class JoystickView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerX = 0f
    private var centerY = 0f
    private var baseRadius = 0f
    private var hatRadius = 0f
    private var posX = 0f
    private var posY = 0f

    private val basePaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val hatPaint = Paint().apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    var onJoystickMoveListener: ((xPercent: Float, yPercent: Float) -> Unit)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        val d = min(w, h)
        baseRadius = d / 3f
        hatRadius = d / 6f
        posX = centerX
        posY = centerY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint)
        canvas.drawCircle(posX, posY, hatRadius, hatPaint)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                updatePosition(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                updatePosition(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                posX = centerX
                posY = centerY
                invalidate()
                onJoystickMoveListener?.invoke(0f, 0f)
            }
        }
        return true
    }

    private fun updatePosition(x: Float, y: Float) {
        val displacement = sqrt((x - centerX).pow(2) + (y - centerY).pow(2))
        if (displacement < baseRadius) {
            posX = x
            posY = y
        } else {
            val ratio = baseRadius / displacement
            posX = centerX + (x - centerX) * ratio
            posY = centerY + (y - centerY) * ratio
        }
        invalidate()

        val xPercent = (posX - centerX) / baseRadius
        val yPercent = (posY - centerY) / baseRadius
        onJoystickMoveListener?.invoke(xPercent, yPercent)
    }
}
