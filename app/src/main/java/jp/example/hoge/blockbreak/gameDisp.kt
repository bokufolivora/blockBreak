package jp.example.hoge.blockbreak

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class gameDisp(context: Context, val ball:Ball, val bar:Bar, val block:Block ) : View(context) {
    internal var paint: Paint

    init {
        paint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (0F != ball.x && 0F != ball.y) {
            paint.setColor(Color.WHITE)
            canvas.drawCircle(ball.x, ball.y, ball.size, paint)
        }
        if (bar.top != 0F) {
            paint.setColor(Color.GREEN)
            canvas.drawRect(bar.left, bar.top, bar.right, bar.bottom, paint)
        }
        if (block.blockCount > 0) {
            paint.setColor(Color.YELLOW)
            for (idx: Int in 0..(block.yoko * block.take) - 1) {
                if (block.chkBlock(idx) == true) {
                    val b = block.getRect(idx)
                    canvas.drawRect(b.left, b.top, b.right, b.bottom, paint)
                }
            }
        }
    }
}