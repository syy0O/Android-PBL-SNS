package com.kuj.androidpblsns.presentation.chat

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.chat.ChatActivity
import com.kuj.androidpblsns.chat.ChatData
import com.kuj.androidpblsns.databinding.ChatlistBinding

class ChatListAdapter(private val context: Context):
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){
    var datas = mutableListOf<ChatData>()

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): ViewHolder {
        val binding = ChatlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position:Int){
        holder.bind(datas[position])


        // 채팅방 목록에서 각 뷰 누르면 채팅 액티비티로
        holder.itemView.setOnClickListener{
                //Log.v("btnCheck", "버튼 눌림")
                val intent = Intent(holder.itemView.context, ChatActivity::class.java);
                intent.putExtra("sellerId","SJfYEZNOjZN4XJxksbZ390CkdX13"); // 테스트 용으로 박아놓은 것
                startActivity(holder.itemView.context, intent, null)
                //activity?.overridePendingTransition(R.anim.slide_up_enter,R.anim.none)
        }
    }
    inner class ViewHolder(private val binding : ChatlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(chatData: ChatData){
            binding.buyername.text = chatData.buyerName
            binding.currentchat.text = chatData.currentChat
        }
    }
}