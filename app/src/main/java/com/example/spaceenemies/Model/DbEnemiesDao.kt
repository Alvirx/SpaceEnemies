package com.example.spaceenemies.Model

import android.arch.persistence.room.*
import java.util.*

/**
 * Dao interface used for interactions with Room database.
 */
@Dao
internal interface DbEnemiesDao {
    /**
     * Inserts all enemies given as parameters to the database.
     */
    @Insert
    fun insertAllEnemies(vararg dbEnemy: DbEnemy)

    /**
     * Deletes from database all enemies.
     */
    @Query("DELETE FROM enemies")
    fun deleteAllEnemies()


    /**
     * Gets all enemies form database.
     *
     * @return LinkedList containing all enemies in database
     */
    @Query("SELECT * FROM enemies")
    fun getAllEnemies(): List<DbEnemy>

}