package com.indexdev.tourin.ui.listratefacility

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.indexdev.tourin.data.model.response.Data
import com.indexdev.tourin.databinding.ItemRateFacilityBinding

class ListRateFacilityAdapter(private val onClickItem: OnClickListener) :
    RecyclerView.Adapter<ListRateFacilityAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.rateValue == newItem.rateValue
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.rateValue == newItem.rateValue
        }

    }
    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitData(value: List<Data>?) = differ.submitList(value)

    inner class ViewHolder(private val binding: ItemRateFacilityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: Data) {
            binding.tvTourName.text = data.namaWisata
            binding.tvFacilityName.text = data.namaFasilitas
            binding.root.setOnClickListener {
                onClickItem.onClickItem(data)
            }
        }
    }

    interface OnClickListener {
        fun onClickItem(data: Data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRateFacilityBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount() = differ.currentList.size
}