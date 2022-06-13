package com.example.android.dagger.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.android.dagger.R
import com.example.android.dagger.adapter.DetailAdapter
import com.example.android.dagger.dataBase.DataBaseRepository
import com.example.android.dagger.dataBase.MovieBookMarkIdTable
import com.example.android.dagger.databinding.ActivityDetailBottomSheetDialogBinding
import com.example.android.dagger.databinding.ActivityMovieDetailBinding
import com.example.android.dagger.event.ActivityEvent
import com.example.android.dagger.model.Event
import com.example.android.dagger.viewModel.DetailActivityItemViewModel
import com.example.android.dagger.viewModel.DetailActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    lateinit var viewModel: DetailActivityViewModel
    lateinit var dataBinding: ActivityMovieDetailBinding

    var bottomSheetDialog: BottomSheetDialog? = null

    var movieId: String? = null

    companion object {
        const val INTENT_ID = "INTENT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        viewModel = ViewModelProvider(this).get(DetailActivityViewModel::class.java)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        dataBinding.viewModel = viewModel

        viewModel.eventStream.observe(this) {
            handleEvents(it)
        }

        handleIntent(intent)
        viewModel.initData(movieId)

        movieId?.let { //check initially if the selected item is bookmarked or not
            checkSelectedItemStatus()
        }
    }

    private fun handleIntent(intent: Intent) {
        val applinkAction = intent.action
        val appLinkData = intent.data.toString()
        if (Intent.ACTION_VIEW == applinkAction) {
            val queryMapIndex = appLinkData.indexOf("?")
            val queryString = appLinkData.substring(queryMapIndex + 1)
            val queryParams = queryString.split("&")
            queryParams.forEach {
                val values = it.split("=")
                if (values.size == 2 && values[0] == INTENT_ID) {
                    movieId = values[1]
                    return@forEach
                }
            }
        } else {
            movieId = intent.extras?.getString(INTENT_ID)
        }
    }

    private fun handleEvents(event: Event) {
        when (event.id) {

            ActivityEvent.OPEN_SHARE_LINK_FLOW -> {
                openShareFlow()
            }
            ActivityEvent.BOOKMARK_ICON_CLICKED -> {
                saveOrDeleteDataFromDb(event.data as Pair<MovieBookMarkIdTable,Boolean>)
            }
            ActivityEvent.CLOSE_BOTTOM_SHEET_DIALOG -> {
                bottomSheetDialog?.cancel()
            }
            ActivityEvent.CLOSE_DETAIL_ACTIVITY -> {
                Toast.makeText(this, "NO DATA RECEIVED FROM API", Toast.LENGTH_LONG).show()
                finish()
            }
            ActivityEvent.OPEN_BOOKMARK_FRAGMENT -> {
                bottomSheetDialog?.cancel()
                openBookMarkFragment()
            }
        }
    }

    private fun openBookMarkFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, BookMarkFragment.getInstance(), BookMarkFragment.TAG)
            .addToBackStack(BookMarkFragment.TAG).commitAllowingStateLoss()
    }

    private fun checkSelectedItemStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = DataBaseRepository.getBookMarkData(movieId!!.toInt())
            if (data != null) {
                viewModel.isMovieSelected.set(true)
            }
        }
    }

    private fun saveOrDeleteDataFromDb(data: Pair<MovieBookMarkIdTable,Boolean>) {
        if (!data.second) { //delete data from Db
            CoroutineScope(Dispatchers.IO).launch {
                DataBaseRepository.deleteParticularBookMarkekItem(movieId!!.toInt())
            }
        } else { //save data in Db
            CoroutineScope(Dispatchers.IO).launch {
                DataBaseRepository.saveBookMarkData(data.first)
            }
        }
        showBottomSheet()
    }

    private fun openShareFlow() {
        val deeplink = "https://www.dagger.com/detail?INTENT_ID=${movieId}"
        val intent = Intent.createChooser(createShareIntent("subject", deeplink), "title")
        this.startActivity(intent)
    }

    private fun createShareIntent(subject: String?, textToShare: String?): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        return shareIntent
    }

    private fun showBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val binding: ActivityDetailBottomSheetDialogBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_detail_bottom_sheet_dialog,
            null,
            false
        )
        binding.viewModel = viewModel
        bottomSheetDialog?.setContentView(binding.root)

        bottomSheetDialog?.setOnShowListener { dialogv1 ->
            val dialog = dialogv1 as BottomSheetDialog
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
                BottomSheetBehavior.from(it).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isHideable = true
                }
            }
        }
        bottomSheetDialog?.show()
    }

}