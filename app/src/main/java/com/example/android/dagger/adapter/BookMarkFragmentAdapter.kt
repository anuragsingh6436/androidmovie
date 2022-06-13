package com.example.android.dagger.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.dagger.R
import com.example.android.dagger.base.BaseAdapter
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.base.BaseViewHolder
import com.example.android.dagger.databinding.FragmentBookMarkItemBinding
import com.example.android.dagger.viewModel.BookMarkFragmentItemViewModel

class BookMarkFragmentAdapter:BaseAdapter() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseRecyclerItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding: FragmentBookMarkItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_book_mark_item, parent, false
        )
        return BookMarkFragmentViewHolder(dataBinding.root, dataBinding) as BaseViewHolder<BaseRecyclerItem>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseRecyclerItem>, position: Int) {
        holder.onBindData(this.items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class BookMarkFragmentViewHolder(itemView: View,val databinding:FragmentBookMarkItemBinding):BaseViewHolder<BookMarkFragmentItemViewModel>(itemView) {

    override fun onBindData(item: BookMarkFragmentItemViewModel, position: Int) {
        with(databinding) {
            viewModel = item
            executePendingBindings()
        }
    }

}