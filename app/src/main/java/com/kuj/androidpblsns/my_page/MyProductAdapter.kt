package com.kuj.androidpblsns.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuj.androidpblsns.databinding.MyproductBinding

class MyProductAdapter(private val context: Context):
    RecyclerView.Adapter<MyProductAdapter.ViewHolder>(){
    var datas = mutableListOf<MyProductData>()


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): ViewHolder {
        val binding = MyproductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size


    override fun onBindViewHolder(holder : ViewHolder, position: Int){
        holder.bind(datas[position])
    }

    inner class ViewHolder(private val binding : MyproductBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(myProductData: MyProductData){
            binding.myProductPrice.text = myProductData.price
            binding.myProductTitle.text = myProductData.title
        }
    }
}