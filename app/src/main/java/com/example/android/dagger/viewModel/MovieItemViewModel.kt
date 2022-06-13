package com.example.android.dagger.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.event.ActivityEvent
import com.example.android.dagger.model.Event
import com.example.android.dagger.model.MovieListV2
import java.util.*

class MovieItemViewModel(val data:MovieListV2, val eventStream:MutableLiveData<Event>):BaseRecyclerItem {


    fun getTitle():String {
        return data.title
    }

    fun getPosterPath():String {
        return data.poster_path
    }

    fun openDetailPage() {
        eventStream.postValue(Event(ActivityEvent.OPEN_DETAIL_PAGE,data = data.id))
    }
}