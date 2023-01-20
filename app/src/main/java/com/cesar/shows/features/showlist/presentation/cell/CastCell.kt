package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.cesar.shows.R
import com.cesar.shows.core.utils.load
import com.cesar.shows.databinding.CastCellBinding
import com.cesar.shows.databinding.ShowCellV2Binding
import com.cesar.shows.features.showlist.data.model.cast.ShowCastResponse
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class CastCell(private val viewbinding: CastCellBinding) : BaseCell(viewbinding.root) {

    fun setupCell(cast: ShowCastResponse, context: Context, navigate: () -> Unit = {}) {
        setupCellView(cast, context)
        navigateToDetails(navigate)
    }

    private fun navigateToDetails(navigate: () -> Unit) {
        viewbinding.imgPerson.setOnClickListener {
            navigate()
        }
    }

    private fun setupCellView(
        cast: ShowCastResponse,
        context: Context
    ) {
        viewbinding.imgPerson.load(cast.person?.image?.medium, context)
        viewbinding.txtCharacterName.text = cast.character?.name
        viewbinding.txtPersonName.text = cast.person?.name
    }
}