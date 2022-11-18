package com.kuj.androidpblsns.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.databinding.FragmentProductDetailBinding
import com.kuj.androidpblsns.databinding.ItemBinding
import com.kuj.androidpblsns.home.ArticleAdapter.Companion.diffUtil
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailAdapter (private val context: Context) : ListAdapter<ArticleModel, ProductDetailAdapter.ViewHolder>(diffUtil){
    inner class ViewHolder (private val binding: FragmentProductDetailBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(articleModel: ArticleModel){
            val format = SimpleDateFormat("MM월 DD일")
            val date = Date(articleModel.createAt)

            binding.detailviewitemProfileName.text = articleModel.sellerId
            binding.deatilviewitemPrice.text = articleModel.price
            binding.detailviewitemTitle.text = articleModel.title
            binding.detailviewitemCreateAt.text = format.format(date).toString()

            binding.root.setOnClickListener{
                (context as HomeActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout, ProductDetailFragment.newInstance(adapterPosition))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentProductDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
