package com.kuj.androidpblsns.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuj.androidpblsns.databinding.ActivityAlarmlistBinding


class AlarmListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAlarmlistBinding.inflate(layoutInflater) }
    private lateinit var adapter: AlarmListAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(AlarmViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = AlarmListAdapter(this)

        val recyclerView : RecyclerView = binding.recycleralarmlist
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        observerData()

    }

    fun observerData(){
        viewModel.fetchData().observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}

