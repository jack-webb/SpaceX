package me.jackwebb.spacex.ui.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.jackwebb.spacex.R
import me.jackwebb.spacex.databinding.ItemLaunchBinding
import me.jackwebb.spacex.model.Launch
import me.jackwebb.spacex.model.Links
import me.jackwebb.spacex.util.Constants

class LaunchAdapter(
    val onClick: ((Links) -> Unit)
) : RecyclerView.Adapter<LaunchAdapter.ViewHolder>() {

    var launches: List<Launch> = emptyList()
        set(value) {
            // This keeps scroll position and minimises redraws
            // We could use notifyDataSetChanged() or smoothScrollToPosition() to scroll on change
            val diffCallback = LaunchDiffUtil(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLaunchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(launches[position])

    override fun getItemCount() = launches.size

    inner class ViewHolder(private val binding: ItemLaunchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(launch: Launch) {
            val context = binding.root.context

            binding.launch = launch
            binding.root.setOnClickListener { onClick.invoke(launch.links) }
            binding.tvDate.text = context.getString(
                R.string.time_utc, launch.dateTime.toString(Constants.DATETIME_FORMAT)
            )
        }
    }

    inner class LaunchDiffUtil(
        private val old: List<Launch>,
        private val new: List<Launch>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return old[oldPos].missionName == new[newPos].missionName
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            val oldLaunch = old[oldPos]
            val newLaunch = new[newPos]
            return oldLaunch.missionName == newLaunch.missionName &&
                    oldLaunch.dateTime == newLaunch.dateTime &&
                    oldLaunch.rocketName == newLaunch.rocketName &&
                    oldLaunch.rocketType == newLaunch.rocketType &&
                    oldLaunch.daysDelta == newLaunch.daysDelta &&
                    oldLaunch.missionPatchUrl == newLaunch.missionPatchUrl
        }

    }
}