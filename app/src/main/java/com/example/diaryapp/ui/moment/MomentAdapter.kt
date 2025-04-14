package com.example.diaryapp.ui.moment

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diaryapp.data.local.entities.Moment
import com.example.diaryapp.databinding.ItemMomentBinding
import java.time.format.DateTimeFormatter

class MomentAdapter(private val onClick: (Moment) -> Unit) :
    ListAdapter<Moment, MomentAdapter.MomentViewHolder>(MomentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        val binding = ItemMomentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MomentViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        val moment = getItem(position)
        holder.bind(moment)
    }

    inner class MomentViewHolder(private val binding: ItemMomentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val moment = getItem(adapterPosition)
                if (moment != null) {
                    onClick(moment)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(moment: Moment) {
            binding.title.text = moment.title
            binding.note.text = moment.note
            binding.location.text = moment.location

            // Преобразуем LocalDateTime в строку для отображения
            val formattedDateTime = moment.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            binding.date.text = formattedDateTime
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