package com.example.spaceenemies.Model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface DbProjectileDao {

    /**
     * Gets all projectiles form database.
     *
     * @return LinkedList containing all projectiles in database
     */
    @Query("SELECT * FROM projectiles")
    fun getAllProjectiles(): List<DbProjectile>

    /**
     * Deletes from database all projectiles
     */
    @Query("DELETE FROM projectiles")
    fun deleteAllProjectiles()

    /**
     * Inserts all Projectiles given as parameters to the database.
     */
    @Insert
    fun insertAllProjectiles(vararg dbProjectile: DbProjectile)

}
