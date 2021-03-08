package me.jackwebb.spacex.ui.info

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.jackwebb.spacex.R
import me.jackwebb.spacex.databinding.DialogFilterBinding
import me.jackwebb.spacex.databinding.ScreenSpacexInformationBinding
import me.jackwebb.spacex.ui.info.filter.FilterYearsAdapter
import me.jackwebb.spacex.ui.info.links.LinksBottomSheet
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class SpaceXInfoFragment : Fragment() {

    private lateinit var binding: ScreenSpacexInformationBinding

    private val viewModel: SpaceXInfoViewModel by viewModels()

    private val launchesAdapter = LaunchAdapter { links ->
        if (links.asTypedList.isNotEmpty()) {
            LinksBottomSheet(links.asTypedList).show(parentFragmentManager, BOTTOM_SHEET_TAG)
        } else {
            Toast.makeText(context, "No article or video links available", Toast.LENGTH_LONG).show()
        }
    }

    private val yearsFilterAdapter = FilterYearsAdapter()
    private lateinit var filterParams: SpaceXInfoViewModel.FilterParams

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScreenSpacexInformationBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            rvLaunches.adapter = launchesAdapter
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.companyInfo.observe(viewLifecycleOwner, { info ->
            binding.tvAbout.text = resources.getString(
                R.string.about,
                info.name,
                info.founderName,
                info.yearFounded,
                info.employees,
                info.launchSites,
                info.valuation.toUSDString()
            )
        })

        viewModel.launches.observe(viewLifecycleOwner, {
            launchesAdapter.launches = it
        })

        viewModel.filterParams.observe(viewLifecycleOwner, {
            filterParams = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> showFilterDialog()
        }
        return true
    }

    private fun showFilterDialog() {
        val dialogBinding = DialogFilterBinding.inflate(layoutInflater, null, false).apply {
            rvYears.adapter = yearsFilterAdapter.apply {
                years = filterParams.years.toList().sortedBy { it.first }
            }
            cbSuccessful.isChecked = true in filterParams.successes
            cbUnsuccessful.isChecked = false in filterParams.successes
            swSortAscending.isChecked = filterParams.sortAscending
        }

        AlertDialog.Builder(context).apply {
            setView(dialogBinding.root)
            setPositiveButton(R.string.submit) { dialog, _ ->
                viewModel.updateFilter(
                    sortAscending = dialogBinding.swSortAscending.isChecked,
                    years = yearsFilterAdapter.checkedYears,
                    successes = mutableSetOf<Boolean>().apply {
                        if (dialogBinding.cbSuccessful.isChecked) this.add(true)
                        if (dialogBinding.cbUnsuccessful.isChecked) this.add(false)
                    }
                )
                dialog.dismiss()
            }
        }.show()
    }

    private fun Long.toUSDString() =
        NumberFormat.getCurrencyInstance(Locale("en", "US")).format(this)

    companion object {
        private const val BOTTOM_SHEET_TAG = "links-bottom-sheet"
        fun newInstance() = SpaceXInfoFragment()
    }
}