package com.kuj.androidpblsns.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuj.androidpblsns.alarm.AlarmData
import com.kuj.androidpblsns.databinding.AlarmlistBinding
import com.kuj.androidpblsns.databinding.FollowlistBinding

class AlarmListAdapter(private val context: Context):
    RecyclerView.Adapter<AlarmListAdapter.ViewHolder>(){
    var datas = mutableListOf<AlarmData>()


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): ViewHolder {
        val binding = AlarmlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size


    override fun onBindViewHolder(holder : ViewHolder, position: Int){
        holder.bind(datas[position])
    }

    inner class ViewHolder(private val binding : AlarmlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(alarmData: AlarmData){
            binding.alarmtitle.text = alarmData.alarm
        }
    }
}