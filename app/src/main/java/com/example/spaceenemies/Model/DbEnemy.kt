/**
 * Project: com.example.spaceenemies
 * Name: Enemykt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-08
 */
package com.example.spaceenemies.Model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Represents single enemy in Room database.
 */
@Entity(tableName = "enemies")
data class DbEnemy (
    @PrimaryKey(autoGenerate = true) val id:Long,
    @ColumnInfo(name="x") var x: Float,
    @ColumnInfo(name="y") var y: Float,
    @ColumnInfo(name="x_destination") var xDestination: Float,
    @ColumnInfo(name="y_destination") var yDestination: Float,
    @ColumnInfo(name="type") var type: String
)

    
