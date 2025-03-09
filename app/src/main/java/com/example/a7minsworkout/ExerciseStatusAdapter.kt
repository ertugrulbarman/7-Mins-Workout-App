package com.example.a7minsworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minsworkout.databinding.ItemExersiceStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

        class ViewHolder(binding: ItemExersiceStatusBinding):
            RecyclerView.ViewHolder(binding.root){
                val tvItem = binding.tvItem

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemExersiceStatusBinding.inflate(
           LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.tvItem.text = model.getId().toString()

        when{
            model.getIsSelected() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.item_circular_thin_color_accent_border)
                holder.tvItem.setTextColor(Color.parseColor("#FF000000"))
            }
            model.getIsCompleted() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.item_circular_color_bg)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            else -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.circlular_progress_gray_bg)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
        }
    }
}