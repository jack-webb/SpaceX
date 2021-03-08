package me.jackwebb.spacex.ui.info.links

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.jackwebb.spacex.R
import me.jackwebb.spacex.databinding.ItemLinkBinding
import me.jackwebb.spacex.model.Links

class LinkAdapter(
    val onClick: ((String) -> Unit)
) : RecyclerView.Adapter<LinkAdapter.ViewHolder>() {

    var links: List<Pair<String, Links.LinkType>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLinkBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(links[position])

    override fun getItemCount() = links.size

    inner class ViewHolder(private val binding: ItemLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(link: Pair<String, Links.LinkType>) {
            binding.root.setOnClickListener { onClick.invoke(link.first) }

            binding.tvLinkName.text = binding.root.context.getString(
                when (link.second) {
                    Links.LinkType.ARTICLE -> R.string.article
                    Links.LinkType.VIDEO -> R.string.video
                    Links.LinkType.WIKIPEDIA -> R.string.wikipedia
                }
            )
            binding.tvLinkUrl.text = link.first

            binding.ivIcon.setImageResource(
                when (link.second) {
                    Links.LinkType.ARTICLE -> R.drawable.ic_article
                    Links.LinkType.VIDEO -> R.drawable.ic_video
                    Links.LinkType.WIKIPEDIA -> R.drawable.ic_wikipedia
                }
            )
        }
    }

}