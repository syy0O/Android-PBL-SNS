package com.kuj.androidpblsns.follwer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuj.androidpblsns.ArticleViewModel
import com.kuj.androidpblsns.alarm.AlarmListActivity
import com.kuj.androidpblsns.databinding.FragmentFollowerBinding
import com.kuj.androidpblsns.home.ArticleAdapter

class FollowerFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var articleAdapter: ArticleAdapter
    private val viewModel by activityViewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)

        viewModel.initFollowerFragmentData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter = ArticleAdapter(requireContext())

        viewModel.articleLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                articleAdapter.submitList(it)
            }
        }

        binding.articleRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }
}