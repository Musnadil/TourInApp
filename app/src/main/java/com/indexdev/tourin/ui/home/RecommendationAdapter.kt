package com.indexdev.tourin.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.indexdev.tourin.data.model.response.ResponseRecommendation
import com.indexdev.tourin.databinding.ItemTourBinding

class RecommendationAdapter(private val onClickItem: OnclickListener) :
    RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<ResponseRecommendation>() {
        override fun areItemsTheSame(
            oldItem: ResponseRecommendation,
            newItem: ResponseRecommendation
        ): Boolean {
            return oldItem.kodeWisata == newItem.kodeWisata
        }

        override fun areContentsTheSame(
            oldItem: ResponseRecommendation,
            newItem: ResponseRecommendation
        ): Boolean {
            return oldItem.wisata == newItem.wisata
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<ResponseRecommendation>?) = differ.submitList(value)
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
        fun bind(data: ResponseRecommendation) {
            Glide.with(binding.root)
                .load(data.urlImage)
                .placeholder(shimmerDrawable)
                .transform(CenterCrop())
                .into(binding.ivTour)
            binding.tvTourName.text = data.wisata
            if (data.rating == "0") {
                binding.icStar.visibility = View.GONE
                binding.tvRate.visibility = View.GONE
            } else {
                binding.tvRate.text = data.rating.take(3)
            }
            binding.root.setOnClickListener {
                onClickItem.onClickItem(data)
            }
        }
    }

    interface OnclickListener {
        fun onClickItem(data: ResponseRecommendation)
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