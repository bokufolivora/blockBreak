package jp.example.hoge.blockbreak


class Kabe(val ball : Ball) {
    private var width  : Float = 0F
    private var height : Float = 0F

    // 配置 (画面サイズをもらう)
    fun arrangement( vw : Float, vh : Float) {
        width = vw
        height = vh
    }

    // 衝突判定 戻り true = ぶつかった＆ボール反転させた
    fun chkCollision( ) : Boolean {

        return if (ball.newLeft < 0 ) {
            // 左
            ball.resetNewX(0F )
            true
        } else if ( ball.newRight >= width ) {
            // 右
            ball.resetNewX( width )
            true
        } else if (ball.newTop < 0 ) {
            ball.resetNewY(0F)
            true
        } else if (ball.newBottom >= height ) {
            ball.resetNewY(height)
            true
        } else {
            false
        }
    }

}