/**
 * Project: com.example.spaceenemies.model
 * Name: ProjectilesClasses.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-14
 */
package com.example.spaceenemies.Controller

import android.graphics.Bitmap

/**
 * Abstract class which represents projectile in game.
 */
abstract class Projectile(
    x: Float,
    y: Float,
    val bitmap: Bitmap,
    radius:Float,
    val speed:Float
) : Entity(x,y,radius) {
    abstract fun move()


}

/**
 * Default player projectile. Simply moves up.
 */
class PlayerProjectile(x:Float, y:Float, bitmap: Bitmap, radius: Float, speed:Float) : Projectile(x,y, bitmap, radius, speed) {
    override fun move() {
        y-=speed
    }
}

/**
 * Default enemy projectile. Simply moves down.
 */
class EnemyProjectile(x:Float, y:Float, bitmap: Bitmap, radius: Float, speed:Float) : Projectile(x,y, bitmap, radius, speed) {
    override fun move() {
        y+=speed
    }
}