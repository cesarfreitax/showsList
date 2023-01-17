package com.cesar.shows.features.showlist.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.core.network.tvmazeapi.RetrofitInstance
import com.cesar.shows.core.utils.toggleVisibility
import com.cesar.shows.databinding.ActivityShowlistBinding
import com.cesar.shows.databinding.ShowCellV2Binding
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchData()
    }


    private fun fetchData() {

        RetrofitInstance.apiInterface.getShows()

            .enqueue(object : Callback<ArrayList<ShowResponse?>> {
                override fun onResponse(
                    call: Call<ArrayList<ShowResponse?>>,
                    response: Response<ArrayList<ShowResponse?>>
                ) {
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
                    if (response.body()?.size == shows.size) {
                        stopFetching()
                        setupRecyclerView()
                    }
                }

                override fun onFailure(call: Call<ArrayList<ShowResponse?>>, t: Throwable) {
                    stopFetching()
                    Toast.makeText(this@ShowListActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun stopFetching() {
        binding.lnrFetching.toggleVisibility(false)
        binding.rcvShows.toggleVisibility(true)
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
                }
            }
        }
}