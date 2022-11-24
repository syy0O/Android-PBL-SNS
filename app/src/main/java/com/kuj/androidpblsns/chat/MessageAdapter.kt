package com.kuj.androidpblsns.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kuj.androidpblsns.R

class MessageAdapter(private val context: Context, private val viewModel: ChatRoomViewModel):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    // 메시지 타입(수신,송신)에 따라 ViewHolder 다르게 설정
    private val receive = 1 // 받는 타입
    private val send = 2 // 보내는 타입

    //화면 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1){ // 받는 화면 연결
            val view:View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            ReceiveViewHolder(view)
        }else{ // 보낸 화면 연결
            val view:View = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
            SendViewHolder(view)
        }
    }

    // view와 데이터 연결
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //현재 메시지
        val currentMessage = viewModel.messageListLiveData.value!![position]

        //보내는 데이터
        if(holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            viewHolder.sendMessage.text =
                currentMessage.message // viewHolder로 sendMessage(Textview)에 접근 -> 값 넣어주기
        } else{
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message // viewHolder로 sendMessage(Textview)에 접근 -> 값 넣어주기
        }
    }

    override fun getItemCount(): Int {
        return viewModel.messageListLiveData.value?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        //메시지 값
        val currentMessage = viewModel.messageListLiveData.value!![position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)){
            send
        }else {
            receive
        }
    }

    //보낸 쪽
    class SendViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){ // 보낸 쪽 View를 전달 받아 객체 생성
        val sendMessage: TextView = itemView.findViewById(R.id.send_message_text)
    }

    //받는 쪽
    class ReceiveViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){ //받는쪽 view를 전달받아 객체 생성
        val receiveMessage:TextView = itemView.findViewById(R.id.receive_message_text)
    }

}
