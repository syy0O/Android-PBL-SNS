package com.kuj.androidpblsns.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.chat.ChatActivity
import com.kuj.androidpblsns.databinding.FragmentProductDetailBinding
import com.kuj.androidpblsns.databinding.ItemBinding
import com.kuj.androidpblsns.home.ArticleAdapter.Companion.diffUtil
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailAdapter (private val context: Context) : ListAdapter<ArticleModel, ProductDetailAdapter.ViewHolder>(diffUtil){
    private val userRef =  Firebase.database.getReference("user")
    inner class ViewHolder (private val binding: FragmentProductDetailBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(articleModel: ArticleModel){
            val format = SimpleDateFormat("MM월 DD일")
            val date = Date(articleModel.createAt)

//            binding.detailviewitemProfileName.text = articleModel.sellerId
            queryItem(articleModel.sellerId)
            binding.deatilviewitemPrice.text = articleModel.price
            binding.detailviewitemTitle.text = articleModel.title
            binding.detailviewitemCreateAt.text = format.format(date).toString()
            if (articleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.detailviewitemImage)
                    .load(articleModel.imageUrl)
                    .into(binding.detailviewitemImage)
            }

            binding.root.setOnClickListener{
                (context as HomeActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout, ProductDetailFragment.newInstance(adapterPosition))
                    .addToBackStack(null)
                    .commit()
            }

            binding.chatBtn.setOnClickListener{
                val intent = Intent(context, ChatActivity::class.java);
                intent.putExtra("sellerId", articleModel.sellerId); // 테스트 용으로 박아놓은 것
                startActivity(context,intent,null)
            }
        }
        private fun queryItem(userID: String) {
            userRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map = dataSnapshot.value as Map<*, *>
                    binding.detailviewitemProfileName.text = map["nickname"].toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
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
