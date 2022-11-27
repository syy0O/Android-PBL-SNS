package com.kuj.androidpblsns.follwer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.alarm.AlarmListActivity
import com.kuj.androidpblsns.databinding.FragmentFollowerBinding
import com.kuj.androidpblsns.home.ArticleAdapter
import com.kuj.androidpblsns.home.ArticleViewModel
import com.kuj.androidpblsns.home.FollowerArticleViewModel
import com.kuj.androidpblsns.product.AddProductActivity

class FollowerFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var articleAdapter: ArticleAdapter
    private val viewModel by viewModels<FollowerArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter = ArticleAdapter(requireContext())

        viewModel.articleLiveData.observe(viewLifecycleOwner) {
            articleAdapter.submitList(it)
        }

        binding.alarmbtn.setOnClickListener {
            val intent = Intent(requireContext(), AlarmListActivity::class.java);
            startActivity(intent)
        }

        binding.articleRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }

    }
}