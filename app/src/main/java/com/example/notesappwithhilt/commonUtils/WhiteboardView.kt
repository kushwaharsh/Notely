package com.example.notesappwithhilt.commonUtils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class WhiteboardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var drawPath: Path = Path()
    private var drawPaint: Paint = Paint()
    private var canvasPaint: Paint = Paint()
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        drawPaint.color = Color.BLACK
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 5f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND

        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        if (canvasBitmap == null) {
            // Create a new bitmap only if it's not set yet
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            drawCanvas = Canvas(canvasBitmap!!)
        } else {
            // Scale the current bitmap to fit the new size
            val scaledBitmap = Bitmap.createScaledBitmap(canvasBitmap!!, w, h, true)
            canvasBitmap = scaledBitmap
            drawCanvas = Canvas(scaledBitmap)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the existing bitmap and the path
        canvasBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, canvasPaint)
        }
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                drawCanvas?.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
        }
        invalidate()
        return true
    }

    // Capture the drawing as a bitmap
    fun getDrawingBitmap(): Bitmap? {
        return canvasBitmap
    }

    // Clear the whiteboard
    fun clearBoard() {
        drawCanvas?.drawColor(Color.WHITE)
        invalidate()
    }

    // Method to set a Bitmap to the WhiteBoardView
    fun setWhiteBoardBitmap(bitmap: Bitmap) {
        // Set the received bitmap as the canvas bitmap
        canvasBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        drawCanvas = Canvas(canvasBitmap!!)
        invalidate() // Redraw the view with the new bitmap
    }
}
