package com.example.spacex.entity

import com.example.spacex.Database
import com.example.sql.Mission

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllShips()
        }
    }

    internal fun selectAllShips(): List<Ship> {

        return dbQuery.selectAllShips(::mapShipSelecting).executeAsList()
    }

    private fun mapShipSelecting(
        shipId: String,
        shipName: String,
        shipType: String,
        homePort: String,
        image: String

    ): Ship {
        return Ship(
            shipId = shipId,
            shipName = shipName,
            shipType = shipType,
            homePort = homePort,
            shipImage = image
        )
    }

    internal fun createShip(ships: List<Ship>) {

        dbQuery.transaction {
            ships.forEach { ship ->
                val ship = dbQuery.selectShipById(ship.shipId).executeAsOneOrNull()

                if (ship != null) {
                    insertShip(ship)
                }

            }
        }
    }

    private fun insertShip(ship: com.example.sql.Ship?) {
        if (ship != null) {
            dbQuery.insertShip(
                shipId = ship.shipId,
                shipName = ship.shipName,
                shipType = ship.shipType,
                homePort = ship.homePort,
                shipImage = ship.shipImage
            )
        }
    }
    internal fun createMission(missions: ArrayList<String>) {

        dbQuery.transaction {
            missions.forEach { ship ->

                    insertMission(missions)

            }
        }
    }

    private fun insertMission(missions: ArrayList<String>) {
        if (missions.isNotEmpty()) {
            dbQuery.insertMission(
                shipId = missions[0],
                missionName = missions[1],
                missionWikipedia = missions[2],
                missionWebsite = missions[3],
                missionTwitter = missions[4],
                missionDescription = missions[5]
            )
        }
    }

    internal fun selectMissionsById(shipId: String): List<Mission> {

        return dbQuery.selectMissionByShipId(shipId).executeAsList()
    }

    internal fun selectMissionsByShipName(missionName: String): List<Mission> {

        return dbQuery.selectMissionByName(missionName).executeAsList()
    }


}