package com.cesar.shows.features.showlist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.databinding.ActivityShowlistBinding
import com.cesar.shows.databinding.ShowCellBinding
import com.cesar.shows.features.showlist.data.model.ShowResponse
import com.cesar.shows.features.showlist.presentation.cell.ShowCell
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate

class ShowListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowlistBinding
    private var adapter = GenericRecyclerAdapter()

    val item = ShowResponse.Show(
        _links = ShowResponse.Links(
            previousepisode = ShowResponse.Previousepisode(
                href = "https://api.tvmaze.com/episodes/185054"
            ),
            self = ShowResponse.Self(
                href = "dsaokdsaokd"
            )
        ),
        averageRuntime = 60,
        dvdCountry = null,
        ended = "no",
        externals = ShowResponse.Externals(
            imdb = "124",
            thetvdb = 1,
            tvrage = 10
        ),
        genres = listOf("Action", "Drama", "Romance"),
        id = 1,
        image = ShowResponse.Image(
            medium = "312321312",
            original = "434343"
        ),
        language = "English",
        name = "Not Arrow",
        network = ShowResponse.Network(
            id = 1,
            country = ShowResponse.Country(
                code = "aaa",
                name = "bbb",
                timezone = "EN-US"
            ),
            name = "teste",
            officialSite = "sadada"
        ),
        officialSite = "wwww...",
        premiered = "no",
        rating = ShowResponse.Rating(
            average = 4.0
        ),
        runtime = 1,
        schedule = ShowResponse.Schedule(
            days = listOf("1", "2"),
            time = "312321"
        ),
        status = "ok",
        summary = "blablalbalbalbalblblablablalbalbalbalblablalba",
        type = "Hero",
        updated = 1,
        url = "www.dasojdaisjd.vom",
        webChannel = null,
        weight = 10
    )

    val listItems = listOf(item, item, item, item, item, item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowlistBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        binding.rcvShows.layoutManager = LinearLayoutManager(this)
        binding.rcvShows.adapter = adapter
        adapter.delegate = recyclerViewDelegate
        adapter.snapshot?.snapshotList = listItems
    }


    private var recyclerViewDelegate =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = listItems.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = ShowCellBinding::class.java,
                    clazz = ShowCell::class.java,
                    reuseIdentifier = 0)
            }

            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as ShowCell).let {
                    val item = listItems[position]
                    it.setupCell(item = item)
                }
            }
        }
}