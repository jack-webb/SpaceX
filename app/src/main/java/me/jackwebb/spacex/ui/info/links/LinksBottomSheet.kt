package me.jackwebb.spacex.ui.info.links

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.jackwebb.spacex.databinding.BottomSheetLinksBinding
import me.jackwebb.spacex.model.Links

class LinksBottomSheet(
    private val links: List<Pair<String, Links.LinkType>>
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetLinksBinding

    private val linksAdapter = LinkAdapter { url ->
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetLinksBinding.inflate(inflater, container, false).apply {
            rvLinks.adapter = linksAdapter
        }

        linksAdapter.links = links

        return binding.root
    }
}