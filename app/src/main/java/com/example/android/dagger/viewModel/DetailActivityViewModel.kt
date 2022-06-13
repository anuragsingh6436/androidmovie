package com.example.android.dagger.viewModel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.dagger.Network.ApiHelper
import com.example.android.dagger.Network.MainRepository
import com.example.android.dagger.Network.RetrofitBuilder
import com.example.android.dagger.adapter.DetailAdapter
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.dataBase.MovieBookMarkIdTable
import com.example.android.dagger.event.ActivityEvent
import com.example.android.dagger.model.Event
import com.example.android.dagger.model.response.MovieDetailResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailActivityViewModel() : ViewModel() {

    val eventStream = MutableLiveData<Event>()
    val repository = MainRepository(ApiHelper(RetrofitBuilder.apiService))
    var movieDetailResponse: MovieDetailResponse? = null
    val isMovieSelected = ObservableBoolean(false)
    val selectionText = ObservableField("")
    val adapter = DetailAdapter()
    val itemList = ObservableArrayList<BaseRecyclerItem>()

    fun initData(id: String?) {
        id?.let {
            getMovieDetailFromApi(it)
        }
    }

    private fun getMovieDetailFromApi(movieId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getMovieDetail(getUrl(movieId)).catch {
                eventStream.postValue(Event(ActivityEvent.CLOSE_DETAIL_ACTIVITY))
            }.collect {
                movieDetailResponse = it
                val detailItem  = DetailActivityItemViewModel(it, eventStream)
                itemList.add(detailItem)
            }
        }
    }

    private fun getUrl(movieId: String): String {
        return "${movieId}?api_key=${api_key}"
    }

    fun onShareIconClick() {
        eventStream.postValue(Event(ActivityEvent.OPEN_SHARE_LINK_FLOW))
    }

    fun onBookMarkIconClick() {
        if (isMovieSelected.get()) {
            selectionText.set("Item is being removed from Bookmark")
        } else {
            selectionText.set("Item is added in BookMark")
        }
        isMovieSelected.set(!isMovieSelected.get())
        eventStream.postValue(Event(ActivityEvent.BOOKMARK_ICON_CLICKED,Pair(createBookMarkData(),isMovieSelected.get())))
    }

    private fun createBookMarkData(): MovieBookMarkIdTable {
        return MovieBookMarkIdTable(
            id = movieDetailResponse!!.id,
            title = movieDetailResponse!!.title,
            imageUrl = movieDetailResponse!!.poster_path,
            overview = movieDetailResponse!!.overview,
            releaseDate = movieDetailResponse!!.release_date
        )
    }

    fun closeBottomSheetDialog() {
        eventStream.postValue(Event(ActivityEvent.CLOSE_BOTTOM_SHEET_DIALOG))
    }

    fun openBookMarkFragment() {
        eventStream.postValue(Event(ActivityEvent.OPEN_BOOKMARK_FRAGMENT))
    }

}