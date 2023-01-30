package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import com.cesar.shows.core.utils.load
import com.cesar.shows.databinding.EpisodeCellBinding
import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class EpisodeCell(private val viewbinding: EpisodeCellBinding) : BaseCell(viewbinding.root) {

    fun setupCell(episode: EpisodeResponse, context: Context) {
        viewbinding.txtEpisodeNumber.text = episode.number.toString()
        viewbinding.imgEpisode.load(episode.image?.medium, context)
        viewbinding.txtEpisodeTitle.text = episode.name
    }
}