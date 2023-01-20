package com.cesar.shows.features.showlist.presentation

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.core.network.tvmazeapi.RetrofitInstanceTvMaze
import com.cesar.shows.core.utils.toggleVisibility
import com.cesar.shows.databinding.ActivityShowDetailsBinding
import com.cesar.shows.databinding.CastCellBinding
import com.cesar.shows.databinding.EpisodeCellBinding
import com.cesar.shows.features.showlist.data.model.cast.*
import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import com.cesar.shows.features.showlist.data.model.video.VideoResponse
import com.cesar.shows.features.showlist.presentation.cell.CastCell
import com.cesar.shows.features.showlist.presentation.cell.EpisodeCell
import com.cesar.shows.features.showlist.presentation.cell.GenreCell
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
    private var seasons = mutableListOf<String?>()
    private var showCastList = mutableListOf<ShowCastResponse>()
    private var adapter = GenericRecyclerAdapter()
    private var adapterCast = GenericRecyclerAdapter()
    private var filteredEpisodes = mutableListOf<EpisodeResponse>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val show = intent.getSerializableExtra("show") as ShowResponse
        binding.txtShowName.text = show.name
        setupGenresCells(show)
        if (show.rating?.average != null) {
            binding.rtbRating.rating = (show.rating.average.div(2)).toFloat()
        } else {
            binding.rtbRating.toggleVisibility(false)
        }
        binding.txtSummary.text = Html.fromHtml(show.summary, Build.VERSION.SDK_INT)
        lifecycle.addObserver(binding.ypvPlayer)

        RetrofitInstanceTvMaze.apiInterface.getCastByShowId(show.id.toString().toInt())
            .enqueue(object : Callback<ArrayList<ShowCastResponse?>> {
                override fun onResponse(
                    call: Call<ArrayList<ShowCastResponse?>>,
                    response: Response<ArrayList<ShowCastResponse?>>
                ) {
                    response.body()?.map { cast ->
                        val person = ShowCastResponse(
                            person = Person(
                                id = cast?.person?.id,
                                url = cast?.person?.url,
                                name = cast?.person?.name,
                                country = cast?.person?.country,
                                birthday = cast?.person?.birthday,
                                deathday = cast?.person?.deathday,
                                gender = cast?.person?.gender,
                                image = cast?.person?.image,
                                updated = cast?.person?.updated,
                                _links = cast?.person?._links
                            ),
                            character = Character(
                                id = cast?.character?.id,
                                url = cast?.character?.url,
                                image = cast?.character?.image,
                                name = cast?.character?.name,
                                _links = cast?.character?._links
                            ),
                            self = cast?.self,
                            voice = cast?.voice
                        )
                        showCastList.add(person)
                        if (response.body()?.size == showCastList.size) {
                            setupRecyclerViewCast()
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<ShowCastResponse?>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        RetrofitInstanceTvMaze.apiInterface.getShowEpisodes(show.id.toString())
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
                        stopFetching()
                        seasons.add("Selecione a temporada")
                        val differentSeasons = episodes.map {
                            "${it.season} Temporada"
                        }.toMutableSet()
                        seasons += differentSeasons.toMutableList()
                        seasonsSpinnerAdapter()
                    }
                }

                override fun onFailure(call: Call<ArrayList<EpisodeResponse?>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        com.cesar.shows.core.network.youtubeapi.RetrofitInstanceYoutube.apiInterface.getSpecificTrailer(
            "${show.name} official trailer"
        )
            .enqueue(object : Callback<VideoResponse?> {
                override fun onResponse(
                    call: Call<VideoResponse?>,
                    response: Response<VideoResponse?>
                ) {
                    binding.ypvPlayer.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
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

    private fun seasonsSpinnerAdapter() {
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, seasons.toTypedArray())
        binding.spnSeasons.adapter = arrayAdapter

        binding.spnSeasons.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val txt = parent?.getChildAt(0) as TextView
                val currentSeason = txt.text.toString().substring(0, 1)
                if (currentSeason.isDigitsOnly()) {
                    filteredEpisodes =
                        episodes.filter { it.season == currentSeason.toInt() }.toMutableList()
                    setupRecyclerView()
                } else {
                    filteredEpisodes.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun setupRecyclerView() {
        binding.rcvSeasons.layoutManager = LinearLayoutManager(this)
        binding.rcvSeasons.adapter = adapter
        adapter.delegate = recyclerViewDelegate
        adapter.snapshot?.snapshotList = filteredEpisodes
    }

    private fun setupRecyclerViewCast() {
        binding.rcvCast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvCast.adapter = adapterCast
        adapterCast.delegate = recyclerViewDelegateCast
        adapterCast.snapshot?.snapshotList = showCastList
    }

    private var recyclerViewDelegate =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = filteredEpisodes.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = EpisodeCellBinding::class.java,
                    clazz = EpisodeCell::class.java,
                    reuseIdentifier = 0
                )
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as EpisodeCell).let { c ->
                    val episode = filteredEpisodes[position]
                    c.setupCell(episode, this@ShowDetailsActivity)
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {
//                (binding.rcvSeasons.findViewHolderForAdapterPosition(index) as? SeasonCell).let {
//                    it?.setupRecyclerView(this@ShowDetailsActivity)
//                }
            }
        }

    private var recyclerViewDelegateCast =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = showCastList.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = CastCellBinding::class.java,
                    clazz = CastCell::class.java,
                    reuseIdentifier = 0
                )
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as CastCell).let { c ->
                    val person = showCastList[position]
                    c.setupCell(person, this@ShowDetailsActivity) {
                        navigateToDetails(person)
                    }
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {

            }
        }

    private fun navigateToDetails(showCast: ShowCastResponse) {
        val intent = Intent(this@ShowDetailsActivity, PersonDetailsActivity::class.java)
        intent.putExtra("person", showCast?.person)
        startActivity(intent)
    }

    private fun setupGenresCells(item: ShowResponse) {
        binding.lnrTypeshowContainer.removeAllViews()
        item.genres?.forEach {
            val genreCell = GenreCell(binding.root.context, null)
            genreCell.setupCell(it)
            binding.lnrTypeshowContainer.addView(genreCell)
        }
    }

    private fun stopFetching() {
        binding.lnrFetching.toggleVisibility(false)
        binding.ctlData.toggleVisibility(true)
    }
}