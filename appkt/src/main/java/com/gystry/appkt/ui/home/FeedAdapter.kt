package com.gystry.appkt.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gystry.appkt.databinding.LayoutFeedTypeImageBinding
import com.gystry.appkt.databinding.LayoutFeedTypeVideoBinding
import com.gystry.appkt.model.Feed
import com.gystry.appkt.model.Feed.Companion.TYPE_IMAGE

/**
 * @author gystry
 * 创建日期：2021/3/31 11
 * 邮箱：gystry@163.com
 * 描述：DiffUtil.ItemCallback<Feed> 数据做差分异时候的回调对象
 */
class FeedAdapter(context: Context) : PagedListAdapter<Feed, FeedAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Feed>() {
    override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        //判断两个item是否是同一个，比如一个item中以id作为主要标识，那么两个item的id一样，就表示为一个item。
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        return oldItem == newItem
    }

}) {
    val inflate: LayoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.itemType!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = if (viewType == TYPE_IMAGE) {
            LayoutFeedTypeImageBinding.inflate(inflate)
        } else {
            LayoutFeedTypeVideoBinding.inflate(inflate)
        }
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    class ViewHolder(itemView: View, binding: ViewDataBinding) : RecyclerView.ViewHolder(itemView) {
        private val mBinding=binding
        fun bindData(item: Feed?) {
            if (mBinding is LayoutFeedTypeImageBinding) {
                val layoutFeedTypeImageBinding = mBinding as LayoutFeedTypeImageBinding
            }else if (mBinding is LayoutFeedTypeVideoBinding){

            }
        }

    }
}