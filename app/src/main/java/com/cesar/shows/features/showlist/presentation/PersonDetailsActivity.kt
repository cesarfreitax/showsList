package com.cesar.shows.features.showlist.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.shows.R
import com.cesar.shows.core.network.tvmazeapi.RetrofitInstanceTvMaze
import com.cesar.shows.core.utils.load
import com.cesar.shows.databinding.ActivityPersonDetailsBinding
import com.cesar.shows.databinding.ShowCellV2Binding
import com.cesar.shows.features.showlist.data.model.cast.Person
import com.cesar.shows.features.showlist.data.model.participations.ParticipationsResponse
import com.cesar.shows.features.showlist.data.model.show.Rating
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import com.cesar.shows.features.showlist.presentation.cell.ShowCellV2
import io.github.enicolas.genericadapter.AdapterHolderType
import io.github.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import io.github.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonDetailsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPersonDetailsBinding.inflate(layoutInflater) }
    private var adapter = GenericRecyclerAdapter()
    private val shows = mutableListOf<ShowResponse>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val person = intent.getSerializableExtra("person") as Person
        binding.imgPerson.load(person.image?.medium, this)
        binding.txtPersonName.text = person.name ?: ""
        binding.txtPersonCountry.text = person.country?.name ?: ""
        val formattedBirthday = person.birthday?.split("-")
        val monthString: String
        if (formattedBirthday?.get(1) == "01") {
            monthString = "Jan"
        } else if (formattedBirthday?.get(2) == "02") {
            monthString = "Fev"
        } else if (formattedBirthday?.get(2) == "03") {
            monthString = "Mar"
        } else if (formattedBirthday?.get(2) == "04") {
            monthString = "Apr"
        } else if (formattedBirthday?.get(2) == "05") {
            monthString = "May"
        } else if (formattedBirthday?.get(2) == "06") {
            monthString = "Jun"
        } else if (formattedBirthday?.get(2) == "07") {
            monthString = "Jul"
        } else if (formattedBirthday?.get(2) == "08") {
            monthString = "Ago"
        } else if (formattedBirthday?.get(2) == "09") {
            monthString = "Sep"
        } else if (formattedBirthday?.get(2) == "10") {
            monthString = "Oct"
        } else if (formattedBirthday?.get(2) == "11") {
            monthString = "Nov"
        } else {
            monthString = "Dec"
        }
        binding.txtPersonBirthday.text =
            "${formattedBirthday?.get(2)}, $monthString, ${formattedBirthday?.get(1)}"

        if (person.deathday == null) {
            binding.cdvIsAliveBorder.setCardBackgroundColor(resources.getColor(R.color.green))
            binding.txtIsalive.setTextColor(resources.getColor(R.color.green))
            binding.txtIsalive.text = getString(R.string.person_alive)
        } else {
            binding.cdvIsAliveBorder.setCardBackgroundColor(resources.getColor(R.color.neutral5))
            binding.txtIsalive.setTextColor(resources.getColor(R.color.neutral5))
            binding.txtIsalive.text = getString(R.string.person_dead)
        }

        RetrofitInstanceTvMaze.apiInterface.getShowsByPersonId(person.id!!.toInt())
            .enqueue(object : Callback<ArrayList<ParticipationsResponse?>> {
                override fun onResponse(
                    call: Call<ArrayList<ParticipationsResponse?>>,
                    response: Response<ArrayList<ParticipationsResponse?>>
                ) {
                    shows.clear()
                    response.body()?.map { s ->
                        val show = ShowResponse(
                            id = s?._embedded?.show?.id,
                            genres = s?._embedded?.show?.genres,
                            image = com.cesar.shows.features.showlist.data.model.show.Image(
                                s?._embedded?.show?.image?.medium.toString(),
                                s?._embedded?.show?.image?.original.toString()
                            ),
                            name = s?._embedded?.show?.name,
                            rating = Rating(s?._embedded?.show?.rating?.average ?: 0.0),
                            summary = s?._embedded?.show?.summary
                        )
                        shows.add(show)
                        if (shows.size == response.body()?.size) {
                            setupRecyclerView()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ParticipationsResponse?>>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
                (cell as ShowCellV2).let { c ->
                    val show = shows[position]
                    c.setupCell(
                        show,
                        this@PersonDetailsActivity,
                        canFavorite = false,
                        navigate = { navigateToDetails(show) })
                }
            }

            override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {

            }
        }

    private fun navigateToDetails(item: ShowResponse) {
        val intent =
            Intent(this@PersonDetailsActivity, ShowDetailsActivity::class.java)
        intent.putExtra("show", item)
        startActivity(intent)
    }
}