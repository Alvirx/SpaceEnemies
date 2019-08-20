package com.example.spaceenemies


import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Math.pow
import java.lang.Math.sqrt
import java.util.*
import kotlin.collections.ArrayList



@RequiresApi(Build.VERSION_CODES.O)
class GameSurface(context: Context?, attrs: AttributeSet?) :
    SurfaceView(context, attrs), SurfaceHolder.Callback
{
    companion object {
        private const val TAG = "GameSurface"

        private const val playerWidth = 70f
        private const val enemyWidth = 70f
        private const val playerFireWidth = 10f
        private const val playerFireSpeed = 10f
        private const val playerSpeed = 6f
        private val randomizer = Random()

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
    private var thread: GameThread


    //Coordinates and game state
    private var playerX = 0f
    private var playerY = 0f

    private var playerFires: LinkedList<PointF> = LinkedList()
    private var enemyFires: LinkedList<PointF> = LinkedList()

    private var enemies: ArrayList<PointF> = ArrayList()

    private var enemiesDestinations:ArrayList<PointF> = ArrayList()

    private var counter = 0
    private var points = 0

    private var gameIsOn = false


    //graphics

    private var playerShip = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.player),
        playerWidth.toInt(),
        playerWidth.toInt(),
        false
    )

    private val enemy = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.enemy),
        enemyWidth.toInt(),
        enemyWidth.toInt(),
        false
    )
    private val playerFire = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.playerfire),
        playerFireWidth.toInt(),
        playerFireWidth.toInt(),
        false
    )
    private val enemyFire = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.enemyfire),
        playerFireWidth.toInt(),
        playerFireWidth.toInt(),
        false
    )



    //Controls
    private var touchedX = 0
    private var touchedY = 0
    private var touched:Boolean = false


    //Other
    private var xScale = 1.0
    private var yScale = 1.0
    private var ratio = 1.0


    init {
        thread = GameThread(holder, this)
        holder.addCallback(this)
    }

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

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        thread.setRunning(false)
        thread.join()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun surfaceCreated(holder: SurfaceHolder) {
        if(!thread.isAlive)
            thread = GameThread(holder, this)
        thread.setRunning(true)
        adjustCanvas()
        startGame()
        thread.start()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas==null) return



        playerFires.forEach{
            canvas.drawBitmap(playerFire, it.x, it.y, green)
        }

        enemyFires.forEach{
            canvas.drawBitmap(enemyFire, it.x, it.y, green)
        }

        enemies.forEach{
            canvas.drawBitmap(enemy, it.x, it.y, green)
        }

        canvas.drawBitmap(playerShip, playerX, playerY, red)

        canvas.drawText("Points: $points", 10f, 25f, green)
        if(!gameIsOn){
            canvas.drawText("Game Over", 360f, 300f, red)
            canvas.drawText("Tap to", 360f, 500f, red)
            canvas.drawText("Play Again ", 360f, 700f, red)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchedX = event.x.toInt()
        touchedY = event.y.toInt()

        when(event.action){
            MotionEvent.ACTION_DOWN -> touched = true
            MotionEvent.ACTION_MOVE -> touched = true
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
            MotionEvent.ACTION_OUTSIDE -> touched = false

        }

        return true
    }


    private fun startGame(){
        gameIsOn = true
        points = 0
        enemies = ArrayList()
        enemiesDestinations = ArrayList()
        playerFires = LinkedList()
        enemyFires = LinkedList()
        playerY = (720*ratio - 2*playerWidth).toFloat()
        playerX = 720/2 - playerWidth/2
    }

    fun update(){

        if(!gameIsOn && touched)
            startGame()

        if(gameIsOn){
            spawnEntities()

            movePlayer()

            calcCollisions()

            moveEnemies()

            moveFires()
        }
    }

    private fun spawnEntities() {
        if(counter==0){
            if (randomizer.nextFloat()>0.15 && enemies.size<30){
                val p = PointF(randomizer.nextInt((720-enemyWidth).toInt()).toFloat(), -enemyWidth)
                enemies.add(p)
                enemiesDestinations.add(
                    PointF(
                        randomizer.nextInt((720-enemyWidth).toInt()).toFloat(),
                        randomizer.nextInt((720-enemyWidth).toInt()).toFloat()
                    )
                )

            }
            enemies.forEach{
                if (randomizer.nextFloat()>0.90){
                    enemyFires.add(PointF(it.x+enemyWidth/2, it.y+enemyWidth))
                }
            }
            playerFires.add(PointF(playerX+(playerWidth/2)-playerFireWidth/2, playerY-20))
        }
        counter = (counter+1)%15
    }

    private fun movePlayer() {
        if(touched){
            if(touchedX>playerX+10 && (playerX)<720-playerWidth){
                playerX+=playerSpeed
            }
            else if(touchedX<playerX-10 && playerX>0){
                playerX-=playerSpeed
            }
        }
    }

    private fun moveFires() {
        playerFires.forEach{
            it.y = it.y-playerFireSpeed
        }
        enemyFires.forEach{
            it.y = it.y+playerFireSpeed
        }
    }

    private fun moveEnemies() {

        var i = 0
        while (i<enemies.size)
        {
            val p1 = enemies[i]
            val p2 = enemiesDestinations[i]
            val x = if(p1.x>p2.x){
                p1.x - 1
            }else{
                p1.x + 1
            }
            val y = if(p1.y>p2.y){
                p1.y-1
            } else{
                p1.y+1
            }
            enemies[i].x = x
            enemies[i].y = y
            val d = sqrt(pow((1.0*p1.x-p2.x),2.0) + pow(1.0*p1.y-p2.y, 2.0))
            if(d<enemyWidth){
                enemiesDestinations[i]= PointF(
                    randomizer.nextInt((720-enemyWidth).toInt()).toFloat(),
                    randomizer.nextInt((720-enemyWidth).toInt()).toFloat()
                )
            }
            i++
        }
    }

    private fun calcCollisions(){
        playerFires.removeIf {
            it.y<0
        }

        enemyFires.removeIf{
            it.y>720*ratio
        }

        var i=0
        while (i<enemies.size)
        {
            var p1 = enemies[i]
            p1 = PointF(p1.x+enemyWidth/2, p1.y+enemyWidth/2)

            var j=0
            while (j<playerFires.size){
                var p2 = playerFires[j]
                p2 = PointF(p2.x+playerFireWidth/2, p2.y+playerFireWidth/2)

                val d = sqrt(pow((1.0*p1.x-p2.x),2.0) + pow(1.0*p1.y-p2.y, 2.0))
                if(d<playerFireWidth/2+enemyWidth/2){
                    enemies.removeAt(i)
                    enemiesDestinations.removeAt(i)
                    playerFires.removeAt(j)
                    points++
                    i--
                    break
                }
                j++
            }
            i++
        }

        enemyFires.removeIf{
            val p1 = PointF(it.x+playerFireWidth/2, it.y+playerFireWidth/2)

            val p2 = PointF(playerX+playerWidth/2, playerY+playerWidth/2)

            val d = sqrt(pow((1.0*p1.x-p2.x),2.0) + pow(1.0*p1.y-p2.y, 2.0))

            if(d<playerWidth/2+playerFireWidth/2) {
                gameIsOn = false
                true
            }else false
        }
    }

}
//TODO refactor this after persistance will be done
//TODO add comments
//TODO add tests