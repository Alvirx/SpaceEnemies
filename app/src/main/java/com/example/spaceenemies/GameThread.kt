package com.example.spaceenemies

import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.SurfaceHolder

/**
 * Thread class that refreshes surface on SurfaceView.
 */
class GameThread(private val holder: SurfaceHolder,
                 private val gameSurface: GameSurface
): Thread() {

    private var running: Boolean = false
    private val targetFPS = 60
    private var canvas: Canvas? = null

    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()
        while (running) {
            startTime = System.nanoTime()
            canvas = null
            try {
                canvas = holder.lockHardwareCanvas()

                synchronized(holder) {
                    gameSurface.update()
                    gameSurface.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }

            timeMillis = (System.nanoTime() - startTime) / 1000000

            waitTime = targetTime - timeMillis

            try {
                sleep(waitTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

//TODO add comments
//TODO add tests