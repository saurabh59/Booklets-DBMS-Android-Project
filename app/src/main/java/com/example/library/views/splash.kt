package com.example.library.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.library.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class splash : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.progressCircular.isVisible = true
        val userId = firebaseAuth.currentUser?.uid
        if(userId != null) {
            val documentRef = db.collection("Users").document(userId)
            documentRef.get().addOnSuccessListener {
                if (it.getLong("isUser") == 1L) {
                    (activity as NavigationHost).navigateTo(HomeFragment(), false)
                } else {
                    (activity as NavigationHost).navigateTo(AdminFragment(), false)
                }
            }
        } else {
            (activity as NavigationHost).navigateTo(LoginFragment(), false)
        }
        binding.progressCircular.isVisible = false

    }

}