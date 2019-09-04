package com.example.spaceenemies.Controller

import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Build
import android.os.SystemClock
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.SurfaceHolder
import com.example.spaceenemies.Model.PersistenceFacadeInterface
import com.example.spaceenemies.Model.PersistentFacade
import com.example.spaceenemies.Model.SpaceEnemiesConsts
import com.example.spaceenemies.View.GameSurface
import java.util.*

/**
 * Main Game thread which refreshes surface on SurfaceView and updates itself.
 */
class GameThread(private val holder: SurfaceHolder,
                 private val gameSurface: GameSurface
): Thread() {

    companion object{
        private const val TAG = "GameThread"
    }


    private var running: Boolean = false
    private val random = Random()
    private val targetFPS = 60
    private var canvas: Canvas? = null
    private val consts = SpaceEnemiesConsts.getInstance(gameSurface.context)

    //Points in game
    var points = 0
        private set

    //In game counter for spawning entities
    private var counter = 0

    //Flag if game is currently on
    var gameIsOn = false
    private set

    //Controls
    var touchedX = 0
    var touchedY = 0
    var touched:Boolean = false


    /////////////BEGIN ENTITIES////////////////
    val player = Player(0f,0f, consts.playerWidth/2,consts.playerShip, consts.playerSpeed)

    //List of projectiles in game
    private var projectiles: LinkedList<Projectile> = LinkedList()

    //List of enemies in game
    private var enemies: LinkedList<Enemy> = LinkedList()

    /////////////END ENTITIES/////////////////


    /**
     * Projectiles getter
     * @return readonly list of projectiles
     */
    fun getProjectiles() = projectiles.toList()

    /**
     * Enemies getter
     * @return readonly list of enemies
     */
    fun getEnemies() = enemies.toList()

    /**
     * Setter for running flag. If true thread will work after start() method until it will be changed to false.
     * @param isRunning - boolean value to be set
     */
    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    /**
     * Saves game state to persistence storage
     */
    fun saveGameState(){
        val persistentFacade: PersistenceFacadeInterface = PersistentFacade.getInstance(gameSurface.context)
        var done = false
        AsyncTask.execute{
            if(gameIsOn){
                persistentFacade.deleteAllEnemies()
                persistentFacade.deleteAllProjectiles()

                enemies.forEach{
                    persistentFacade.saveAllEnemies(it)
                }

                projectiles.forEach{
                    persistentFacade.saveAllProjectiles(it)
                }

                persistentFacade.saveGameState(counter, points, true)
                persistentFacade.savePlayer(player.x, player.y)

            }else {
                persistentFacade.saveGameState(0,0,false)
            }
            done = true
        }
        while (!done) SystemClock.sleep(1)

    }

    /**
     * Initializes game before start
     */
    fun initializeGame() {
        running = true
        gameIsOn = PersistentFacade.getInstance(gameSurface.context).isGameOn()
        if(gameIsOn){
            Log.d(TAG,"Przywracamy stan")
            resumeOldGame()
        }else{
            startNewGame()
            Log.d(TAG,"Startujemy nowa gre")
        }
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

    private fun update(){

        if(!gameIsOn && touched)
            startNewGame()


        if(gameIsOn){
            spawnEntities()
            calcCollisions()
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
                            points++
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
        points = 0
        counter=0
        enemies = LinkedList()
        projectiles = LinkedList()
        player.y = (720*1.5 - 4*player.radius).toFloat()
        player.x = 720/2 - player.radius
    }

    private fun resumeOldGame(){
        val persistentFacade: PersistenceFacadeInterface = PersistentFacade.getInstance(gameSurface.context)
        AsyncTask.execute{
            points = persistentFacade.getPoints()
            counter = persistentFacade.getCounter()
            enemies = persistentFacade.getAllEnemies()
            projectiles = persistentFacade.getAllProjectiles()
            player.x = persistentFacade.getPlayerX()
            player.y = persistentFacade.getPlayerY()
        }



    }

}