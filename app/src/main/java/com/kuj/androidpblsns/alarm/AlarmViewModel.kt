package com.kuj.androidpblsns.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmViewModel : ViewModel(){
    private val repo = AlarmRepo()
    fun fetchData() : LiveData<MutableList<AlarmData>>{
        val mutableData = MutableLiveData<MutableList<AlarmData>>()
        repo.getData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }
}