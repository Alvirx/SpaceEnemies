/**
 * Project: com.example.spaceenemies
 * Name: DbProjectile.ktle.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-10
 */
package com.example.spaceenemies.Model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Represents single projectile in Room Database
 */
@Entity(tableName = "projectiles")
data class DbProjectile (
    @PrimaryKey(autoGenerate = true) val id:Long,
    @ColumnInfo(name="x") var x: Float,
    @ColumnInfo(name="y") var y: Float,
    @ColumnInfo(name="type") var type: String
)