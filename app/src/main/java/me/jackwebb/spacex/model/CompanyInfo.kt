package me.jackwebb.spacex.model

data class CompanyInfo(
    val name: String,
    val founderName: String,
    val yearFounded: Int,
    val employees: Int,
    val launchSites: Int,
    val valuation: Long
)