package com.example.spacex.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Ship(
    @SerialName("ship_id")
    val shipId: String,
    @SerialName("ship_name")
    val shipName: String,
    @SerialName("ship_type")
    val shipType: String,
    @SerialName("home_port")
    val homePort: String,
    @SerialName("image")
    val shipImage: String
)



//@Serializable
//data class Links(
//    @SerialName("mission_patch")
//    val missionPatchUrl: String?,
//    @SerialName("article_link")
//    val articleUrl: String?
//)