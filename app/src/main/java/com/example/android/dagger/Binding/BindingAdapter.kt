package com.example.android.dagger.Binding

import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.dagger.adapter.MainAadpter
import com.example.android.dagger.base.BaseAdapter
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.viewModel.MovieItemViewModel
import com.squareup.picasso.Picasso


@BindingAdapter(value = ["imageUrl"],requireAll = false)
fun getImage(view:AppCompatImageView,imageUrl:String) {
    val url = "https://image.tmdb.org/t/p/original/${imageUrl}"
    Log.d("tag4",url)
    Picasso.get()
        .load(url)
        .into(view)
}

@BindingAdapter(value = ["adapter","itemsList"],requireAll = true)
fun updateRecyclerView(recyclerView: RecyclerView,adapter:BaseAdapter,itemsList:ArrayList<BaseRecyclerItem>) {
    if(recyclerView.adapter == null) recyclerView.adapter = adapter
    if(itemsList.isNotEmpty()){
        adapter.setItems(itemsList)
    }
}

@BindingAdapter(value = ["visibleGone"],requireAll = true)
fun toggleView(view:View,visible:Boolean) {
    if(visible)View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["visibleGoneByText"],requireAll = true)
fun toggleViewByText(view: View,text:String?) {
    if(text.isNullOrEmpty())View.GONE else View.VISIBLE
}

