package me.jackwebb.spacex.model

import org.joda.time.DateTime
import org.joda.time.Days

data class Launch(
    val missionName: String,
    val dateTime: DateTime,
    val rocketName: String,
    val rocketType: String,
    val successful: Boolean,
    val missionPatchUrl: String?,
    val links: Links
) {
    val daysDelta: Int
        get() = Days.daysBetween(dateTime, DateTime.now()).days
}
