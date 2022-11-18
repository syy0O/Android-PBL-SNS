package com.kuj.androidpblsns.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.databinding.FragmentProductDeatilReBinding
import com.kuj.androidpblsns.databinding.FragmentProductDetailBinding
import com.kuj.androidpblsns.databinding.ItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.fragment_product_detail.view.*
import kotlinx.android.synthetic.main.fragment_product_deatil_re.view.*


private const val POSITION = "position"

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDeatilReBinding
    /** [ArticleViewModel]가 Activity 에서 생성되었기에 데이터가 남아있음 */
    private val viewModel by activityViewModels<ArticleViewModel>()
    private lateinit var productdetailadapter: ProductDetailAdapter
    private val firebaseAuth:FirebaseAuth by lazy{Firebase.auth}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDeatilReBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(POSITION)
        val data = position?.let { viewModel.articleLiveData.value?.get(it) }
        Log.d("ProductDetailFragment", position.toString())

        /**
         * TODO 가져온 data를 통해서 관리해주면 됩니다.
         */
        productdetailadapter = ProductDetailAdapter(requireContext())
        viewModel.articleLiveData.observe(viewLifecycleOwner){
            productdetailadapter.submitList(it)
        }

        binding.productDetailRecyclerView.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productdetailadapter
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}