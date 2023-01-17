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

class EpisodeCell(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding: EpisodeCellBinding = EpisodeCellBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupCell(episode: EpisodeResponse, context: Context) {
        binding.txtEpisodeNumber.text = episode.number.toString()
        binding.imgEpisode.load(episode.image?.medium, context)
        binding.txtEpisodeTitle.text = episode.name
    }
}