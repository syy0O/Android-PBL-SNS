package com.kuj.androidpblsns

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



/**
 * A simple [Fragment] subclass.
 * Use the [MyPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPageFragment : Fragment() {

    private var auth : FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        val logoutButton : Button = view.findViewById(R.id.logoutButton)

        logoutButton.setOnClickListener {
            // 로그아웃 후 로그인 화면으로
            auth = Firebase.auth
            auth?.signOut()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }

        return view
    }
}