package me.jackwebb.spacex.network.dto

import com.google.gson.annotations.SerializedName
import me.jackwebb.spacex.model.Launch
import org.joda.time.DateTime

class LaunchDto(
    @SerializedName("mission_name")
    val missionName: String,
    @SerializedName("launch_date_utc")
    val launchDateUtc: DateTime,
    @SerializedName("rocket")
    val rocketDto: RocketDto,
    @SerializedName("launch_success")
    val launchSuccess: Boolean,
    @SerializedName("links")
    val linksDto: LinksDto
) : Mapper<Launch> {
    override fun toModel() = Launch(
        missionName = missionName,
        dateTime = launchDateUtc,
        rocketName = rocketDto.name,
        rocketType = rocketDto.type,
        successful = launchSuccess,
        missionPatchUrl = linksDto.missionPatch,
        links = linksDto.toModel()
    )
}
