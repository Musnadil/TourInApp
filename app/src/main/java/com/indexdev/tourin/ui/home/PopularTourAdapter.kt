package com.indexdev.tourin.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.indexdev.tourin.R
import com.indexdev.tourin.data.model.response.ResponseTourList
import com.indexdev.tourin.databinding.ItemTourBinding

class PopularTourAdapter(private val onClickItem: OnClickListener) :
    RecyclerView.Adapter<PopularTourAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<ResponseTourList>() {
        override fun areItemsTheSame(
            oldItem: ResponseTourList,
            newItem: ResponseTourList
        ): Boolean {
            return oldItem.idWisata == newItem.idWisata
        }

        override fun areContentsTheSame(
            oldItem: ResponseTourList,
            newItem: ResponseTourList
        ): Boolean {
            return oldItem.wisata == newItem.wisata
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitData(value: List<ResponseTourList>?) = differ.submitList(value)
    private val shimmer = Shimmer.AlphaHighlightBuilder()
        .setDuration(1800)
        .setBaseAlpha(0.7f)
        .setHighlightAlpha(0.6f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
    inner class ViewHolder(private val binding: ItemTourBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseTourList) {
            Glide.with(binding.root)
                .load(data.urlImage)
                .placeholder(shimmerDrawable)
                .transform(CenterCrop())
                .into(binding.ivTour)
            binding.tvTourName.text = data.wisata
            binding.tvRate.text = data.rating
            binding.root.setOnClickListener {
                onClickItem.onClickItem(data)
            }
        }

    }

    interface OnClickListener {
        fun onClickItem(data: ResponseTourList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemTourBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount() = differ.currentList.size
}