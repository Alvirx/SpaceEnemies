package com.example.spaceenemies.model

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
     * Deletes from database all enemies given as parameters.
     */
    @Delete
    fun deleteAllEnemies(vararg dbEnemy: DbEnemy)

    /**
     * Deletes from database all projectiles given as parameters.
     */
    @Delete
    fun deleteAllProjectiles(dbProjectile: DbProjectile)

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

    /**
     * Updates all enemies in database given as parameters.
     */
    @Update
    fun updateAllEnemies(vararg dbEnemy: DbEnemy)

    /**
     * Updates all projectiles in database given as parameters.
     */
    @Update
    fun updateAllProjectiles(vararg dbProjectile: DbProjectile)
}