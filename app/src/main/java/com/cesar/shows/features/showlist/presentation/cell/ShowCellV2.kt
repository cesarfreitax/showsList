package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import com.cesar.shows.core.utils.load
import com.cesar.shows.databinding.ShowCellBinding
import com.cesar.shows.databinding.ShowCellV2Binding
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class ShowCellV2(private val viewbinding: ShowCellV2Binding) : BaseCell(viewbinding.root) {

    fun setupCell(item: ShowResponse, context: Context, navigate: () -> Unit = {}) {
        setupCellView(item, context)
        navigateToDetails(navigate)
    }

    private fun navigateToDetails(navigate: () -> Unit) {
        viewbinding.imgShow.setOnClickListener {
            navigate()
        }
    }

    private fun setupCellView(
        item: ShowResponse,
        context: Context
    ) {
        viewbinding.imgShow.load(item.image?.medium, context)
    }
}