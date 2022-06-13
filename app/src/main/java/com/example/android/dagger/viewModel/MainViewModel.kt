package com.example.android.dagger.viewModel

import android.content.Context
import android.text.Editable
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.dagger.Network.MainRepository
import com.example.android.dagger.adapter.MainAadpter
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.dataBase.DataBaseRepository
import com.example.android.dagger.dataBase.MovieNameTable
import com.example.android.dagger.event.ActivityEvent
import com.example.android.dagger.event.ActivityEvent.OPEN_SEARCH_FRAGMENT
import com.example.android.dagger.helper.MainViewModelRecyclerHelper
import com.example.android.dagger.model.Event
import com.example.android.dagger.model.MovieList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


const val api_key = "80487b92e163261cd7ad5a26b48af717"

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    val eventStream = MutableLiveData<Event>()
    val recyclerHelper = MainViewModelRecyclerHelper()
    val adapter = MainAadpter()
    val itemsList = ObservableArrayList<BaseRecyclerItem>()
    var movieList: MovieList? = null

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            mainRepository.getUsers(api_key)
                .catch {
                    eventStream.postValue(Event(ActivityEvent.FETCH_DATA_FROM_DB))
                }
                .collect {
                    movieList = it
                    val recyclerItems = recyclerHelper.getRecyclerItems(it, eventStream)
                    itemsList.addAll(recyclerItems)
                    adapter.initialItemsList.addAll(recyclerItems)
                    eventStream.postValue(Event(ActivityEvent.SAVE_DATA_TO_DB))
                }
        }
    }

    fun fetchDataFromDataBase(context: Context) {
        val recyclerItems = arrayListOf<MovieItemViewModel>()
        CoroutineScope(Dispatchers.IO).launch {
            val data = DataBaseRepository.getMovieData()
            data.forEach {
                recyclerItems.add(MovieItemViewModel(data = it.data, eventStream = eventStream))
            }
        }
        itemsList.addAll(recyclerItems)
    }

    fun saveDataToDb(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            movieList?.let {
                DataBaseRepository.deleteAllMovieDataFromDb()
                val movieNameTableList = arrayListOf<MovieNameTable>()
                it.results.forEach {
                    movieNameTableList.add(MovieNameTable(data = it,id = it.id, isSelected = false))
                }
                DataBaseRepository.saveMovieData(movieNameTableList)
            }
        }
    }

    fun openSearchFragment() {
        eventStream.postValue(Event(OPEN_SEARCH_FRAGMENT))
    }

    fun afterTextChanged(e:Editable) {
        adapter.exampleFilter.filter(e)
    }
}


//fun saveDataToDb(context: Context) {
//    val dataBaseDao = MovieDataBase.getInstance(context).getMovieNameDao()
//    CoroutineScope(Dispatchers.IO).launch {
//        val data  = DummyDataHolder().provideDummyData()
//        dataBaseDao.saveData(data)
//    }
//    CoroutineScope(Dispatchers.IO).launch {
//        val data = dataBaseDao.getData()
//        data.forEach {
//            Log.d("tag5",it.name)
//        }
//    }
//
////        val contentResolver = context.contentResolver.query(Uri.parse("content://authority_uri/movieListTable/1"),null,null,null)
////        Log.d("tag6",contentResolver.toString())
//}
