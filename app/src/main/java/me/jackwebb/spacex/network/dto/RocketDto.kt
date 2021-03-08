package me.jackwebb.spacex.network.dto

import com.google.gson.annotations.SerializedName

class RocketDto(
    @SerializedName("rocket_name")
    val name: String,
    @SerializedName("rocket_type")
    val type: String,
)