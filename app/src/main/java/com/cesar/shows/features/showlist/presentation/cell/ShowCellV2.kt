package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.cesar.shows.R
import com.cesar.shows.core.utils.load
import com.cesar.shows.databinding.ShowCellV2Binding
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class ShowCellV2(private val viewbinding: ShowCellV2Binding) : BaseCell(viewbinding.root) {

    fun setupCell(item: ShowResponse, context: Context, isFavorite: Boolean = false, navigate: () -> Unit = {}, favoriteAction: () -> Boolean) {
        setupCellView(item, context)
        navigateToDetails(navigate)

        val iconFavorite = viewbinding.imgFavorite
        changeImageIfIsFavorite(isFavorite, iconFavorite, context)

        iconFavorite.setOnClickListener {
            val boolean = favoriteAction()
            changeImageIfIsFavorite(boolean, iconFavorite, context)
        }
    }

    private fun changeImageIfIsFavorite(isFavorite: Boolean, iconFavorite: ImageView, context: Context) {
        if (isFavorite) {
            iconFavorite.apply {
                setImageResource(R.drawable.ic_star_full)
                setColorFilter(ContextCompat.getColor(context, R.color.yellow))
            }
        } else {
            iconFavorite.apply {
                setImageResource(R.drawable.ic_star_empty)
                setColorFilter(ContextCompat.getColor(context, R.color.white))
            }
        }
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