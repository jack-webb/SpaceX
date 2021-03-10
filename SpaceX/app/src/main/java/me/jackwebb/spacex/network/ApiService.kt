package me.jackwebb.spacex.network

import me.jackwebb.spacex.network.dto.CompanyInfoDto
import me.jackwebb.spacex.network.dto.LaunchDto
import retrofit2.http.GET

interface ApiService {
    @GET("info")
    suspend fun getCompanyInfo(): CompanyInfoDto

    @GET("launches")
    suspend fun getAllLaunches(): List<LaunchDto>
}