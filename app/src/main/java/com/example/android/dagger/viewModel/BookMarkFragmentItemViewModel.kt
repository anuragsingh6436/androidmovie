package com.example.android.dagger.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.dataBase.MovieBookMarkIdTable
import com.example.android.dagger.model.Event

class BookMarkFragmentItemViewModel(
    val movieBookMarkIdTable: MovieBookMarkIdTable,
    eventStream: MutableLiveData<Event>
) :BaseRecyclerItem {

}