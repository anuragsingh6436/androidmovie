package com.example.android.dagger.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.dagger.R
import com.example.android.dagger.base.BaseAdapter
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.base.BaseViewHolder
import com.example.android.dagger.databinding.ActivityMovieDetailItemBinding
import com.example.android.dagger.viewModel.DetailActivityItemViewModel

class DetailAdapter : BaseAdapter() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseRecyclerItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding: ActivityMovieDetailItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_movie_detail_item, parent, false
        )
        return DetailViewHolder(dataBinding.root, dataBinding) as BaseViewHolder<BaseRecyclerItem>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseRecyclerItem>, position: Int) {
        holder.onBindData(this.items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DetailViewHolder(itemView: View, val dataBinding: ActivityMovieDetailItemBinding) :
        BaseViewHolder<DetailActivityItemViewModel>(itemView) {

        override fun onBindData(item: DetailActivityItemViewModel, position: Int) {
            with(dataBinding) {
                model = item
                executePendingBindings()
            }
        }
    }

}