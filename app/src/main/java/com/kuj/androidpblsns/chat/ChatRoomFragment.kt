package com.kuj.androidpblsns.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.databinding.FragmentChatRoomBinding

private const val RECEIVER_UID = "receiverUid"
class ChatRoomFragment : Fragment() {

    private lateinit var binding: FragmentChatRoomBinding
    private val viewModel by viewModels<ChatRoomViewModel>()
    private var receiverUid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        arguments?.let {
            receiverUid = it.getString(RECEIVER_UID)
        }

        receiverUid?.let {
            initView()
        }

        return binding.root
    }

    private fun initView() {
        viewModel.setReceiverUid(receiverUid!!)

        val messageAdapter = MessageAdapter(requireContext(), viewModel)

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }

        viewModel.getMessageFromDB()

        // 채팅방 나가기 버튼
        binding.backButton.setOnClickListener{
            (activity as HomeActivity).removeFragment(this@ChatRoomFragment)
        }

        // DB 객체 이용해 메시지 가져오기
        viewModel.getMessageFromDBSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                messageAdapter.notifyDataSetChanged()
                viewModel.getMessageFromDBSuccess.value = false
            }
        }

        //메시지 전송버튼 이벤트
        binding.sendBtn.setOnClickListener{
            val message = binding.messageEdit.text.toString()

            viewModel.messageSendSuccess.observe(viewLifecycleOwner) { isSuccess ->
                if (isSuccess) {
                    // 메시지 입력창 초기화
                    binding.messageEdit.setText("")
                    viewModel.messageSendSuccess.value = false // 다시 초기화
                }
            }
        }

        // 대화상대이름 설정
        viewModel.chatReceiverTextLiveData.observe(viewLifecycleOwner) {
            binding.chatReceiverText.text = it
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(receiverUid: String) = ChatRoomFragment().apply {
            arguments = Bundle().apply {
                putString(RECEIVER_UID, receiverUid)
            }
        }
    }
}