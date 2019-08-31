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
        val distance = sqrt(
            (this.x - other.x).pow(2) + (this.y-other.y).pow(2)
        )
        return distance<this.radius+other.radius
    }
}