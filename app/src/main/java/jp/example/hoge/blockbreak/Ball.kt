package jp.example.hoge.blockbreak

class Ball() {
    private val radius : Float = 30F
    private var pointX : Float = 0F
    private var pointY : Float = 0F
    private var vectorX:Float = 0F
    private var vectorY:Float = 0F
    private var tmpX : Float = 0F
    private var tmpY: Float = 0F

    //　描画（円）用
    val size : Float
        get() = radius
    val x  : Float
        get() = pointX
    val y : Float
        get() = pointY
    // 移動前の位置
    val top : Float
        get() = pointY-radius
    val bottom : Float
        get() = pointY+radius
    val left : Float
        get() = pointX-radius
    val right : Float
        get() = pointX+radius
    // 移動後の位置
    val newTop : Float
        get() = tmpY-radius
    val newBottom : Float
        get() = tmpY+radius
    val newLeft : Float
        get() = tmpX-radius
    val newRight : Float
        get() = tmpX+radius

    // 初期位置へ
    fun reset(cx : Float, ytop : Float, vx : Float, vy :Float ) {
        pointX = cx
        pointY = ytop -radius
        vectorX = vx
        vectorY = vy
    }
    // 移動 (暫定値算出)
    fun move( ) {
        tmpX = pointX + vectorX
        tmpY = pointY + vectorY
    }
    // 暫定値を今の位置に　(どこにもぶつからなかった時呼ばれる)
    fun fix() {
        pointX = tmpX
        pointY = tmpY
    }
    // Xぶつかった時
    fun resetNewY( p : Float ) {
        pointY = if ( top > p ) {
            p+radius
        } else {
            p-radius
        }
        vectorY *= -1
    }
    // Yぶつかった時
    fun resetNewX( p : Float ) {
        pointX = if ( left < p ) {
            p-radius
        } else {
            p+radius
        }
        vectorX *= -1
    }
    // X軸　加減速　多少のゲーム要素
    fun vectorAcceleX( acc : Float) { vectorX += acc }

}