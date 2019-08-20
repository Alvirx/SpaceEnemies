package com.example.spaceenemies.model

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
     * Deletes from database all enemies given as parameters.
     */
    fun deleteAllEnemies(vararg enemy: Enemy)

    /**
     * Deletes from database all projectiles given as parameters.
     */
    fun deleteAllProjectiles(vararg projectile: Projectile)

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

    /**
     * Updates all enemies in database given as parameters.
     */
    fun updateAllEnemies(vararg enemy: Enemy)

    /**
     * Updates all projectiles in database given as parameters.
     */
    fun updateAllProjectiles(vararg projectile: Projectile)
}