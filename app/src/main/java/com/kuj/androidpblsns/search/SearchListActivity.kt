package com.kuj.androidpblsns.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.databinding.FragmentSearchBinding
import com.kuj.androidpblsns.home.ArticleModel
import com.kuj.androidpblsns.home.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchListActivity : AppCompatActivity(){
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }

    /** 이 객체가 초기화될 때 [ArticleViewModel]에서 init 발생 */
    private val viewModel by viewModels<ArticleViewModel>()
    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child("articles")
    }
    private lateinit var searchListAdapter: SearchListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
    }
    private fun initRecycler(){
        searchListAdapter = SearchListAdapter(this)
        binding.SearchRecyclerView.adapter = searchListAdapter
        searchinnerBtn.setOnClickListener {
                articleDB.orderByChild("title").equalTo(editSearch.text.toString()).addChildEventListener(object:
                    ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val result = snapshot.getValue(ArticleModel::class.java)
                        result ?: return

                        viewModel.addArticleModel(result)
                        Log.d("AddMemberActivity", "onChildAdded:" +result?.title.toString())
                    }
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {Log.d("AddMemberActivity", "onCancelled:$error") }
                })
            }
        }
    }