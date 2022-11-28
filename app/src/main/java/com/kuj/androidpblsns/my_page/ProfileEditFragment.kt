package com.kuj.androidpblsns.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kuj.androidpblsns.ArticleViewModel
import com.kuj.androidpblsns.databinding.FragmentEditProfileBinding

class ProfileEditFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel by activityViewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        binding.editNickNameBtn.setOnClickListener {
            viewModel.editUserNickName(binding.editNickName.text.toString())
        }

        viewModel.editProfileSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == true) {
                Toast.makeText(requireContext(), "닉네임 변경 완료", Toast.LENGTH_SHORT).show()
                viewModel.editProfileSuccess.value = null
                binding.editNickName.setText("")

            } else if (isSuccess == false) {
                Toast.makeText(requireContext(),  "변경에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                viewModel.editProfileSuccess.value = null
            }
        }
    }
}