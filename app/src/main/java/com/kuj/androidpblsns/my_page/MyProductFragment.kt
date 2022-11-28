package com.kuj.androidpblsns.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuj.androidpblsns.ArticleViewModel
import com.kuj.androidpblsns.databinding.FragmentMyProductBinding
import com.kuj.androidpblsns.home.ArticleAdapter

class MyProductFragment : Fragment() {

    private lateinit var binding: FragmentMyProductBinding
    private val viewModel by activityViewModels<ArticleViewModel>()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyProductBinding.inflate(inflater, container, false)

        viewModel.initMyArticleData()

        initView()

        return binding.root
    }

    private fun initView(){
        articleAdapter = ArticleAdapter(requireContext())
        binding.myProductRv.apply {
            setHasFixedSize(true)
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.articleLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                articleAdapter.submitList(it)
            }
        }
    }
}