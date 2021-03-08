package me.jackwebb.spacex.repo

import me.jackwebb.spacex.model.CompanyInfo
import me.jackwebb.spacex.model.Launch
import me.jackwebb.spacex.network.ApiService
import javax.inject.Inject

class SpaceXRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getCompanyInfo(): CompanyInfo {
        return api.getCompanyInfo().toModel()
    }

    suspend fun getAllLaunches(): List<Launch> {
        return api.getAllLaunches().map { it.toModel() }
    }
}