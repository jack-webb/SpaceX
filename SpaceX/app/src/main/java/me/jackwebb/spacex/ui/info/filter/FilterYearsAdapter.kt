package me.jackwebb.spacex.ui.info.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.jackwebb.spacex.databinding.ItemCheckboxBinding

class FilterYearsAdapter : RecyclerView.Adapter<FilterYearsAdapter.ViewHolder>() {

    var years: List<Pair<Int, Boolean>> = emptyList()
        set(value) {
            field = value
            checkedYears = value.toMap().toMutableMap()
            notifyDataSetChanged()
        }

    var checkedYears: MutableMap<Int, Boolean> = mutableMapOf()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCheckboxBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(years[position])

    override fun getItemCount() = years.size


    inner class ViewHolder(private val binding: ItemCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(year: Pair<Int, Boolean>) {
            with(binding.checkBox) {
                text = year.first.toString()
                isChecked = year.second

                setOnCheckedChangeListener { _, isChecked -> checkedYears[year.first] = isChecked }
            }
        }
    }

}