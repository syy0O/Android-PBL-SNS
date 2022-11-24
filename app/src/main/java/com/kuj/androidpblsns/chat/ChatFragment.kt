package com.kuj.androidpblsns.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.databinding.FragmentChatBinding
import com.kuj.androidpblsns.presentation.chat.ChatListAdapter

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatRoomListAdapter : ChatListAdapter

    private val viewModel by viewModels<ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChatBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){
        chatRoomListAdapter = ChatListAdapter(viewModel) {
            (activity as HomeActivity).changeFragmentWithBackStack(ChatRoomFragment.newInstance(viewModel.getTestSellerId()))
            //activity?.overridePendingTransition(R.anim.slide_up_enter,R.anim.none)
        }
        binding.chatList.apply {
            setHasFixedSize(true)
            adapter = chatRoomListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}