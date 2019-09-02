package com.example.spaceenemies.Controller

import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.SurfaceHolder
import com.example.spaceenemies.Model.SpaceEnemiesConsts
import com.example.spaceenemies.View.GameSurface
import java.util.*

/**
 * Thread class that refreshes surface on SurfaceView.
 */
class GameThread(private val holder: SurfaceHolder,
                 private val gameSurface: GameSurface
): Thread() {

    companion object{
        private const val TAG = "GameThread"
    }

    private var running: Boolean = false
    private val targetFPS = 60
    private var canvas: Canvas? = null
    private val consts = SpaceEnemiesConsts.getInstance(gameSurface.context)
    val player = Player(0f,0f, consts.playerWidth/2,consts.playerShip, consts.playerSpeed)

    private val random = Random()

    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()
        while (running) {
            startTime = System.nanoTime()
            canvas = null
            try {
                canvas = holder.lockHardwareCanvas()

                synchronized(holder) {
                    update()
                    gameSurface.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }

            timeMillis = (System.nanoTime() - startTime) / 1000000

            waitTime = targetTime - timeMillis

            try {
                sleep(waitTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Coordinates and game state

    private var projectiles: LinkedList<Projectile> = LinkedList()
    fun getProjectiles() = projectiles.toList()


    private var enemies: LinkedList<Enemy> = LinkedList()
    fun getEnemies() = enemies.toList()

    var points = 0
        private set

    private var counter = 0


    var gameIsOn = false

    //Controls
    var touchedX = 0
    var touchedY = 0
    var touched:Boolean = false


    private fun update(){

        if(!gameIsOn && touched)
            startNewGame()


        if(gameIsOn){

            spawnEntities()
            Log.d(TAG, "Poczatek update")
            calcCollisions()
            Log.d(TAG, "Koniec update")

            moveEntities()

        }


    }

    private fun spawnEntities() {
        if(counter==0){
            if (random.nextFloat()>0.15 && enemies.size<30){
                enemies.add(
                    BasicEnemy(
                        random.nextInt(720-consts.enemyWidth.toInt()).toFloat(),
                        -consts.enemyWidth,
                        random.nextInt(720-consts.enemyWidth.toInt()).toFloat(),
                        random.nextInt(720-consts.enemyWidth.toInt()).toFloat(),
                        consts.enemy,
                        consts.enemyWidth/2,
                        consts.enemySpeed
                    )
                )

            }
            enemies.forEach{
                if (random.nextFloat()>0.90){

                    projectiles.add(
                        EnemyProjectile(
                            it.x+ consts.enemyWidth /2,
                            it.y+ consts.enemyWidth,
                            consts.enemyFire,
                            consts.projectileWidth/2,
                            consts.projectileSpeed
                        )
                    )
                }
            }

            projectiles.add(
                PlayerProjectile(
                    player.x+(player.radius)-consts.projectileWidth /2,
                    player.y-20,
                    consts.playerFire,
                    consts.projectileWidth/2,
                    consts.projectileSpeed
                )
            )
        }
        counter = (counter+1)%15
    }

    private fun moveEntities(){
        movePlayer()
        enemies.forEach{
            it.move(random)
        }
        projectiles.forEach{
            it.move()
        }
    }

    private fun movePlayer() {
        if(touched){
            if(touchedX>player.x+10){
                player.moveRight()
            }
            else if(touchedX<player.x-10){
                player.moveLeft()
            }
        }
    }

    private fun calcCollisions(){
        var i = 0
        while(i < projectiles.size){
            val p = projectiles[i]
            if(p is PlayerProjectile){
                if(p.y<-10){
                    projectiles.removeAt(i)
                    i--
                }
                else{
                    for(j in 0 until enemies.size){
                        val e = enemies[j]
                        if(p.collides(e)){
                            projectiles.removeAt(i)
                            enemies.removeAt(j)
                            i--
                            break
                        }
                    }
                }
            }
            else{
                if(p.collides(player)){
                    projectiles.removeAt(i)
                    i--
                    gameIsOn = false
                } else if(p.y>1.8*720) {
                    projectiles.removeAt(i)
                    i--
                }

            }
            i++
        }
    }


    private fun startNewGame(){
        gameIsOn = true
        running = true
        points = 0
        enemies = LinkedList()
        projectiles = LinkedList()
        player.y = (720*1.5 - 4*player.radius).toFloat()
        player.x = 720/2 - player.radius
    }

    private fun resumeOldGame(){

    }

    fun initializeGame() {
        startNewGame()
    }
}

//TODO add comments
//TODO add tests