package com.kuj.androidpblsns.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.chat.ChatRoomFragment
import com.kuj.androidpblsns.databinding.FragmentProductDetailBinding
import java.text.SimpleDateFormat
import java.util.*

private const val POSITION = "position"

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    /** [ArticleViewModel]가 Activity 에서 생성되었기에 데이터가 남아있음 */
    private val viewModel by activityViewModels<ArticleViewModel>()
    private val firebaseAuth:FirebaseAuth by lazy{Firebase.auth}
    private val userRef =  Firebase.database.getReference("user")

    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            position = it.getInt(POSITION)
        }

        initView()

        return binding.root
    }

    private fun initView() {
        val position = arguments?.getInt(POSITION)
        val data = viewModel.articleLiveData.value!![position!!]

        Log.d("ProductDetailFragment", position.toString())

        val format = SimpleDateFormat("MM월 DD일")
        val date = Date(data.createAt)

        queryItem(data.sellerId)

        binding.apply {
            deatilviewitemPrice.text = data.price
            detailviewitemTitle.text = data.title
            detailviewitemCreateAt.text = format.format(date).toString()
        }

        if (data.imageUrl.isNotEmpty()) {
            Glide.with(binding.detailviewitemImage)
                .load(data.imageUrl)
                .into(binding.detailviewitemImage)
        }

        binding.chatBtn.setOnClickListener{
            (activity as HomeActivity).changeFragmentWithBackStack(ChatRoomFragment.newInstance(data.sellerId)) // 테스트 용 sellerId 박아놓은 것
        }
    }

    private fun queryItem(userID: String) {
        userRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<*, *>
                binding.detailviewitemProfileName.text = map["nickname"].toString()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}