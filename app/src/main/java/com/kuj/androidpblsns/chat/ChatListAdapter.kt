package com.kuj.androidpblsns.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kuj.androidpblsns.chat.ChatData
import com.kuj.androidpblsns.chat.ChatViewModel
import com.kuj.androidpblsns.databinding.ChatlistBinding

class ChatListAdapter(private val viewModel: ChatViewModel, private val itemClicked: () -> Unit) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChatlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModel.chatRoomListLiveData.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.chatRoomListLiveData.value!![position])

        // 채팅방 목록에서 각 뷰 누르면 채팅 액티비티로
        holder.itemView.setOnClickListener {
            itemClicked()
        }
    }

    inner class ViewHolder(private val binding: ChatlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatData: ChatData) {
            binding.buyername.text = chatData.buyerName
            binding.currentchat.text = chatData.currentChat
        }
    }
}