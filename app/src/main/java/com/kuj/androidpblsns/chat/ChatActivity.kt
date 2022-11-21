package com.kuj.androidpblsns.chat

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.data.Message
import com.kuj.androidpblsns.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {


    private lateinit var receiverUid: String //대화 상대 정보 담을 변수
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) } // 바인딩 객체

    //Firebase real-time database setting
    private val database = Firebase.database
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userRef =  Firebase.database.getReference("user")
    private val chatRef = database.getReference("chats")

    // chatting room setting
    private lateinit var receiverRoom: String // 받는 대화방
    private lateinit var senderRoom:String // 보낸 대화방

    // message
    private lateinit var messageList: ArrayList<Message> //메시지 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* Section: 초기화 */
        messageList = ArrayList()  //메시지 리스트 초기화
        val messageAdapter:MessageAdapter = MessageAdapter(this, messageList)

        //RecyclerView - adpater 연결
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = messageAdapter

        //넘어온 데이터(대화 상대 정보) 변수에 담기
        receiverUid = intent.getStringExtra("sellerId").toString()
        queryItem(receiverUid)

        //접속자 Uid
        val senderUid = auth.currentUser?.uid

        //보낸이 방
        senderRoom = receiverUid + senderUid

        //받는이 방
        receiverRoom = senderUid + receiverUid

        // 채팅방 나가기 버튼
        binding.backButton.setOnClickListener{
            finish()
        }

        //메시지 전송버튼 이벤트
        binding.sendBtn.setOnClickListener{
            val message = binding.messageEdit.text.toString()
            val messageObject = Message(message, senderUid)

            //데이터 저장
            chatRef.child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //저장 성공하면
                    chatRef.child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            // 메시지 입력창 초기화
            binding.messageEdit.setText("")
        }

        // DB 객체 이용해 메시지 가져오기
        chatRef.child(senderRoom).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   //데이터 가져오는 기능 구현
                    messageList.clear()
                    for(postSnapshat in snapshot.children){
                        val message = postSnapshat.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    // 적용
                    messageAdapter.notifyDataSetChanged() // 화면에 메시지 내용 보여주기
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
    private fun queryItem(userID: String) {
        userRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<*, *>
                binding.chatReceiverText.text = map["nickname"].toString()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }


}