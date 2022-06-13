package com.example.android.dagger.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.android.dagger.Network.ApiHelper
import com.example.android.dagger.Network.RetrofitBuilder
import com.example.android.dagger.R
import com.example.android.dagger.custom.VerticalItemDecorator
import com.example.android.dagger.databinding.ActivityMainBinding
import com.example.android.dagger.event.ActivityEvent
import com.example.android.dagger.factory.ViewModelFactory
import com.example.android.dagger.model.Event
import com.example.android.dagger.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var dataBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.viewModel = mainViewModel

        mainViewModel.eventStream.observe(this) {
            handleEvents(it)
        }

        addItemDecoration()
    }

    private fun addItemDecoration() {
        with(dataBinding) {
            recyclerView.addItemDecoration(VerticalItemDecorator(20))

        }
    }

    private fun handleEvents(event: Event) {
        when (event.id) {
            ActivityEvent.SAVE_DATA_TO_DB -> {
                mainViewModel.saveDataToDb(this)
            }
            ActivityEvent.FETCH_DATA_FROM_DB -> {
                mainViewModel.fetchDataFromDataBase(applicationContext)
            }
            ActivityEvent.OPEN_DETAIL_PAGE -> {
                openDetailPage(event.data as Int)
            }
        }
    }

    private fun openDetailPage(id: Int) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.INTENT_ID, id.toString())
        }
        startActivity(intent)
    }
}
