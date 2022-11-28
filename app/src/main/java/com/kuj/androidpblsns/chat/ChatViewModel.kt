package com.kuj.androidpblsns.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {

    private val testSellerId = "3QPZ7ot3TXdcO97n1xzYVbJPH6H2"
    private val chatRoomList = mutableListOf<ChatData>()
    private val _chatRoomListLiveData = MutableLiveData<List<ChatData>>()
    val chatRoomListLiveData get() = _chatRoomListLiveData

    init {
        /* TODO 실제 DB 데이터 가져온 후 dummy 데이터 지울 것. */
        initDummyData()
    }

    private fun initDummyData() {
        chatRoomList.apply{
            add(ChatData(buyerName = "중고조아연", currentChat = "얼마"))
            add(ChatData(buyerName = "싸게주셈", currentChat = "네고가능?"))
            add(ChatData(buyerName = "한성부기", currentChat = "신고합니다"))
        }
        _chatRoomListLiveData.value = chatRoomList
    }

    fun addChatRoom(chatData: ChatData) {
        chatRoomList.add(chatData)
        _chatRoomListLiveData.value = chatRoomList
    }

    fun getTestSellerId() = testSellerId
}