package com.example.spacex.api

import com.example.spacex.Database
import com.example.spacex.entity.DatabaseDriverFactory
import com.example.spacex.entity.Ship
import com.example.sql.Mission
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONException

import org.json.JSONObject


class SpaceXApi {
    private val httpClient = HttpClient {

        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            }


            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun getAllShips(): List<Ship> {
        val response : HttpResponse = httpClient.get(LAUNCHES_ENDPOINT)
//        println(response.readText())


        val json = JSONObject(response.readText())

        val missions: JSONArray = json.getJSONArray("missions")
        val jsonArray: JSONArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            println(item.getString("ship_id"))
        }
        return httpClient.get(LAUNCHES_ENDPOINT)
    }

    suspend fun getAllMissions(database: com.example.spacex.entity.Database){
        val response : HttpResponse = httpClient.get(LAUNCHES_ENDPOINT)

        val json = JSONObject(response.readText())
        val jsonArray = JSONArray(json)

        var missionsArray : ArrayList<String> = ArrayList()

//        val missions: JSONArray = json.getJSONArray("missions")
//        val shipId = jsonArray.getJSONObject(())

        for (i in 0 until jsonArray.length()) {
            val shipId = jsonArray.getJSONObject(i).getString("ship_id")
            val missions = jsonArray.getJSONObject(i).getJSONArray("missions")

            for (x in 0 until missions.length()){
                missionsArray.clear()
               missionsArray.add(shipId)
                missionsArray.add(missions.getJSONObject(x).getString("mission_name"))
                missionsArray.add(missions.getJSONObject(x).getString("wikipedia"))
                missionsArray.add(missions.getJSONObject(x).getString("website"))
                missionsArray.add(missions.getJSONObject(x).getString("twitter"))
                missionsArray.add(missions.getJSONObject(x).getString("description"))
                database.createMission(missionsArray)
            }
        }

    }

    companion object {
        private const val LAUNCHES_ENDPOINT = "https://api.spacexdata.com/v3/ships"
    }
}

