package com.kuj.androidpblsns.my_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kuj.androidpblsns.databinding.AcitivityMyproductBinding

class MyProductActivity : AppCompatActivity() {

    private val binding by lazy { AcitivityMyproductBinding.inflate(layoutInflater)}
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    lateinit var myProductAdapter: MyProductAdapter
    val datas = mutableListOf<MyProductData>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
    }
    private fun initRecycler(){
        myProductAdapter = MyProductAdapter(this)
        binding.recyclermyproductlist.adapter = myProductAdapter

        datas.apply{
            add(MyProductData(title = "포켓몬 빵", price = "3000원"))
            add(MyProductData(title = "에어팟", price = "200000원"))
            add(MyProductData(title = "노트북", price = "500000원"))
        }
        myProductAdapter.datas = datas
        myProductAdapter.notifyDataSetChanged()

    }
}