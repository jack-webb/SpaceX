package me.jackwebb.spacex

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import me.jackwebb.spacex.model.CompanyInfo
import me.jackwebb.spacex.model.Launch
import me.jackwebb.spacex.model.Links
import me.jackwebb.spacex.repo.SpaceXRepository
import me.jackwebb.spacex.ui.info.SpaceXInfoViewModel
import org.joda.time.DateTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SpaceXInfoViewModelTest : BaseUnitTest() {

    @MockK(relaxed = true)
    private lateinit var spaceXRepository: SpaceXRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        coEvery { spaceXRepository.getCompanyInfo() } returns companyInfo
        coEvery { spaceXRepository.getAllLaunches() } returns allLaunches
    }

    private fun getViewModel() = SpaceXInfoViewModel(spaceXRepository)

    @Test
    fun `Test company info`() {
        val vm = getViewModel()
        assertEquals(companyInfo, vm.companyInfo.value!!)
    }

    @Test
    fun `Test initial list state`() {
        val vm = getViewModel()

        coVerify(exactly = 1) { spaceXRepository.getCompanyInfo() }
        coVerify(exactly = 1) { spaceXRepository.getAllLaunches() }

        assertEquals(allLaunches, vm.launches.value!!)
    }

    @Test
    fun `Test default filter params`() {
        val vm = getViewModel()

        with(vm.filterParams.value!!) {
            assertEquals(true, this.sortAscending)
            assertEquals(setOf(true, false), this.successes)
            assertEquals(mapOf(2011 to true, 2012 to true, 2013 to true, 2014 to true), this.years)
        }
    }

    @Test
    fun `Test ascending order`() {
        val vm = getViewModel()

        vm.updateFilter(sortAscending = true)

        assertEquals(true, vm.filterParams.value!!.sortAscending)
        assertEquals(allLaunches, vm.launches.value!!)
    }

    @Test
    fun `Test descending order`() {
        val vm = getViewModel()

        vm.updateFilter(sortAscending = false)

        assertEquals(false, vm.filterParams.value!!.sortAscending)
        assertEquals(allLaunches.reversed(), vm.launches.value!!)
    }

    @Test
    fun `Test only failures filter`() {
        val vm = getViewModel()
        val successesFilter = setOf(false)

        vm.updateFilter(successes = successesFilter)

        assertEquals(successesFilter, vm.filterParams.value!!.successes)
        assertEquals(allLaunches.filterNot { it.successful }, vm.launches.value!!)
    }

    @Test
    fun `Test only successes filter`() {
        val vm = getViewModel()
        val successesFilter = setOf(true)

        vm.updateFilter(successes = successesFilter)

        assertEquals(successesFilter, vm.filterParams.value!!.successes)
        assertEquals(allLaunches.filter { it.successful }, vm.launches.value!!)
    }

    @Test
    fun `Test no fails or successes filter`() {
        val vm = getViewModel()
        val successesFilter = setOf<Boolean>()

        vm.updateFilter(successes = successesFilter)

        assertEquals(successesFilter, vm.filterParams.value!!.successes)
        assertEquals(emptyList<Launch>(), vm.launches.value!!.map { it.missionName })
    }

    @Test
    fun `Test single year filter`() {
        val vm = getViewModel()
        val yearsMap = mapOf(2011 to false, 2012 to false, 2013 to false, 2014 to true)

        vm.updateFilter(years = yearsMap)

        assertEquals(yearsMap, vm.filterParams.value!!.years)
        assertEquals(allLaunches.filter { it.dateTime.year == 2014 }, vm.launches.value!!)
    }

    @Test
    fun `Test multiple years filter`() {
        val vm = getViewModel()
        val yearsMap = mapOf(2011 to false, 2012 to false, 2013 to true, 2014 to true)

        vm.updateFilter(years = yearsMap)

        assertEquals(yearsMap, vm.filterParams.value!!.years)
        assertEquals(allLaunches.filter { it.dateTime.year in (2013..2014) }, vm.launches.value!!)
    }

    @Test
    fun `Test no years filter`() {
        val vm = getViewModel()
        val yearsMap = mapOf(2011 to false, 2012 to false, 2013 to false, 2014 to false)

        vm.updateFilter(years = yearsMap)

        assertEquals(yearsMap, vm.filterParams.value!!.years)
        assertEquals(emptyList<Launch>(), vm.launches.value!!)
    }


    // Completely mocking this isn't really necessary, because we're only
    // comparing the info object as a whole, and not its members.
    private val companyInfo = CompanyInfo(
        name = "Company Name",
        founderName = "Founder Name",
        yearFounded = 2000,
        employees = 100,
        launchSites = 10,
        valuation = 1234567890L
    )

    private val allLaunches = listOf(
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
            links = Links(null, null, null)
        ),
    )
}