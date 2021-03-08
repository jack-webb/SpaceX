package me.jackwebb.spacex.ui.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.jackwebb.spacex.model.CompanyInfo
import me.jackwebb.spacex.model.Launch
import me.jackwebb.spacex.repo.SpaceXRepository
import javax.inject.Inject

@HiltViewModel
class SpaceXInfoViewModel @Inject constructor(
    private val spaceXRepository: SpaceXRepository
) : ViewModel() {

    private var _filterParams: FilterParams = FilterParams(true, emptyMap(), emptySet())
        set(value) {
            field = value
            filterParams.value = value
            refreshLaunches()
        }
    val filterParams = MutableLiveData<FilterParams>()

    val companyInfo = MutableLiveData<CompanyInfo>()

    private var allLaunches: List<Launch> = emptyList()
    private var _launches: List<Launch> = emptyList()
        set(value) {
            field = value
            launches.value = value
        }
    val launches = MutableLiveData<List<Launch>>()

    init {
        viewModelScope.launch {
            companyInfo.value = spaceXRepository.getCompanyInfo()
        }

        viewModelScope.launch {
            allLaunches = spaceXRepository.getAllLaunches()

            // Initialise the filter - custom setter also populates LiveData
            _filterParams = FilterParams(
                sortAscending = true,
                years = allLaunches.map { it.dateTime.year }.associateWith { true },
                successes = setOf(true, false)
            )
        }
    }

    private fun refreshLaunches() {
        val filteredLaunches = allLaunches
            .filter { it.successful in _filterParams.successes }
            .filter { it.dateTime.year in _filterParams.years.filter { it.value }.keys } // Show years with true values in the map

        _launches = if (_filterParams.sortAscending) {
            filteredLaunches.sortedBy { it.dateTime }
        } else {
            filteredLaunches.sortedByDescending { it.dateTime }
        }
    }

    /**
     * Update the filter parameters. Passing null doesn't update that parameter
     */
    fun updateFilter(
        sortAscending: Boolean? = null,
        years: Map<Int, Boolean>? = null,
        successes: Set<Boolean>? = null
    ) {
        _filterParams = FilterParams(
            sortAscending ?: _filterParams.sortAscending,
            years ?: _filterParams.years,
            successes ?: _filterParams.successes
        )
    }

    data class FilterParams(
        val sortAscending: Boolean,
        val years: Map<Int, Boolean>,
        val successes: Set<Boolean>
    )
}