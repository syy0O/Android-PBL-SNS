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
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.ArticleViewModel
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.chat.ChatRoomFragment
import com.kuj.androidpblsns.databinding.FragmentProductDetailBinding

import com.kuj.androidpblsns.push.FcmPush

//<<<<<<< HEAD
//=======
//import com.kuj.androidpblsns.databinding.FragmentProductDeatilReBinding
//>>>>>>> Stashed changes
//=======
//>>>>>>> 3bd1d545e85c6a3841757d49ad81a87b547ab9c9
import java.text.SimpleDateFormat
import java.util.*


private const val POSITION = "position"

class ProductDetailFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentProductDetailBinding

    /** [ArticleViewModel]가 Activity 에서 생성되었기에 데이터가 남아있음 */
    private val viewModel by activityViewModels<ArticleViewModel>()//activityViewModels<ArticleViewModel>(), activityViewModels<FollowerArticleViewModel>()
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val userRef = Firebase.database.getReference("user")

    val namrRefdatabase = FirebaseDatabase.getInstance().reference
    val nameRef = namrRefdatabase.child("user")


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
        val data = viewModel.articleLiveData.value!![position]

        Log.d("ProductDetailFragment", position.toString())

        val format = SimpleDateFormat("MM월 dd일")
        val date = Date(data.createAt)

        database = Firebase.database.reference
        getSellerNickname(data.sellerId)

        binding.apply {
            deatilviewitemPrice.text = data.price
            detailviewitemTitle.text = data.title
            detailviewitemCreateAt.text = format.format(date).toString()
            detailviewitemContent.text = data.content
            binding.followBtn.text =
                if (!viewModel.followingUidLiveData.value!!.contains(data.sellerId)) "Follow" else "Unfollow"
        }

        val uid = firebaseAuth.currentUser!!.uid

        if (data.sellerId == uid) {
            binding.chatBtn.isEnabled = false
            binding.chatBtn.isClickable = false
            binding.followBtn.isEnabled = false
            binding.followBtn.isClickable = false
        }

        if (data.imageUrl.isNotEmpty()) {
            Glide.with(binding.detailviewitemImage)
                .load(data.imageUrl)
                .into(binding.detailviewitemImage)
        }

        binding.chatBtn.setOnClickListener {
            (activity as HomeActivity).changeFragment(ChatRoomFragment.newInstance(data.sellerId)) // 테스트 용 sellerId 박아놓은 것
        }

        binding.followBtn.setOnClickListener {
            //팔로우 눌렀을 때
            followingProcess(firebaseAuth.currentUser?.uid!!, data.sellerId)
        }
    }

    private fun followingProcess(userId: String, followingId: String) {

        var myFollowingList: List<String> = emptyList()
        var followUserList: List<String> = emptyList()

        userRef.child(userId).child("following").get().addOnSuccessListener {
            myFollowingList = it.value as List<String>
            if (followListValidation(myFollowingList, followUserList)) {
                Log.d("파송송", "followValidation Success ${myFollowingList} ${followUserList}")
                followProcess(userId, followingId, myFollowingList.toMutableList(), followUserList.toMutableList())
            }
        }

        userRef.child(followingId).child("follower").get().addOnSuccessListener {
            followUserList = it.value as List<String>
            if (followListValidation(myFollowingList, followUserList)) {
                Log.d("파송송", "followValidation Success ${myFollowingList} ${followUserList}")
                followProcess(userId, followingId, myFollowingList.toMutableList(), followUserList.toMutableList())
            }
        }
    }

    private fun followListValidation(myList: List<String>, followList: List<String>): Boolean {
        if (myList.isNotEmpty() && followList.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun followProcess(
        userId: String,
        followingId: String,
        myList: MutableList<String>,
        followList: MutableList<String>,
    ) {
        if (myList.contains(followingId)) {
            myList.remove(followingId)
            userRef.child(userId).child("following").setValue(myList)
            followList.remove(userId)
            userRef.child(followingId).child("follower").setValue(followList.toList())
            binding.followBtn.text = "Follow"
            viewModel.updateFollowData(myList)
            viewModel.removeSpecificFollower(followingId)

        } else { // 팔로우했을때
            myList.add(followingId)
            userRef.child(userId).child("following").setValue(myList.toList())
            followList.add(userId)
            userRef.child(followingId).child("follower").setValue(followList.toList())
            binding.followBtn.text = "UnFollow"

            viewModel.updateFollowData(myList)
            viewModel.addSpecificFollower(followingId)

            //팔로우한사람 nickname DB에서 가져오기
            sendNotificationToFollowingUser(userId, followingId)
        }

        viewModel.updateFollowArticle()
    }

    private fun sendNotificationToFollowingUser(userId: String, followingId: String) {
        val nickname = nameRef.child(userId).child("nickname")
        nickname.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val followerName = dataSnapshot.getValue(String::class.java)
                // 팔로우 푸시알람 보내기
                FcmPush.instance.sendMessage(followingId,
                    "팔로우 알림",
                    followerName + "님이 회원님을 팔로우합니다")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getSellerNickname(userID: String) {
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