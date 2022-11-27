package com.kuj.androidpblsns.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuj.androidpblsns.databinding.FragmentSearchBinding
import com.kuj.androidpblsns.ArticleViewModel
import com.kuj.androidpblsns.home.ArticleAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchListFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by activityViewModels<ArticleViewModel>()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        articleAdapter = ArticleAdapter(requireContext())

        binding.SearchRecyclerView.apply {
            setHasFixedSize(true)
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.searchProductBtn.setOnClickListener {
            Log.d("song2", "input: ${binding.editSearch.text.toString()}")
            viewModel.searchArticleModel(binding.editSearch.text.toString())
        }

        viewModel.searchSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                articleAdapter.submitList(viewModel.articleLiveData.value)
                viewModel.searchSuccess.value = false
            }
        }
    }


}