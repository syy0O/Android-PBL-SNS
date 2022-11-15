package com.kuj.androidpblsns.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kuj.androidpblsns.databinding.ItemBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter (val onItemClicked:(ArticleModel)->Unit) : ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil){
    inner class ViewHolder(private val binding:ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articleModel: ArticleModel) {
            val format = SimpleDateFormat("MM월 DD일")
            val date = Date(articleModel.createAt)

            binding.titleTextView.text = articleModel.title
            binding.dateTextView.text = format.format(date).toString()
            binding.priceTextView.text = articleModel.price

            binding.root.setOnClickListener {
                onItemClicked(articleModel)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        return ViewHolder(
            ItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder:ViewHolder, position:Int){
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>(){
            override fun areItemsTheSame(
                oldItem: ArticleModel,
                newItem: ArticleModel
            ): Boolean {
                return  oldItem.createAt == newItem.createAt
            }

            override fun areContentsTheSame(
                oldItem: ArticleModel,
                newItem: ArticleModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}