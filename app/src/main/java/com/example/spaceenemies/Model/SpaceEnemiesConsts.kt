/**
 * Project: com.example.spaceenemies.Model
 * Name: SpaceEnemiesConstsnemiesConsts.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-29
 */
package com.example.spaceenemies.Model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.spaceenemies.R
import java.util.*

/**
 * Singleton class for different in game constant values.
 */
class SpaceEnemiesConsts private constructor(context:Context){
    val playerWidth = 70f
    val enemyWidth = 70f
    val projectileWidth = 10f
    val projectileSpeed = 10f
    val playerSpeed = 6f
    val enemySpeed = 0.5f

    /**
     * Bitmap for basic enemies
     */
    val enemy = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.resources, R.drawable.enemy),
        enemyWidth.toInt(),
        enemyWidth.toInt(),
        false
    )

    /**
     * Bitmap for player projectiles
     */
    val playerFire = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.resources, R.drawable.playerfire),
        projectileWidth.toInt(),
        projectileWidth.toInt(),
        false
    )


    /**
     * Bitmap for enemy projectiles
     */
    val enemyFire = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.resources, R.drawable.enemyfire),
        projectileWidth.toInt(),
        projectileWidth.toInt(),
        false
    )

    /**
     * Bitmap for player ship
     */
    val playerShip = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.resources, R.drawable.player),
        playerWidth.toInt(),
        playerWidth.toInt(),
        false
    )

    companion object {

        @Volatile private var INSTANCE: SpaceEnemiesConsts? = null

        /**
         *if the instance does not exists before, creates one and returns it,
         * else just returns instance
         *
         * @param context - given context
         * @return instance of this class
         */
        fun getInstance(context: Context): SpaceEnemiesConsts =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: SpaceEnemiesConsts(context).also { INSTANCE = it }
            }
    }




}