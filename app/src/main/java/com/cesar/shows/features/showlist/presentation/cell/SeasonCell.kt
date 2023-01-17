package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.R
import com.cesar.shows.core.utils.toggleVisibility
import com.cesar.shows.databinding.EpisodeCellBinding
import com.cesar.shows.databinding.SeasonCellBinding
import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.BaseCell
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class SeasonCell(private val viewbinding: SeasonCellBinding) : BaseCell(viewbinding.root) {

    private var adapter = GenericRecyclerAdapter()
    private var isChecked = false

    init {
        viewbinding.imgDetails.setOnClickListener {
            isChecked = !isChecked
            viewbinding.lnrEpisodesContainer.toggleVisibility(isChecked)
            if (isChecked) viewbinding.imgDetails.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24) else viewbinding.imgDetails.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }

    }

    fun setupCell(
        title: String,
        context: Context,
        episodes: MutableList<EpisodeResponse>
    ) {
        viewbinding.txtSeasonTitle.text = title
        setupEpisodesCell(episodes, context)
    }

    private fun setupEpisodesCell(episodes: MutableList<EpisodeResponse>, context: Context) {
        viewbinding.lnrEpisodesContainer.removeAllViews()
        episodes.forEach { episode ->
            val episodeCell = EpisodeCell(context, null)
            episodeCell.setupCell(episode, context)
            viewbinding.lnrEpisodesContainer.addView(episodeCell)
        }
    }

}