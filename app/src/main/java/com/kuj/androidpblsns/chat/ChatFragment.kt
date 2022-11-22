package com.kuj.androidpblsns.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.databinding.ChatlistBinding
import com.kuj.androidpblsns.databinding.FragmentChatBinding
import com.kuj.androidpblsns.databinding.FragmentHomeBinding
import com.kuj.androidpblsns.presentation.chat.ChatListAdapter
import com.kuj.androidpblsns.product.AddProductActivity

class ChatFragment : Fragment() {

    private val binding by lazy { FragmentChatBinding.inflate(layoutInflater)}
    lateinit var ChatListAdapter : ChatListAdapter
    val datas = mutableListOf<ChatData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        initRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initRecycler(){
        ChatListAdapter = ChatListAdapter(context!!)
        binding.chatList.adapter = ChatListAdapter

        datas.apply{
            add(ChatData(buyerName = "중고조아연", currentChat = "얼마"))
            add(ChatData(buyerName = "싸게주셈", currentChat = "네고가능?"))
            add(ChatData(buyerName = "한성부기", currentChat = "신고합니다"))
        }
        ChatListAdapter.datas = datas
        ChatListAdapter.notifyDataSetChanged()

    }
}