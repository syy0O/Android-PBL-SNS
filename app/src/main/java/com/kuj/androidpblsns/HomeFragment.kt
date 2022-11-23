package com.kuj.androidpblsns

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.alarm.AlarmListActivity
import com.kuj.androidpblsns.databinding.FragmentHomeBinding
import com.kuj.androidpblsns.home.ArticleAdapter
import com.kuj.androidpblsns.home.ArticleViewModel
import com.kuj.androidpblsns.home.ArticleModel
import com.kuj.androidpblsns.product.AddProductActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    /** [ArticleViewModel]가 Activity 에서 생성되었기에 데이터가 남아있음 */
    private val viewModel by activityViewModels<ArticleViewModel>()

    private lateinit var articleAdapter: ArticleAdapter
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java);
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_up_enter, R.anim.none)
        }

        binding.articleRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }
}