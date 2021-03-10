package me.jackwebb.spacex.network.dto

import com.google.gson.annotations.SerializedName
import me.jackwebb.spacex.model.CompanyInfo

class CompanyInfoDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("founder")
    val founderName: String,
    @SerializedName("founded")
    val yearFounded: Int,
    @SerializedName("employees")
    val employees: Int,
    @SerializedName("launch_sites")
    val launchSites: Int,
    @SerializedName("valuation")
    val valuation: Long
) : Mapper<CompanyInfo> {
    override fun toModel() = CompanyInfo(
        name = name,
        founderName = founderName,
        yearFounded = yearFounded,
        employees = employees,
        launchSites = launchSites,
        valuation = valuation
    )
}
