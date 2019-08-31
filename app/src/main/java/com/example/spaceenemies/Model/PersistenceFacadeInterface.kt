package com.example.spaceenemies.Model

import com.example.spaceenemies.Controller.Enemy
import com.example.spaceenemies.Controller.Projectile
import java.util.*

/**
 * Facade interface which provides simple way for saving and reading persistence data.
 */
interface PersistenceFacadeInterface {
    /**
     * @return saved enemies
     */
    fun getAllEnemies() : LinkedList<Enemy>

    /**
     * @return saved projectiles
     */
    fun getAllProjectiles() : LinkedList<Projectile>

    /**
     * Saves enemies given as params.
     * @param enemy - enemies to be saved.
     */
    fun saveAllEnemies(vararg enemy: Enemy)

    /**
     * Saves projectiles given as params.
     * @param projectile - projectiles to be saved.
     */
    fun saveAllProjectiles(vararg projectile: Projectile)


    /**
     * Deletes enemies given as params.
     * @param enemy - enemies to be deleted
     */
    fun deleteAllEnemies()

    /**
     * Deletes projectiles given as params.
     * @param projectile - projectiles to be deleted
     */
    fun deleteAllProjectiles()

    /**
     * Saves player position
     * @param x - x-coordinate to be saved
     * @param y - y-coordinate to be saved
     */
    fun savePlayer(x:Float, y:Float)

    /**
     * Gets player`s x-coordinate
     * @return player`s x-coordinate
     */
    fun getPlayerX():Float

    /**
     * Gets player`s y-coordinate
     * @return player`s y-coordinate
     */
    fun getPlayerY():Float

    /**
     * Saves game state.
     * @param counter - in game counter to be saved
     * @param points - current points in game to be saved
     * @param gameIsOn - flag that represents if game is currently on to be saved.
     */
    fun saveGameState(counter:Int, points:Int, gameIsOn:Boolean)

    /**
     * Gets in game counter
     * @return saved counter
     */
    fun getCounter():Int

    /**
     * Gets in game points
     * @return saved points
     */
    fun getPoints():Int

    /**
     * Gets isGameOn flag
     * @return saved flag
     */
    fun isGameOn():Boolean

}

