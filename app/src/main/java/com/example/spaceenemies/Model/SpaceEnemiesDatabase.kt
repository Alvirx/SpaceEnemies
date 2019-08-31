/**
 * Project: com.example.spaceenemies
 * Name: SpaceEnemiesDatabase.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-08
 */
package com.example.spaceenemies.Model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * This class is responsible in connecting with database and provides the instance of itself via Singleton pattern
 */
@Database(entities = [DbEnemy::class], version = 1)
internal abstract class SpaceEnemiesDatabase : RoomDatabase() {

    /**
     * Provides interface for interactions with entities table in database.
     * @return entityDao interface
     */
    abstract fun getEntitiesDao(): EntitiesDao

}