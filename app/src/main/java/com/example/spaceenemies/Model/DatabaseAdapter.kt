/**
 * Project: com.example.spaceenemies.model
 * Name: DatabaseAdapter.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-14
 */
package com.example.spaceenemies.Model

import android.arch.persistence.room.Room
import android.content.Context
import com.example.spaceenemies.Controller.*
import java.util.*

/**
 * Concrete Adapter which adapts EntitiesDao methods to work with game objects. Converts objects that are intended to be saved to db objects
 * and objects that are intended to be read to game objects. Implements DatabaseAdapterInterface.
 */
class DatabaseAdapter(private val context: Context): DatabaseAdapterInterface {

    private val consts = SpaceEnemiesConsts.getInstance(context)
    private val entitiesDao = buildDatabase(context).getEntitiesDao()

    override fun insertAllEnemies(vararg enemy: Enemy) {
        for(e in enemy){
            val dbEnemy = convertEnemyToDb(e)
            entitiesDao.insertAllEnemies(dbEnemy)
        }
    }

    override fun insertAllProjectiles(vararg projectile: Projectile) {
        for(p in projectile){
            val dbProjectile = convertProjectileToDb(p)
            entitiesDao.insertAllProjectiles(dbProjectile)
        }
    }

    override fun deleteAllEnemies() {
            entitiesDao.deleteAllEnemies()
    }

    override fun deleteAllProjectiles() {
        entitiesDao.deleteAllProjectiles()
    }

    override fun getAllEnemies(): LinkedList<Enemy> {
        val list = LinkedList<Enemy>()
        entitiesDao.getAllEnemies().forEach {
            list.add(convertEnemyFromDb(it))
        }
        return list
    }

    override fun getAllProjectiles(): LinkedList<Projectile> {
        val list = LinkedList<Projectile>()
        entitiesDao.getAllProjectiles().forEach {
            list.add(convertProjectileFromDb(it))
        }
        return list
    }


    private fun convertEnemyFromDb(dbEnemy:DbEnemy) : Enemy {
        return BasicEnemy(
            dbEnemy.x,
            dbEnemy.y,
            dbEnemy.xDestination,
            dbEnemy.yDestination,
            consts.enemy,
            consts.enemyWidth/2,
            consts.enemySpeed
        )
    }

    private fun convertProjectileFromDb(dbProjectile: DbProjectile) : Projectile {
        return if(dbProjectile.type=="player")
            PlayerProjectile( dbProjectile.x, dbProjectile.y, consts.playerFire, consts.projectileWidth/2, consts.projectileSpeed)
        else
            EnemyProjectile(dbProjectile.x, dbProjectile.y, consts.enemyFire, consts.projectileWidth/2, consts.projectileSpeed)
    }

    private fun convertEnemyToDb(enemy: Enemy) : DbEnemy{
        return DbEnemy(
            1,
            enemy.x,
            enemy.y,
            enemy.xDestination,
            enemy.yDestination,
            "basic"
        )
    }

    private fun convertProjectileToDb(projectile: Projectile): DbProjectile{
        val type = if(projectile is EnemyProjectile)
            "enemy"
        else
            "player"

        return DbProjectile(1, projectile.x, projectile.y, type)

    }

    /**
     * Builds database via Room framework with given context.
     *
     * @param context - given context
     * @return built database
     */
    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(context.applicationContext,
            SpaceEnemiesDatabase::class.java, "task.db")
            .build()
}