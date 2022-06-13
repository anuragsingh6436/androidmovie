package com.example.android.dagger.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.model.Event
import com.example.android.dagger.model.response.MovieDetailResponse

class DetailActivityItemViewModel(val data:MovieDetailResponse,val eventStream:MutableLiveData<Event>) :BaseRecyclerItem{

    fun getAverageVote():String {
        return data.vote_average.toString()
    }

}