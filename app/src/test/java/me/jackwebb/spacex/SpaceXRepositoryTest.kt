package me.jackwebb.spacex

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import me.jackwebb.spacex.model.CompanyInfo
import me.jackwebb.spacex.model.Launch
import me.jackwebb.spacex.model.Links
import me.jackwebb.spacex.network.ApiService
import me.jackwebb.spacex.network.dto.CompanyInfoDto
import me.jackwebb.spacex.network.dto.LaunchDto
import me.jackwebb.spacex.network.dto.LinksDto
import me.jackwebb.spacex.network.dto.RocketDto
import me.jackwebb.spacex.repo.SpaceXRepository
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SpaceXRepositoryTest : BaseUnitTest() {

    @MockK(relaxed = true)
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        coEvery { apiService.getCompanyInfo() } returns companyInfoDto
        coEvery { apiService.getAllLaunches() } returns launchDtos
    }

    private fun getRepository() = SpaceXRepository(apiService)

    @Test
    fun `Test get company info to model`() {
        val repo = getRepository()

        runBlockingTest { assertEquals(expectedCompanyModel, repo.getCompanyInfo()) }
        coVerify(exactly = 1) { apiService.getCompanyInfo() }
    }

    @Test
    fun `Test get launches to model`() {
        val repo = getRepository()

        runBlockingTest { assertEquals(expectedLaunches, repo.getAllLaunches()) }
        coVerify(exactly = 1) { apiService.getAllLaunches() }
    }


    private val companyInfoDto = CompanyInfoDto(
        name = "Company Name",
        founderName = "Founder Name",
        yearFounded = 2000,
        employees = 100,
        launchSites = 10,
        valuation = 1234567890L
    )

    private val expectedCompanyModel = CompanyInfo(
        name = "Company Name",
        founderName = "Founder Name",
        yearFounded = 2000,
        employees = 100,
        launchSites = 10,
        valuation = 1234567890L
    )

    // Not all members here strictly necessary - see SpaceXInfoViewModelTest.kt:149
    private val launchDtos = listOf(
        LaunchDto(
            missionName = "Mission 1",
            launchDateUtc = DateTime(1293840000000), // 2011-01-01 00:00:00
            rocketDto = RocketDto(
                name = "Rocket 1",
                type = "Type A",
            ),
            launchSuccess = true,
            linksDto = LinksDto("Patch Url 1", "Article 1", "Video 1", "Wikipedia 1")
        ),
        LaunchDto(
            missionName = "Mission 2",
            launchDateUtc = DateTime(1325376000000), // 2012-01-01 00:00:00
            rocketDto = RocketDto(
                name = "Rocket 2",
                type = "Type B",
            ),
            launchSuccess = false,
            linksDto = LinksDto("Patch Url 2", "Article 2", "Video 2", "Wikipedia 2")
        ),
        LaunchDto(
            missionName = "Mission 3",
            launchDateUtc = DateTime(1356998400000), // 2013-01-01 00:00:00
            rocketDto = RocketDto(
                name = "Rocket 3",
                type = "Type B",
            ),
            launchSuccess = true,
            linksDto = LinksDto("Patch Url 3", "Article 3", "Video 3", "Wikipedia 3")
        ),
        LaunchDto(
            missionName = "Mission 4",
            launchDateUtc = DateTime(1388534400000), // 2014-01-01 00:00:00
            rocketDto = RocketDto(
                name = "Rocket 4",
                type = "Type A",
            ),
            launchSuccess = false,
            linksDto = LinksDto("Patch Url 4", "Article 4", "Video 4", "Wikipedia 4")
        ),
        LaunchDto(
            missionName = "Mission 5",
            launchDateUtc = DateTime(1391212800000), // 2014-02-01 00:00:00
            rocketDto = RocketDto(
                name = "Rocket 5",
                type = "Type C",
            ),
            launchSuccess = false,
            linksDto = LinksDto(null, null, null, null)
        ),
    )

    private val expectedLaunches = listOf(
        Launch(
            missionName = "Mission 1",
            dateTime = DateTime(1293840000000), // 2011-01-01 00:00:00
            rocketName = "Rocket 1",
            rocketType = "Type A",
            successful = true,
            missionPatchUrl = "Patch Url 1",
            Links("Article 1", "Video 1", "Wikipedia 1")
        ),
        Launch(
            missionName = "Mission 2",
            dateTime = DateTime(1325376000000), // 2012-01-01 00:00:00
            rocketName = "Rocket 2",
            rocketType = "Type B",
            successful = false,
            missionPatchUrl = "Patch Url 2",
            Links("Article 2", "Video 2", "Wikipedia 2")
        ),
        Launch(
            missionName = "Mission 3",
            dateTime = DateTime(1356998400000), // 2013-01-01 00:00:00
            rocketName = "Rocket 3",
            rocketType = "Type B",
            successful = true,
            missionPatchUrl = "Patch Url 3",
            Links("Article 3", "Video 3", "Wikipedia 3")
        ),
        Launch(
            missionName = "Mission 4",
            dateTime = DateTime(1388534400000), // 2014-01-01 00:00:00
            rocketName = "Rocket 4",
            rocketType = "Type A",
            successful = false,
            missionPatchUrl = "Patch Url 4",
            Links("Article 4", "Video 4", "Wikipedia 4")
        ),
        Launch(
            missionName = "Mission 5",
            dateTime = DateTime(1391212800000), // 2014-02-01 00:00:00
            rocketName = "Rocket 5",
            rocketType = "Type C",
            successful = false,
            missionPatchUrl = null,
            Links(null, null, null)
        ),
    )
}