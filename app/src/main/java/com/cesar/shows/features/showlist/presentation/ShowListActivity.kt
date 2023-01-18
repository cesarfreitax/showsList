package com.cesar.shows.features.showlist.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.core.network.tvmazeapi.RetrofitInstanceTvMaze
import com.cesar.shows.core.utils.toggleVisibility
import com.cesar.shows.databinding.ActivityShowlistBinding
import com.cesar.shows.databinding.ShowCellV2Binding
import com.cesar.shows.features.showlist.data.model.search.ShowSearchResponse
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

    private val binding by lazy { ActivityShowlistBinding.inflate(layoutInflater) }
    private var adapter = GenericRecyclerAdapter()
    private val shows = mutableListOf<ShowResponse>()
    private val showsFiltered = mutableListOf<ShowSearchResponse>()
    private var pageIndex = 0
    private var isLastIndex = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        setupSearchView()
        fetchData()
    }

    private fun setupSearchView() {
        binding.srcFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                RetrofitInstanceTvMaze.apiInterface.getShowsBySearch(query.toString())
                    .enqueue(object : Callback<ArrayList<ShowSearchResponse?>>{
                        override fun onResponse(
                            call: Call<ArrayList<ShowSearchResponse?>>,
                            response: Response<ArrayList<ShowSearchResponse?>>
                        ) {
                            shows.clear()
                            startLoading()
                            response.body()?.map {
                                val show = ShowResponse(
                                    id = it?.show?.id,
                                    genres = it?.show?.genres,
                                    image = com.cesar.shows.features.showlist.data.model.show.Image(it?.show?.image?.medium.toString(), it?.show?.image?.original.toString()),
                                    name = it?.show?.name,
                                    summary = it?.show?.summary
                                )
                                shows.add(show)
                            }
                            setupRecyclerView()
                            adapter.notifyDataSetChanged()
                            stopLoading()
                        }

                        override fun onFailure(call: Call<ArrayList<ShowSearchResponse?>>, t: Throwable) {
                            stopLoading()
                            Toast.makeText(this@ShowListActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                // on below line we are checking
                // if query exist or not.
//                if (programmingLanguagesList.contains(query)) {
//                    // if query exist within list we
//                    // are filtering our list adapter.
//                    listAdapter.filter.filter(query)
//                } else {
//                    // if query is not present we are displaying
//                    // a toast message as no  data found..
//                    Toast.makeText(this@MainActivity, "No Language found..", Toast.LENGTH_LONG)
//                        .show()
//                }
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


    private fun fetchData() {
        RetrofitInstanceTvMaze.apiInterface.getShows(pageIndex)
            .enqueue(object : Callback<ArrayList<ShowResponse?>> {
                override fun onResponse(
                    call: Call<ArrayList<ShowResponse?>>,
                    response: Response<ArrayList<ShowResponse?>>
                ) {
                    shows.clear()
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
                    stopLoading()
                }

                override fun onFailure(call: Call<ArrayList<ShowResponse?>>, t: Throwable) {
                    stopLoading()
                    Toast.makeText(this@ShowListActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun stopLoading() {
        binding.lnrFetching.toggleVisibility(false)
        binding.rcvShows.toggleVisibility(true)
    }

    private fun startLoading() {
        binding.lnrFetching.toggleVisibility(true)
        binding.rcvShows.toggleVisibility(false)
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

            @RequiresApi(Build.VERSION_CODES.N)
            override fun cellForPosition(
                adapter: GenericRecyclerAdapter,
                cell: RecyclerView.ViewHolder,
                position: Int
            ) {
                (cell as ShowCellV2).let {
                    val item = shows[position]
                    it.setupCell(item = item, context = this@ShowListActivity) {
                        val intent = Intent(this@ShowListActivity, ShowDetailsActivity::class.java)
                        intent.putExtra("show", item)
                        startActivity(intent)
                    }
                    if (position == (shows.size - 1) && position > 200) {
                        pageIndex++
                        binding.lnrFetching.toggleVisibility(true)
                        fetchData()
                    }
                }
            }
        }
}