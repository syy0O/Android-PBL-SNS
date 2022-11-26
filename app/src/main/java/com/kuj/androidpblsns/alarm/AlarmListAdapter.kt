package com.kuj.androidpblsns.alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.protobuf.Internal.ListAdapter
import com.kuj.androidpblsns.databinding.AlarmlistBinding
import com.kuj.androidpblsns.databinding.ItemBinding
import com.kuj.androidpblsns.home.ArticleAdapter
import com.kuj.androidpblsns.home.ArticleModel
import kotlinx.android.synthetic.main.alarmlist.view.*

class AlarmListAdapter(private val context: Context):
    RecyclerView.Adapter<AlarmListAdapter.ViewHolder>(){

    private var alarmList = mutableListOf<AlarmData>()

    fun setListData(data:MutableList<AlarmData>){
        alarmList = data
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): AlarmListAdapter.ViewHolder {
        val binding = AlarmlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //view = binding
        return ViewHolder(binding) //customViewHolder = ViewHoldr
    }

    override fun getItemCount(): Int = alarmList.size


    override fun onBindViewHolder(holder : AlarmListAdapter.ViewHolder, position: Int){
        holder.bind(alarmList[position])
    }

    inner class ViewHolder(private val binding : AlarmlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(alarmData: AlarmData){
            binding.alarmtitle.text = alarmData.userName
        }
    }
}




