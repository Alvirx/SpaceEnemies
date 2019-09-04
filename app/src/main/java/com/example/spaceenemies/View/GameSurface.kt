package com.example.spaceenemies.View


import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.spaceenemies.Controller.GameThread
import com.example.spaceenemies.Model.SpaceEnemiesConsts


//TODO przenieś trzymanie danych i ustawień do persistence facade zhardcoduj wielkość planszy.
// Nie może być tak, że gra będzie inna na różnych urządzeniach bo mają inne aspect ratio. Playera też zhardcoduj



@RequiresApi(Build.VERSION_CODES.O)
class GameSurface(context: Context?, attrs: AttributeSet?) :
    SurfaceView(context, attrs), SurfaceHolder.Callback
{
    companion object {
        private const val TAG = "GameSurface"

        private val red:Paint = with(Paint(Paint.ANTI_ALIAS_FLAG)){
            setARGB(255,255,0,0)
            textSize = 100f
            textAlign = Paint.Align.CENTER
            this
        }
        private val green:Paint = with(Paint(Paint.ANTI_ALIAS_FLAG))
        {
            setARGB(255, 0, 255, 0)
            textSize = 25f
            this
        }
    }

    private var game: GameThread = GameThread(holder, this)

    init {
        holder.addCallback(this)
    }


    //Other
    private var xScale = 1.0
    private var yScale = 1.0
    private var ratio = 1.0


    /**
     * Adjusts canvas for different screen sizes
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun adjustCanvas() {

        val canvas = holder.lockHardwareCanvas()
        val w = canvas!!.width
        val h = canvas.height

        ratio = (1.0*h/w)
        xScale = w/720.0
        yScale = h/(720.0*ratio)
        holder.setFixedSize(720, (720*ratio).toInt())
        holder.unlockCanvasAndPost(canvas)
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        adjustCanvas()
    }

    /**
     * Suspends the game and finishes the game thread
     */
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        game.saveGameState()
        game.setRunning(false)
        game.join()
    }


    /**
     * Initializes GameThread and canvas
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun surfaceCreated(holder: SurfaceHolder) {
        if(!game.isAlive)
            game = GameThread(holder, this)
        adjustCanvas()
        game.initializeGame()
        game.start()
    }

    /**
     * Draws entities on canvas
     */
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas==null) return

        game.getProjectiles().forEach{
            canvas.drawBitmap(it.bitmap, it.x, it.y, green)
        }

        game.getEnemies().forEach{
            canvas.drawBitmap(it.bitmap, it.x, it.y, green)
        }

        canvas.drawBitmap(game.player.bitmap, game.player.x, game.player.y, red)

        canvas.drawText("Points: ${game.points}", 10f, 25f, green)
        if(!game.gameIsOn){
            canvas.drawText("Game Over", 360f, 300f, red)
            canvas.drawText("Tap to", 360f, 500f, red)
            canvas.drawText("Play Again ", 360f, 700f, red)
        }

    }

    /**
     * Sends information about touch events to game.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        game.touchedX = event.x.toInt()
        game.touchedY = event.y.toInt()

        when(event.action){
            MotionEvent.ACTION_DOWN -> game.touched = true
            MotionEvent.ACTION_MOVE -> game.touched = true
            MotionEvent.ACTION_UP -> game.touched = false
            MotionEvent.ACTION_CANCEL -> game.touched = false
            MotionEvent.ACTION_OUTSIDE -> game.touched = false

        }

        return true
    }

}