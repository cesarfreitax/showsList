package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.cesar.shows.R
import com.cesar.shows.core.utils.load
import com.cesar.shows.core.utils.toggleVisibility
import com.cesar.shows.databinding.ShowCellV2Binding
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import io.github.enicolas.genericadapter.adapter.BaseCell

class ShowCellV2(private val viewbinding: ShowCellV2Binding) : BaseCell(viewbinding.root) {

    private val iconFavorite = viewbinding.imgFavorite

    fun setupCell(
        item: ShowResponse,
        context: Context,
        isFavorite: Boolean = false,
        navigate: () -> Unit = {},
        favoriteAction: () -> Boolean = {false},
        canFavorite: Boolean? = true
    ) {
        setupCellView(item, context, item.id == 0, canFavorite!!, isFavorite)
        navigateToDetails(navigate, item.id == 0)
        setupCellFavorite(favoriteAction, context)
    }

    private fun setupCellFavorite(
        favoriteAction: () -> Boolean,
        context: Context
    ) {
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

    private fun navigateToDetails(navigate: () -> Unit, isEmpty: Boolean) {
        viewbinding.imgShow.setOnClickListener {
            if (!isEmpty) navigate()
        }
    }

    private fun setupCellView(
        item: ShowResponse,
        context: Context,
        isEmpty: Boolean,
        canFavorite: Boolean,
        isFavorite: Boolean
    ) {
        if (isEmpty) {
            viewbinding.imgShow.setImageResource(R.drawable.background_empty_show)
            viewbinding.cdvShowBorder.setCardBackgroundColor(viewbinding.root.context.getColor(R.color.background))
        } else {
            viewbinding.imgShow.load(item.image?.medium, context)
        }
        viewbinding.imgFavorite.toggleVisibility(canFavorite)
        changeImageIfIsFavorite(isFavorite, iconFavorite, context)
    }
}