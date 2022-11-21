package com.kuj.androidpblsns.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuj.androidpblsns.databinding.FollowlistBinding

class FollowerListAdapter(private val context: Context):
    RecyclerView.Adapter<FollowerListAdapter.ViewHolder>(){
    var datas = mutableListOf<FollowerData>()


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): ViewHolder {
        val binding = FollowlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size


    override fun onBindViewHolder(holder : ViewHolder, position: Int){
        holder.bind(datas[position])
    }

    inner class ViewHolder(private val binding : FollowlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(followerData: FollowerData){
            binding.followerEmail.text = followerData.email
            binding.followerNickname.text = followerData.nickname
        }
    }
}