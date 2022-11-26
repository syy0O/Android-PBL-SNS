package com.kuj.androidpblsns.home

//<<<<<<< Updated upstream
//=======
//import com.kuj.androidpblsns.databinding.FragmentProductDeatilReBinding
//>>>>>>> Stashed changes

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
//<<<<<<< Updated upstream
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
//=======
//        binding = FragmentProductDeatilReBinding.inflate(inflater, container, false)
//>>>>>>> Stashed changes

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
        isRecentFollowing(firebaseAuth.currentUser?.uid!!,data.sellerId,false)


        binding.apply {
            deatilviewitemPrice.text = data.price
            detailviewitemTitle.text = data.title
            detailviewitemCreateAt.text = format.format(date).toString()
            detailviewitemContent.text = data.content
        }

        if (data.imageUrl.isNotEmpty()) {
            Glide.with(binding.detailviewitemImage)
                .load(data.imageUrl)
                .into(binding.detailviewitemImage)
        }

        binding.chatBtn.setOnClickListener{
            (activity as HomeActivity).changeFragmentWithBackStack(ChatRoomFragment.newInstance(data.sellerId)) // 테스트 용 sellerId 박아놓은 것
        }

        binding.followBtn.setOnClickListener{
            //팔로우 눌렀을 때
            isRecentFollowing(firebaseAuth.currentUser?.uid!!,data.sellerId,true)
        }
    }

    private fun isRecentFollowing(userId:String, followingId:String, isBtnClicked:Boolean){
            userRef.child(userId).child("following").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val following = dataSnapshot.value as HashMap<String, Boolean>

                    if (isBtnClicked == false){

                        if(following.containsKey(followingId)){
                            var currentValue = following.getValue(followingId)
                            if (currentValue == true){
                                binding.followBtn.text = "팔로우중"
                            }
                            else {
                                binding.followBtn.text = "팔로우"
                            }
                        }
                        else {
                            binding.followBtn.text = "팔로우"
                        }
                    }
                    else {
                        if(following.containsKey(followingId)){
                            var currentValue = following.getValue(followingId)
                            var removeFollower = userRef.child(followingId).child("follower").child(userId)
                            // 다른사람 uid = followingId
                            // 팔로워는 내가 팔로우 한 사람의 UID 자식노드 follower에 저장되며
                            // 알람리스트 및 푸시알람을 위해 사용
                            // 언팔하게되면 상대방 follower노드에 내 UID가 Remove 된다.

                            if (currentValue == true){
                                userRef.child(userId).child("following").child(followingId).setValue(false)
                                binding.followBtn.text = "팔로우"

                                //언팔했을경우 팔로워에서 내 UID... 삭제하고싶음..
                                //removeFollower.removeValue()
                                Log.v("##123##", removeFollower.toString())
                            }
                            else {
                                userRef.child(userId).child("following").child(followingId).setValue(true)
                                binding.followBtn.text = "팔로우중"
                                Log.v("####", followingId)
                                //팔로우 했으면 상대방 follower에 내 UID 저장
                                userRef.child(followingId).child("follower").setValue(userId)
                            }
                        }
                        else {
                            userRef.child(userId).child("following").child(followingId)
                                .setValue(true)
                            binding.followBtn.text = "팔로우중"
                        }
                    }
                }
            })
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