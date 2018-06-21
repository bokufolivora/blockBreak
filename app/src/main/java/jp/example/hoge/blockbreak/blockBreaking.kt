package jp.example.hoge.blockbreak

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_block_breaking.*
import java.util.*

class blockBreaking : AppCompatActivity() {
    private val time = 10
    private val handler = Handler()

    // 難易度用
    private val ballSpeed : Float = -10F
    private val barSize : Float = 100F
    private val barMoveStep : Float = 10F

    internal lateinit var ball: Ball
    internal lateinit var kabe : Kabe
    internal lateinit var bar :  Bar
    internal lateinit var block : Block
    internal lateinit var gameArea : gameDisp

    // ゲーム状態
    enum class GmSt {
        INIT ,
        PLAY ,
        STOP ,
        END
    }
    var isGame : GmSt = GmSt.INIT
    // タップ処理用
    var isLongPush : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_breaking)

        ban.setBackgroundColor(Color.BLACK)

        ball = Ball()
        kabe = Kabe(ball)
        bar  = Bar(ball)
        block = Block(ball)
        gameArea = gameDisp(this, ball, bar, block )
        ban.addView(gameArea)

        // 中央のボタン　状態で処理を変える
        button.setOnClickListener(){
            when(isGame) {
                GmSt.INIT ,GmSt.STOP -> {
                    button.text = "STOP"
                    isGame = GmSt.PLAY
                    handler.post(runnable)
                }
                GmSt.PLAY -> {
                    button.text = "RESTART"
                    isGame = GmSt.STOP
                    handler.removeCallbacks(runnable)
                }
                GmSt.END -> {
                    button.text = "START"
                    isGame = GmSt.INIT
                }
            }
        }

        button1.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> { bar.setMove(Bar.MoveSw.move_left, true)}
                MotionEvent.ACTION_UP   -> { bar.setMove(Bar.MoveSw.move_left, false)}
            }
            false
        }
        button2.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> { bar.setMove(Bar.MoveSw.move_right, true)}
                MotionEvent.ACTION_UP   -> { bar.setMove(Bar.MoveSw.move_right, false)}
            }
            false
        }
        ban.setOnLongClickListener {
            isLongPush = true
            false
        }
        ban.setOnTouchListener { v, event ->
            val px = event.getX().toInt()
            val py = event.getY().toInt()

            when (event.action) {
                MotionEvent.ACTION_DOWN->{
                    bar.chkLeftRight( px, py )
                }
                MotionEvent.ACTION_MOVE -> {
                    if ( isLongPush == true ) {
                        bar.chkLeftRight(  px, py )
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isLongPush = false
                    bar.chkLeftRight( px, 0 )
                }
            }
            false
        }
    }
    // 表示後に呼ばれる onCreate()と違いサイズを取得できる　画面再表示時毎回呼ばれるけど
    override fun onWindowFocusChanged( hasFocus : Boolean ) {
        super.onWindowFocusChanged(hasFocus)
        // カベ
        kabe.arrangement(ban.width.toFloat(), ban.height.toFloat())
        // バー
        bar.arrangement(ban.width.toFloat(),ban.height.toFloat())
        // ブロック
        block.arrangement(ban.width.toFloat(),ban.height.toFloat())

        dataReset()
        gameArea.invalidate()
    }
    public override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    // 10mS
    val runnable = object : Runnable {
        override fun run() {
            bar.moveTo()
            ball.move()
            if (false == kabe.chkCollision()) {
                if ( false == bar.chkCollision()) {
                    if ( false == block.chkCollision()) {
                        ball.fix()
                    }
                }
            }
            val cnt = block.blockCount
            if ( 0 == cnt ) {
                button.text = "@string/reset"
                isGame = GmSt.END
                textView.text = "@string/gov"

            } else {
                textView.text = " BLOCK = " + cnt.toString()
            }
            gameArea.invalidate()
            handler.postDelayed(this, time.toLong())
        }
    }

    fun dataReset() {
        bar.reset(barSize, barMoveStep)
        ball.reset(bar.center,bar.top,Random().nextInt(4).toFloat() + 8F,ballSpeed)
        block.reset()
        textView.text = " BLOCK = " + block.blockCount.toString()
    }

}
