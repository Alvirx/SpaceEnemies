/**
 * Project: com.example.spaceenemies.model
 * Name: ProjectilesClasses.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-14
 */
package com.example.spaceenemies.model

/**
 * Abstract class which represents projectile in game.
 */
abstract class Projectile(
    open val id:Long,
    open var x: Float,
    open var y: Float
) {
    abstract fun move()
}

/**
 * Default player projectile. Simply moves up.
 */
class PlayerProjectile(id:Long,x:Float,y:Float) : Projectile(id,x,y) {
    override fun move() {

    }
}

/**
 * Default enemy projectile. Simply moves down.
 */
class EnemyProjectile(id:Long,x:Float,y:Float) : Projectile(id,x,y) {
    override fun move() {

    }
}