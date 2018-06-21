package jp.example.hoge.blockbreak

import android.util.Log

class Bar(val ball : Ball ) {
    // バーの移動状態
    enum class MoveSw {
        move_stop,
        move_left,
        move_right
    }

    // ここでバーの表示状態指定
    private val OffsetY : Float = 100F
    private val h : Float = 20F

    private var vWidth : Float = 0F
    private var x : Float = 0F
    private var y : Float = 0F
    private var moveFlg : MoveSw = MoveSw.move_stop
    private var targetX : Float = 0F

    private var size : Float = 100F
    private var moveSize : Float = 10F


    // バーの座標情報
    val top : Float
        get() = y
    val bottom : Float
        get() = y+h
    val left : Float
        get() = x-size
    val right : Float
        get() = x+size
    val center : Float
        get() = x

    //　配置　（画面の大きさに合わせる)
    fun arrangement( vw : Float, vh : Float) {
        vWidth = vw
        y = vh - OffsetY
    }
    fun reset(barWidih : Float , kizami : Float ) {
        x = vWidth / 2
        size = barWidih
        moveSize = kizami
    }
    fun chkCollision( ) : Boolean {
        var r: Boolean = false
        if ( ball.bottom < y  && ball.newBottom >= y) {
            if ((  ball.right>left || ball.newRight>left ) &&
                    (ball.left < right || ball.newLeft < right))   {
                //　ざっくりとした判定　
                ball.resetNewY( y )
                when(moveFlg) {
                    MoveSw.move_left -> { ball.vectorAcceleX(-0.1F)}
                    MoveSw.move_right -> {ball.vectorAcceleX(0.1F)}
                }
                r = true
            }
        }
        return r
    }
    fun chkLeftRight( px : Int, py :Int) {
        targetX = if ( py > top && py < top+100F ) {
            if ((x + (size / 3)) < px) {
//                Log.d("right", px.toString()+","+(x.toInt()).toString())
                px.toFloat()
            } else if ((x - (size / 3)) > px ) {
//                Log.d("left", px.toString()+","+(x.toInt()).toString())
                px.toFloat()
            } else {
//                Log.d("In", px.toString()+","+(x.toInt()).toString())
                moveFlg = MoveSw.move_stop
                0F
            }
        } else {
            Log.d("out", px.toString()+","+(x.toInt()).toString())
            moveFlg = MoveSw.move_stop
            0F
        }
    }
    fun setMove(LeftRight:MoveSw, goStop : Boolean ) {
        if ( goStop == true) {
            moveFlg = LeftRight
        } else if ( moveFlg == LeftRight ) {
            moveFlg = MoveSw.move_stop
        }
    }
    fun moveTo() {
        moveFlg = if ( 0F != targetX ) {
            if ( (x-(size/3)) >= targetX ) {
                MoveSw.move_left
            } else if ( (x+(size/3)) <= targetX) {
                MoveSw.move_right
            } else {
                targetX = 0F
                MoveSw.move_stop
            }
        } else {
            moveFlg
        }
        x = when(moveFlg) {
            MoveSw.move_left -> {
                if (left < 0F) {
                    size
                } else {
                    x - moveSize
                }
            }
            MoveSw.move_right-> {
                if (right > vWidth) {
                    vWidth - size
                } else {
                    x + moveSize
                }
            }
            else -> { x }

        }
    }
}