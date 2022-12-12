package com.cesar.shows.features.showlist.presentation.cell

import com.cesar.shows.databinding.ShowCellBinding
import com.cesar.shows.features.showlist.data.model.ShowResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class ShowCell(private val viewbinding: ShowCellBinding) : BaseCell(viewbinding.root) {

    fun setupCell(item: ShowResponse.Show) {
        viewbinding.txtShowTitle.text = item.name
        viewbinding.txtSummary.text = item.summary
        viewbinding.txtGenre.text = item.genres.first()
    }

}