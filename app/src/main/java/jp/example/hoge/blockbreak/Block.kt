package jp.example.hoge.blockbreak

import android.graphics.Rect


class Block (val ball : Ball ) {
    val take : Int = 3
    val yoko : Int = 5

    private val top : Float = 10F
    private val height : Float = 40F
    private var stepy : Float = height+ 20F

    private var left : Float = 0F
    private var stepx : Float = 0F
    private var width : Float = 0F
    private var blockSt = Array<Boolean>( take * yoko, { true } )


    //
    val blockCount : Int
        get() = blockSt.count { it == true }

    private fun getTakeIndex(idx : Int) : Int {
        return  idx / yoko
    }
    private fun getColumnIndex( idx : Int, line : Int) : Int {
        return  idx - ( line * yoko )
    }
    fun chkBlock( idx : Int ) : Boolean {
        return blockSt[idx]
    }
    fun getRect( idx : Int) : RectFloat {
        var r : RectFloat = RectFloat()
        if ( blockSt[idx] == true) {
            val line = getTakeIndex(idx)
            val column = getColumnIndex(idx, line)
            r.left = left + (column * stepx )
            r.right = r.left + width
            r.top = top + (line * stepy)
            r.bottom =r.top + height
        }
        return r
    }
    fun arrangement( viewWidth : Float, viewHight : Float) {
        stepx = viewWidth / (yoko+1)
        left = stepx / 2
        width = stepx * 0.8F
    }
    fun reset() {
        blockSt = Array<Boolean>( take * yoko, { true } )
    }


    //　衝突判定　それっぽく
    fun chkCollision(): Boolean {
        var r: Boolean = false

        for (l in 0..take - 1) {
            val yt = top + (l * stepy)
            val yb = yt + height
            if (ball.newBottom >= yt && ball.newTop <= yb) {
                for (c in 0..yoko - 1) {
                    if ( true == blockSt[ l*yoko+c]) {
                        val xl = left + (c * stepx)
                        val xr = xl + width
                        if (ball.newRight >= xl && ball.newLeft <= xr) {
                            // ぶつかっている　
                            blockSt[l * yoko + c] = false

                            val ts = Math.abs(yt - ball.bottom)
                            val bs = Math.abs(yb - ball.top)
                            val ls = Math.abs(xl - ball.right)
                            val bl = Math.abs(xr - ball.left)
                            if (ts <= bs && ts <= ls && ts <= bl) {
                                // 上からぶつかったことに
                                ball.resetNewY(yt)
                            } else if (bs <= ts && bs <= ls && bs <= bl) {
                                ball.resetNewY(yb)
                            } else if (ls <= ts && ls <= bs && ls <= bl) {
                                ball.resetNewX(xl)
                            } else {
                                ball.resetNewX(xr)
                            }
                            return true
                        }
                    }
                }
            }
        }
        return false
    }
}

// 有るかも or 本当はここまでいらないと思う
class RectFloat(var left : Float = 0F, var top : Float = 0F, var right : Float = 0F, var bottom : Float = 0F)
