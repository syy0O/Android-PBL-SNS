package com.kuj.androidpblsns.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.data.Message

class ChatRoomViewModel : ViewModel(){


    private val database = Firebase.database
    private val userRef =  Firebase.database.getReference("user")
    private val chatRef = database.getReference("chats")
    private val auth: FirebaseAuth by lazy{ Firebase.auth}


    private lateinit var receiverUid: String
    //접속자 Uid
    private val senderUid = auth.currentUser?.uid

    // chatting room setting
    private lateinit var receiverRoom: String // 받는 대화방
    private lateinit var senderRoom:String // 보낸 대화방

    // message
    private var messageList = ArrayList<Message>() //메시지 리스트
    private val _messageListLiveData = MutableLiveData<ArrayList<Message>>()
    val messageListLiveData: LiveData<ArrayList<Message>> get() = _messageListLiveData

    private val _chatReceiverTextLiveData = MutableLiveData<String>()
    val chatReceiverTextLiveData: LiveData<String> get() = _chatReceiverTextLiveData

    val messageSendSuccess = MutableLiveData(false)
    val getMessageFromDBSuccess = MutableLiveData(false)

    fun setReceiverUid(receiverUid: String) {
        this.receiverUid = receiverUid

        //보낸이 방
        senderRoom = receiverUid + senderUid

        //받는이 방
        receiverRoom = senderUid + receiverUid
    }

    fun sendMessageData(message: String) {
        val messageObject = Message(message, senderUid)

        chatRef.child(senderRoom).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                //저장 성공하면
//                    chatRef.child(receiverRoom).child("messages").push()
//                        .setValue(messageObject)
                messageSendSuccess.value = true
            }
    }

    // DB 객체 이용해 메시지 가져오기
    fun getMessageFromDB() {
        chatRef.child(senderRoom).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //데이터 가져오는 기능 구현
                    messageList.clear()
                    for(postSnapShat in snapshot.children){
                        val message = postSnapShat.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    // 적용
                    _messageListLiveData.value = messageList
                    getMessageFromDBSuccess.value = true
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun queryItem() {
        userRef.child(receiverUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<*, *>
                _chatReceiverTextLiveData.value = map["nickname"].toString()

                Log.d("아니 했는데..",_chatReceiverTextLiveData.value+"")

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}