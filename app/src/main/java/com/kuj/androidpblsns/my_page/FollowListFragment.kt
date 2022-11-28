package com.kuj.androidpblsns.my_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuj.androidpblsns.ArticleViewModel
import com.kuj.androidpblsns.databinding.FragmentFollowListBinding

class FollowListFragment : Fragment() {

    private lateinit var binding: FragmentFollowListBinding

    lateinit var followerListAdapter: FollowerListAdapter
    private val viewModel by activityViewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFollowListBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        followerListAdapter = FollowerListAdapter(viewModel, requireContext())

        binding.recyclerfollowerlist.apply {
            setHasFixedSize(true)
            adapter = followerListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.followingTotalDataLiveData.observe(viewLifecycleOwner) {
            Log.d("song아 왜그래", "${it}")
            followerListAdapter.notifyDataSetChanged()
        }

    }
}