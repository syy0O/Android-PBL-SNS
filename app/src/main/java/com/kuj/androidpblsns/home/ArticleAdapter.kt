package com.kuj.androidpblsns.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.home.ArticleModel
import com.kuj.androidpblsns.databinding.ItemBinding
import com.kuj.androidpblsns.home.ProductDetailFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * ArticleAdapter는 Product View를 재사용하기 위해 사용됩니다.
 */
class ArticleAdapter(private val context: Context) :
    ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(articleModel: ArticleModel) {
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(articleModel.createAt)

            binding.titleTextView.text = articleModel.title
            binding.dateTextView.text = format.format(date).toString()
            binding.priceTextView.text = articleModel.price
            if (articleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(articleModel.imageUrl)
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                (context as HomeActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout, ProductDetailFragment.newInstance(adapterPosition))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel, ): Boolean {
                return oldItem.createAt == newItem.createAt
            }
            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel, ): Boolean {
                return oldItem == newItem
            }
        }
    }
}