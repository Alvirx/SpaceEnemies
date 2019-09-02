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

open class Entity(var x: Float,
                  var y: Float,
                  val radius:Float) {
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