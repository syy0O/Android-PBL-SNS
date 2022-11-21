//package com.kuj.androidpblsns.product
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.kuj.androidpblsns.ArticleViewModel
//import com.kuj.androidpblsns.databinding.FragmentProductDetailBinding
//
//private const val POSITION = "position"
//
//class ProductDetailFragment : Fragment() {
//
//    private lateinit var binding: FragmentProductDetailBinding
//    /** [ArticleViewModel]가 Activity 에서 생성되었기에 데이터가 남아있음 */
//    private val viewModel by activityViewModels<ArticleViewModel>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val position = arguments?.getInt(POSITION)
//        val data = position?.let { viewModel.articleLiveData.value?.get(it) }
//        Log.d("ProductDetailFragment", position.toString())
//
//        /**
//         * TODO 가져온 data를 통해서 관리해주면 됩니다.
//         */
//
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance(position: Int) =
//            ProductDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(POSITION, position)
//                }
//            }
//    }
//}