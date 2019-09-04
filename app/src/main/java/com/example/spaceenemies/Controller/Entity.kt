/**
 * Project: com.example.spaceenemies.Controller
 * Name: Entity.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-31
 */
package com.example.spaceenemies.Controller

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Class to be derived from by entities in game. Provides collides method.
 */
open class Entity(var x: Float,
                  var y: Float,
                  val radius:Float) {
    /**
     * Checks if collision between two entities occurred.
     * @param other - other entity to be checked.
     */
    fun collides(other:Entity):Boolean{
        val myX = x+radius
        val myY = y+radius
        val otherX = other.x+other.radius
        val otherY = other.y+other.radius

        val distance = sqrt(
            (myX - otherX).pow(2) + (myY-otherY).pow(2)
        )
        return distance<this.radius+other.radius
    }
}