package com.example.android.dagger.viewModel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.dagger.adapter.BookMarkFragmentAdapter
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.dataBase.DataBaseRepository
import com.example.android.dagger.event.ActivityEvent
import com.example.android.dagger.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookMarkFragmentViewModel : ViewModel() {

    val eventStream = MutableLiveData<Event>()

    val adapter = BookMarkFragmentAdapter()
    val itemsList = ObservableArrayList<BaseRecyclerItem>()
    val errorText = ObservableField("")

    init {
        getItemsFromDb()
    }

    private fun getItemsFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            val data  = DataBaseRepository.getAllBookMarkItems()
            data?.forEach {
                itemsList.add(BookMarkFragmentItemViewModel(it,eventStream))
            } ?: kotlin.run{
                errorText.set("NO BOOKMARK MOVIES")
            }
        }
    }

    fun clickOnEmptyArea() {
        eventStream.postValue(Event(ActivityEvent.CLOSE_BOOK_MARK_FRAGMENT))
    }

}