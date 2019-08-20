/**
 * Project: com.example.spaceenemies.model
 * Name: EnemiesClasses.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-14
 */
package com.example.spaceenemies.model

/**
 * Abstract class which represents enemy in game.
 */
abstract class Enemy(
    val id:Long,
    var x: Float,
    var y: Float,
    var xDestination: Float,
    var yDestination: Float
) {
    abstract fun move()
}

/**
 * Default enemy in game. It randomly generates a point in the top part of the screen and moves to that point. When point is reached new point is randomly generated.
 */
class BasicEnemy(id:Long, x: Float, y: Float, xDestination: Float, yDestination: Float) : Enemy(id,x,y,xDestination,yDestination) {
    /**
     * Moves enemy towards destination point.
     */
    override fun move() {

    }
}