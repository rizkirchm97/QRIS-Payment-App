package com.rizki.qrispayment.common.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.math.min

class DrawRect(context: Context): View(context) {


    val paint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val squareRect = android.graphics.RectF()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            val squareSize = min(width, height) / 2.toFloat()
            val centerX = width / 2.toFloat()
            val centerY = height / 2.toFloat()

            squareRect.set(
                centerX - squareSize / 2,
                centerY - squareSize / 2,
                centerX + squareSize / 2,
                centerY + squareSize / 2
            )

            canvas.drawRect(squareRect, paint)
        }
    }
}