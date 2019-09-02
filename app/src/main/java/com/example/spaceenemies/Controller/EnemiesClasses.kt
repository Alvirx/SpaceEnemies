/**
 * Project: com.example.spaceenemies.model
 * Name: EnemiesClasses.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-14
 */
package com.example.spaceenemies.Controller

import android.graphics.Bitmap
import com.example.spaceenemies.Model.SpaceEnemiesConsts
import java.util.*

/**
 * Abstract class which represents enemy in game.
 */
abstract class Enemy(
    x: Float,
    y: Float,
    var xDestination: Float,
    var yDestination: Float,
    val bitmap: Bitmap,
    radius: Float,
    val speed: Float
) : Entity(x,y,radius) {
    abstract fun move(random:Random)
}

/**
 * Default enemy in game. It randomly generates a point in the top part of the screen and moves to that point. When point is reached new point is randomly generated.
 */
class BasicEnemy(x: Float, y: Float, xDestination: Float, yDestination: Float, bitmap: Bitmap, radius: Float, speed: Float) : Enemy(x,y,xDestination,yDestination, bitmap, radius, speed) {
    /**
     * Moves enemy towards destination point.
     */
    override fun move(random:Random) {
        val eps = 10f
        var moved = true

        when {
            x+eps<xDestination -> x+=speed
            x-eps>xDestination -> x-=speed
            else -> moved = false
        }

        if(y+eps<yDestination) y+=speed
        else if(y-eps>yDestination) y-=speed
        else if(!moved) {
            xDestination = random.nextInt(720).toFloat()
            yDestination = random.nextInt(720).toFloat()
        }

    }
}