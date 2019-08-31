/**
 * Project: com.example.spaceenemies
 * Name: PersistentFacade.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-10
 */
package com.example.spaceenemies.Model

import android.content.Context
import android.preference.PreferenceManager
import com.example.spaceenemies.Controller.Enemy
import com.example.spaceenemies.Controller.Projectile
import java.util.*

/**
 * Concrete PersistenceFacade which implements PersistenceFacadeInterface. Also works as singleton.
 */
class PersistentFacade private constructor(context:Context) : PersistenceFacadeInterface {


    private val databaseAdapter:DatabaseAdapterInterface = DatabaseAdapter(context)

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {

        @Volatile private var INSTANCE: PersistentFacade? = null

        /**
         *if the instance does not exists before, creates one and returns it,
         * else just returns instance
         *
         * @param context - given context
         * @return instance of this class
         */
        fun getInstance(context: Context): PersistentFacade =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: PersistentFacade(context).also { INSTANCE = it }
            }
    }

    override fun getAllEnemies(): LinkedList<Enemy> {
        return databaseAdapter.getAllEnemies()
    }

    override fun getAllProjectiles(): LinkedList<Projectile> {
        return databaseAdapter.getAllProjectiles()
    }

    override fun saveAllEnemies(vararg enemy: Enemy) {
        databaseAdapter.insertAllEnemies(*enemy)
    }

    override fun deleteAllEnemies() {
        databaseAdapter.deleteAllEnemies()
    }

    override fun deleteAllProjectiles() {
        databaseAdapter.deleteAllProjectiles()
    }

    override fun saveAllProjectiles(vararg projectile: Projectile) {
        databaseAdapter.insertAllProjectiles(*projectile)
    }

    override fun savePlayer(x: Float, y: Float) {
        with(sharedPreferences.edit()){
            putFloat("PlayerX", x)
            putFloat("PlayerY", y)
            apply()
        }
    }

    override fun saveGameState(counter: Int, points: Int, gameIsOn: Boolean) {
        with(sharedPreferences.edit()){
            putInt("Counter", counter)
            putInt("Points", points)
            putBoolean("GameIsOn", gameIsOn)
            apply()
        }
    }

    override fun getCounter(): Int {
        return sharedPreferences.getInt("Counter", 0)
    }

    override fun getPoints(): Int {
        return sharedPreferences.getInt("Points", 0)
    }

    override fun isGameOn(): Boolean {
        return  sharedPreferences.getBoolean("GameIsOn", false)
    }

    override fun getPlayerX(): Float{
        return sharedPreferences.getFloat("PlayerX", 0f)
    }

    override fun getPlayerY(): Float {
        return sharedPreferences.getFloat("PlayerY", 0f)
    }
}