package me.jackwebb.spacex.network.dto

import com.google.gson.annotations.SerializedName
import me.jackwebb.spacex.model.Links

class LinksDto(
    @SerializedName("mission_patch")
    val missionPatch: String?,
    @SerializedName("article_link")
    val article: String?,
    @SerializedName("video_link")
    val video: String?,
    @SerializedName("wikipedia")
    val wikipedia: String?,
) : Mapper<Links> {
    override fun toModel() = Links(
        articleUrl = article,
        videoUrl = video,
        wikipediaUrl = wikipedia
    )
}