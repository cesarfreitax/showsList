package com.cesar.shows.features.showlist.presentation

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.core.network.tvmazeapi.RetrofitInstance
import com.cesar.shows.databinding.ActivityShowDetailsBinding
import com.cesar.shows.databinding.SeasonCellBinding
import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import com.cesar.shows.features.showlist.data.model.video.VideoResponse
import com.cesar.shows.features.showlist.presentation.cell.GenreCell
import com.cesar.shows.features.showlist.presentation.cell.SeasonCell
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowDetailsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityShowDetailsBinding.inflate(layoutInflater) }
    private val episodes = mutableListOf<EpisodeResponse>()
    private var seasons = mutableSetOf<Int?>()
    private var adapter = GenericRecyclerAdapter()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val show = intent.getSerializableExtra("show") as ShowResponse
        binding.txtShowName.text = show.name
        setupGenresCells(show)
        binding.rtbRating.rating = (show.rating?.average?.div(2))!!.toFloat()
        binding.txtSummary.text = Html.fromHtml(show.summary, Build.VERSION.SDK_INT)
        lifecycle.addObserver(binding.ypvPlayer)

        RetrofitInstance.apiInterface.getShowEpisodes(show.id.toString())
            .enqueue(object : Callback<ArrayList<EpisodeResponse?>> {
                override fun onResponse(
                    call: Call<ArrayList<EpisodeResponse?>>,
                    response: Response<ArrayList<EpisodeResponse?>>
                ) {
                    response.body()?.map {
                        val episode = EpisodeResponse(
                            _links = it?._links,
                            airstamp = it?.airstamp,
                            airdate = it?.airdate,
                            airtime = it?.airtime,
                            id = it?.id,
                            image = it?.image,
                            name = it?.name,
                            number = it?.number,
                            rating = it?.rating,
                            runtime = it?.runtime,
                            season = it?.season,
                            summary = it?.summary,
                            type = it?.type,
                            url = it?.url
                        )
                        episodes.add(episode)
                    }
                    if (episodes.size == response.body()?.size) {
                        seasons = episodes.map { it.season }.toMutableSet()
                        setupRecyclerView()
                    }
                }

                override fun onFailure(call: Call<ArrayList<EpisodeResponse?>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        com.cesar.shows.core.network.youtubeapi.RetrofitInstance.apiInterface.getSpecificTrailer("${show.name} official trailer")
            .enqueue(object : Callback<VideoResponse?> {
                override fun onResponse(
                    call: Call<VideoResponse?>,
                    response: Response<VideoResponse?>
                ) {
                    binding.ypvPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            val videoId = response.body()?.items?.first()?.id?.videoId.toString()
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    })

                }

                override fun onFailure(call: Call<VideoResponse?>, t: Throwable) {
                }
            })


    }

    private fun setupRecyclerView() {
        binding.rcvSeasons.layoutManager = LinearLayoutManager(this)
        binding.rcvSeasons.adapter = adapter
        adapter.delegate = recyclerViewDelegate
        adapter.snapshot?.snapshotList = episodes
    }

    private var recyclerViewDelegate =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = seasons.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = SeasonCellBinding::class.java,
                    clazz = SeasonCell::class.java,
                    reuseIdentifier = 0
                )
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as SeasonCell).let { c ->
                    val season = seasons.toMutableList()[position]
                    c.setupCell("${season.toString()} Temporada", this@ShowDetailsActivity, episodes.filter { it.season == season }.toMutableList())
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
//                (binding.rcvSeasons.findViewHolderForAdapterPosition(index) as? SeasonCell).let {
//                    it?.setupRecyclerView(this@ShowDetailsActivity)
//                }
            }
        }

    private fun setupGenresCells(item: ShowResponse) {
        binding.lnrTypeshowContainer.removeAllViews()
        item.genres?.forEach {
            val genreCell = GenreCell(binding.root.context, null)
            genreCell.setupCell(it)
            binding.lnrTypeshowContainer.addView(genreCell)
        }
    }
}