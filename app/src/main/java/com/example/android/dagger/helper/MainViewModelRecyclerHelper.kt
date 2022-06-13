package com.example.android.dagger.helper

import androidx.lifecycle.MutableLiveData
import com.example.android.dagger.viewModel.MovieItemViewModel
import com.example.android.dagger.model.Event
import com.example.android.dagger.model.MovieList
import javax.inject.Inject

class MainViewModelRecyclerHelper @Inject constructor(){

    fun getRecyclerItems(response: MovieList, eventStream: MutableLiveData<Event>):List<MovieItemViewModel> {
        val data = ArrayList<MovieItemViewModel>()
        response.results.forEach {
            data.add(MovieItemViewModel(it,eventStream))
        }
        return data
    }
}