package me.jackwebb.spacex.model

data class Links(
    val articleUrl: String?,
    val videoUrl: String?,
    val wikipediaUrl: String?,
) {
    val asTypedList: List<Pair<String, LinkType>>
        get() = listOf(
            this.articleUrl to LinkType.ARTICLE,
            this.videoUrl to LinkType.VIDEO,
            this.wikipediaUrl to LinkType.WIKIPEDIA,
        ).filterNot { it.first == null } as List<Pair<String, LinkType>>

    enum class LinkType { ARTICLE, VIDEO, WIKIPEDIA }
}
