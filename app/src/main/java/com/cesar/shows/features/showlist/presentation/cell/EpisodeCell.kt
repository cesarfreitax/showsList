package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.core.utils.load
import com.cesar.shows.databinding.EpisodeCellBinding
import com.cesar.shows.databinding.GenreCellBinding
import com.cesar.shows.databinding.SeasonCellBinding
import com.cesar.shows.databinding.ShowCellBinding
import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.BaseCell
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class EpisodeCell(private val viewbinding: EpisodeCellBinding) : BaseCell(viewbinding.root) {

    fun setupCell(episode: EpisodeResponse, context: Context) {
        viewbinding.txtEpisodeNumber.text = episode.number.toString()
        viewbinding.imgEpisode.load(episode.image?.medium, context)
        viewbinding.txtEpisodeTitle.text = episode.name
    }
}