package com.example.spaceenemies.Model

import android.arch.persistence.room.*
import java.util.*

/**
 * Dao interface used for interactions with Room database.
 */
@Dao
internal interface EntitiesDao {
    /**
     * Inserts all enemies given as parameters to the database.
     */
    @Insert
    fun insertAllEnemies(vararg dbEnemy: DbEnemy)

    /**
     * Inserts all Projectiles given as parameters to the database.
     */
    @Insert
    fun insertAllProjectiles(vararg dbProjectile: DbProjectile)

    /**
     * Deletes from database all enemies.
     */
    @Query("DELETE FROM enemies")
    fun deleteAllEnemies()

    /**
     * Deletes from database all projectiles
     */
    @Query("DELETE FROM projectiles")
    fun deleteAllProjectiles()

    /**
     * Gets all enemies form database.
     *
     * @return LinkedList containing all enemies in database
     */
    @Query("SELECT * FROM enemies")
    fun getAllEnemies(): LinkedList<DbEnemy>

    /**
     * Gets all projectiles form database.
     *
     * @return LinkedList containing all projectiles in database
     */
    @Query("SELECT * FROM projectiles")
    fun getAllProjectiles(): LinkedList<DbProjectile>


}