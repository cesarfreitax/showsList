package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import com.cesar.shows.core.utils.load
import com.cesar.shows.databinding.ShowCellBinding
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class ShowCell(private val viewbinding: ShowCellBinding) : BaseCell(viewbinding.root) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun setupCell(item: ShowResponse, context: Context, navigate: () -> Unit = {}) {
        setupCellView(item, context)
        navigateToDetails(navigate)
    }

    private fun navigateToDetails(navigate: () -> Unit) {
        viewbinding.cdvDetails.setOnClickListener {
            navigate()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupCellView(
        item: ShowResponse,
        context: Context
    ) {
        viewbinding.txtShowTitle.text = item.name
        viewbinding.txtSummary.text = Html.fromHtml(item.summary, Build.VERSION.SDK_INT)
        viewbinding.txtAverage.text = "Rating: ${item.rating?.average}"
        viewbinding.imgShow.load(item.image?.medium, context)
        setupGenresCells(item)
    }

    private fun setupGenresCells(item: ShowResponse) {
        viewbinding.lnrTypeshowContainer.removeAllViews()
        item.genres?.forEach {
            val genreCell = GenreCell(viewbinding.root.context, null)
            genreCell.setupCell(it)
            viewbinding.lnrTypeshowContainer.addView(genreCell)
        }
    }

}