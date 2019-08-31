/**
 * Project: com.example.spaceenemies.Controller
 * Name: Player.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-31
 */
package com.example.spaceenemies.Controller

import android.graphics.Bitmap

class Player(x:Float, y:Float, radius:Float, val bitmap: Bitmap, private val speed:Float) : Entity(x,y,radius) {
    fun moveLeft(){
        if(x>0) x-=speed
    }
    fun moveRight(){
        if(x+2*radius<720) x+=speed
    }
}