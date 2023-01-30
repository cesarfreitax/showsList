package com.cesar.shows.features.showlist.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.R
import com.cesar.shows.core.network.tvmazeapi.RetrofitInstanceTvMaze
import com.cesar.shows.core.utils.toggleVisibility
import com.cesar.shows.databinding.ActivityShowlistBinding
import com.cesar.shows.databinding.ShowCellV2Binding
import com.cesar.shows.features.showlist.data.model.search.ShowSearchResponse
import com.cesar.shows.features.showlist.data.model.show.Image
import com.cesar.shows.features.showlist.data.model.show.Rating
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import com.cesar.shows.features.showlist.presentation.cell.ShowCellV2
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowListActivity : AppCompatActivity() {

    private val sharedPreferencesFavs: SharedPreferences by lazy {
        getSharedPreferences(
            "favorites",
            Context.MODE_PRIVATE
        )
    }
    private val binding by lazy { ActivityShowlistBinding.inflate(layoutInflater) }
    private var adapter = GenericRecyclerAdapter()
    private val shows = mutableListOf<ShowResponse>()
    private var pageIndex = 0
    private var showFavorites = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        setupSearchView()
        setupFavorites()
        setupHeaderVisibility()
        setupLogoClick()
        fetchData()
    }

    private fun setupLogoClick() {
        binding.imgLogo.setOnClickListener {
            binding.rcvShows.scrollToPosition(0)
        }
    }

    private fun setupHeaderVisibility() {
        binding.rcvShows.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val x = scrollY - oldScrollY

            if (x > 100) {
                binding.ctlHeader.toggleVisibility(false)
            }

            if (x < -100) {
                binding.ctlHeader.toggleVisibility(true)
            }
        }
    }

    private fun setupFavorites() {
        binding.imgShowFavorites.setOnClickListener {
            shows.clear()
            showFavorites = !showFavorites
            changeImageIfIsFavorite(showFavorites, binding.imgShowFavorites)
            loading(true)
            val sharedPrefOnlyFavs = sharedPreferencesFavs.all.filter { it.value == true }
            if (showFavorites && sharedPrefOnlyFavs.isNotEmpty()) {
                fetchOnlyFavorites(sharedPrefOnlyFavs)
            } else if (showFavorites) {
                setupEmptyFavoritesPlaceholder(true)
                loading(false)
            } else {
                setupEmptyFavoritesPlaceholder(false)
                fetchData(true)
            }

        }
    }

    private fun setupEmptyFavoritesPlaceholder(isEmpty: Boolean) {
        binding.lnrEmptyFavorites.toggleVisibility(isEmpty)
        binding.rcvShows.toggleVisibility(!isEmpty)
    }

    private fun fetchOnlyFavorites(sharedPrefOnlyFavs: Map<String, Any?>) {
        sharedPrefOnlyFavs.forEach { show ->
            RetrofitInstanceTvMaze.apiInterface.getShowById(show.key.toInt())
                .enqueue(object : Callback<ShowResponse?> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ShowResponse?>,
                        response: Response<ShowResponse?>
                    ) {
                        val show = ShowResponse(
                            id = response.body()?.id,
                            genres = response.body()?.genres,
                            image = Image(
                                response.body()?.image?.medium.toString(),
                                response.body()?.image?.original.toString()
                            ),
                            name = response.body()?.name,
                            summary = response.body()?.summary,
                            rating = Rating(response.body()?.rating!!.average)
                        )
                        shows.add(show)
                        if (shows.size == sharedPrefOnlyFavs.size) {
                            layoutCompleter()
                            setupRecyclerView()
                            adapter.notifyDataSetChanged()
                            loading(false)
                        }
                    }

                    override fun onFailure(call: Call<ShowResponse?>, t: Throwable) {
                        loading(false)
                        Toast.makeText(
                            this@ShowListActivity,
                            t.localizedMessage,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
        }
    }

    private fun changeImageIfIsFavorite(isFavorite: Boolean, iconFavorite: ImageView) {
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

    private fun setupSearchView() {
        binding.srcFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                layoutCompleter()
                fetchByQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    fetchData()
                }
                return false
            }

        })
    }

    private fun fetchByQuery(query: String?) {
        RetrofitInstanceTvMaze.apiInterface.getShowsBySearch(query.toString())
            .enqueue(object : Callback<ArrayList<ShowSearchResponse?>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<ShowSearchResponse?>>,
                    response: Response<ArrayList<ShowSearchResponse?>>
                ) {
                    shows.clear()
                    loading(true)
                    response.body()?.map {
                        var average = 3.0
                        if (it?.show?.rating?.average != null) {
                            average = it.show.rating.average.toString().toDouble()
                        }
                        val show = ShowResponse(
                            id = it?.show?.id,
                            genres = it?.show?.genres,
                            image = Image(
                                it?.show?.image?.medium.toString(),
                                it?.show?.image?.original.toString()
                            ),
                            name = it?.show?.name,
                            rating = Rating(average),
                            summary = it?.show?.summary
                        )
                        shows.add(show)
                    }
                    layoutCompleter()
                    setupRecyclerView()
                    adapter.notifyDataSetChanged()
                    loading(false)
                }

                override fun onFailure(
                    call: Call<ArrayList<ShowSearchResponse?>>,
                    t: Throwable
                ) {
                    loading(false)
                    Toast.makeText(
                        this@ShowListActivity,
                        t.localizedMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }

    private fun layoutCompleter() {
        val diff = shows.size % 3
        if (diff != 0) {
            for (i in 0..diff) {
                shows.add(ShowResponse())
            }
        }
    }

    private fun fetchData(clear: Boolean = true) {
        RetrofitInstanceTvMaze.apiInterface.getShows(pageIndex)
            .enqueue(object : Callback<ArrayList<ShowResponse?>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<ShowResponse?>>,
                    response: Response<ArrayList<ShowResponse?>>
                ) {
                    if (clear) { shows.clear() }
                    response.body()?.map {
                        val show = ShowResponse(
                            id = it?.id,
                            genres = it?.genres,
                            image = it?.image,
                            name = it?.name,
                            rating = it?.rating,
                            summary = it?.summary
                        )
                        shows.add(show)
                    }
                    adapter.notifyDataSetChanged()
                    loading(false)
                }

                override fun onFailure(call: Call<ArrayList<ShowResponse?>>, t: Throwable) {
                    loading(false)
                    Toast.makeText(this@ShowListActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun loading(isLoading: Boolean) {
        binding.lnrFetching.toggleVisibility(isLoading)
        binding.rcvShows.toggleVisibility(!isLoading)
    }

    private fun setupRecyclerView() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.justifyContent = JustifyContent.CENTER
        binding.rcvShows.layoutManager = layoutManager
        binding.rcvShows.adapter = adapter
        adapter.delegate = recyclerViewDelegate
        adapter.snapshot?.snapshotList = shows
    }

    private var recyclerViewDelegate =
        object : GenericRecylerAdapterDelegate {

            override fun numberOfRows(adapter: GenericRecyclerAdapter): Int = shows.size

            override fun registerCellAtPosition(
                adapter: GenericRecyclerAdapter,
                position: Int
            ): AdapterHolderType {
                return AdapterHolderType(
                    viewBinding = ShowCellV2Binding::class.java,
                    clazz = ShowCellV2::class.java,
                    reuseIdentifier = 0
                )
            }

            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as ShowCellV2).let {
                    val item = shows[position]
                    val isFavorite = sharedPreferencesFavs.getBoolean(item.id.toString(), false)
                    val emptyShow = item.id == 0
                    it.setupCell(
                        canFavorite = !emptyShow,
                        item = item,
                        context = this@ShowListActivity,
                        isFavorite = isFavorite,
                        navigate = {
                            if (item.id != null) navigateToDetails(item)
                        },
                        favoriteAction = {
                            val isFavAux = sharedPreferencesFavs.getBoolean(item.id.toString(), false)
                            sharedPreferencesFavs.edit().putBoolean(item.id.toString(), !isFavAux).apply()
                            return@setupCell sharedPreferencesFavs.getBoolean(
                                item.id.toString(),
                                false
                            )
                        })
                    if (position == (shows.size - 1) && position > 200) {
                        pageIndex++
                        binding.lnrFetching.toggleVisibility(true)
                        fetchData(clear = false)
                    }
                }
            }
        }

    private fun navigateToDetails(item: ShowResponse) {
        val intent =
            Intent(this@ShowListActivity, ShowDetailsActivity::class.java)
        intent.putExtra("show", item)
        startActivity(intent)
    }
}