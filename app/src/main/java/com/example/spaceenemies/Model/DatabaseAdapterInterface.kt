package com.example.spaceenemies.Model

import com.example.spaceenemies.Controller.Enemy
import com.example.spaceenemies.Controller.Projectile
import java.util.*

/**
 * Adapts objects that program uses to be writable in database and saves them there. Also Adapts read objects from database to objects that program uses.
 */
interface DatabaseAdapterInterface {

    /**
     * Inserts all enemies given as parameters to the database.
     */
    fun insertAllEnemies(vararg enemy: Enemy)

    /**
     * Inserts all Projectiles given as parameters to the database.
     */
    fun insertAllProjectiles(vararg projectile: Projectile)

    /**
     * Deletes from database all enemies.
     */
    fun deleteAllEnemies()

    /**
     * Deletes from database all projectiles.
     */
    fun deleteAllProjectiles()

    /**
     * Gets all enemies form database.
     *
     * @return LinkedList containing all enemies in database
     */
    fun getAllEnemies(): LinkedList<Enemy>

    /**
     * Gets all projectiles form database.
     *
     * @return LinkedList containing all projectiles in database
     */
    fun getAllProjectiles(): LinkedList<Projectile>

}