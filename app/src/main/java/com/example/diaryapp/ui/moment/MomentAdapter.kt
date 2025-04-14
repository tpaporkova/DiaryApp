package com.example.diaryapp.ui.moment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diaryapp.data.local.entities.Moment
import com.example.diaryapp.databinding.ItemMomentBinding

class MomentAdapter(private val onClick: (Moment) -> Unit) :
    ListAdapter<Moment, MomentAdapter.MomentViewHolder>(MomentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        val binding = ItemMomentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MomentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        val moment = getItem(position)
        holder.bind(moment)
    }

    inner class MomentViewHolder(private val binding: ItemMomentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val moment = getItem(adapterPosition)
                Log.d("MomentAdapter", "Item clicked: ${moment.id}")  // Логируем клик по элементу
                onClick(moment)
            }
        }

        fun bind(moment: Moment) {
            binding.note.text = moment.note
            binding.location.text = moment.location
            binding.date.text = moment.date
            binding.type.text = moment.type
        }
    }
}

class MomentDiffCallback : DiffUtil.ItemCallback<Moment>() {
    override fun areItemsTheSame(oldItem: Moment, newItem: Moment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Moment, newItem: Moment): Boolean {
        return oldItem == newItem
    }
}
