package com.cesar.shows.features.showlist.presentation.cell

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.cesar.shows.databinding.GenreCellBinding

class GenreCell(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding: GenreCellBinding = GenreCellBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupCell(title: String) {
        binding.txtGenre.text = title
    }

}