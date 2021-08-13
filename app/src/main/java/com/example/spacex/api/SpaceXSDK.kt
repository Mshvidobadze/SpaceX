package com.example.spacex.api

import android.widget.Toast
import com.example.spacex.entity.Database
import com.example.spacex.entity.DatabaseDriverFactory
import com.example.spacex.entity.Ship
import com.example.sql.Mission
import io.ktor.client.request.*
import kotlin.coroutines.coroutineContext


class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getShips(forceReload: Boolean): List<Ship> {

        val cachedShips = database.selectAllShips()


        return if (cachedShips.isNotEmpty() && !forceReload) {
            cachedShips
        } else {



            api.getAllShips().also {
                database.clearDatabase()

                database.createShip(it)

            }
        }

    }
    @Throws(Exception::class) suspend fun getMissions(forceReload: Boolean){

        api.getAllMissions(database)

    }
}
