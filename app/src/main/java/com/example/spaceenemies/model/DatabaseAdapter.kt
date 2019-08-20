/**
 * Project: com.example.spaceenemies.model
 * Name: DatabaseAdapter.kt
 * Purpose:
 *
 * @author Bartosz Gorski
 * @date 2019-08-14
 */
package com.example.spaceenemies.model

import android.arch.persistence.room.Room
import android.content.Context
import java.util.*

/**
 * Concrete Adapter which adapts EntitiesDao methods to work with game objects. Converts objects that are intended to be saved to db objects
 * and objects that are intended to be read to game objects. Implements DatabaseAdapterInterface.
 */
class DatabaseAdapter(context: Context): DatabaseAdapterInterface {

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

    override fun deleteAllEnemies(vararg enemy: Enemy) {
        for(e in enemy){
            val dbEnemy = convertEnemyToDb(e)
            entitiesDao.deleteAllEnemies(dbEnemy)
        }
    }

    override fun deleteAllProjectiles(vararg projectile: Projectile) {
        for(p in projectile){
            val dbProjectile = convertProjectileToDb(p)
            entitiesDao.deleteAllProjectiles(dbProjectile)
        }
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

    override fun updateAllEnemies(vararg enemy: Enemy) {
        for(e in enemy){
            val dbEnemy = convertEnemyToDb(e)
            entitiesDao.updateAllEnemies(dbEnemy)
        }
    }

    override fun updateAllProjectiles(vararg projectile: Projectile) {
        for(p in projectile){
            val dbProjectile = convertProjectileToDb(p)
            entitiesDao.updateAllProjectiles(dbProjectile)
        }
    }

    private fun convertEnemyFromDb(dbEnemy:DbEnemy) : Enemy{
        return BasicEnemy(
            dbEnemy.id,
            dbEnemy.x,
            dbEnemy.y,
            dbEnemy.xDestination,
            dbEnemy.yDestination)
    }

    private fun convertProjectileFromDb(dbProjectile: DbProjectile) : Projectile{
        return if(dbProjectile.type=="player")
            PlayerProjectile(dbProjectile.id,dbProjectile.x,dbProjectile.y)
        else
            EnemyProjectile(dbProjectile.id,dbProjectile.x,dbProjectile.y)
    }

    private fun convertEnemyToDb(enemy:Enemy) : DbEnemy{
        return DbEnemy(
            enemy.id,
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

        return DbProjectile(projectile.id, projectile.x, projectile.y, type)

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