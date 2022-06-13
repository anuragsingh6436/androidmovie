package com.example.android.dagger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.android.dagger.R
import com.example.android.dagger.custom.VerticalItemDecorator
import com.example.android.dagger.databinding.FragmentBookMarkBinding
import com.example.android.dagger.event.ActivityEvent
import com.example.android.dagger.model.Event
import com.example.android.dagger.viewModel.BookMarkFragmentViewModel

class BookMarkFragment : Fragment() {

    companion object {
        const val TAG = "BookMarkFragment"
        fun getInstance():BookMarkFragment {
            return BookMarkFragment()
        }
    }

    lateinit var viewModel: BookMarkFragmentViewModel
    lateinit var dataBinding: FragmentBookMarkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BookMarkFragmentViewModel::class.java)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_mark, container, false)

        dataBinding.viewModel = viewModel
        addItemDecoration()
        return dataBinding.root
    }

    private fun addItemDecoration() {
        with(dataBinding) {
            recyclerView.addItemDecoration(VerticalItemDecorator(20))

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.eventStream.observe(viewLifecycleOwner) {
            handleEvents(it)
        }
    }

    private fun handleEvents(event: Event) {
        when(event.id) {
            ActivityEvent.CLOSE_BOOK_MARK_FRAGMENT -> {
                closeFragemnt()
            }
        }
    }

    private fun closeFragemnt() {
        (activity as FragmentActivity).supportFragmentManager.popBackStack()
    }
}