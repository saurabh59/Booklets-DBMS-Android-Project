package com.example.library.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.library.data.User
import com.example.library.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var createAccountInputArray: Array<EditText>
    private lateinit var userEmail: String
    private lateinit var userName: String
    private lateinit var userPassword: String
    private var db = Firebase.firestore
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater,
        container, false)
        createAccountInputArray = arrayOf(binding.nameETC, binding.emailETC, binding.passwordETC, binding.confirmPasswordETC)
        binding.loginButtonC.setOnClickListener {
            (activity as NavigationHost).navigateTo(LoginFragment(), false)
        }
        binding.registerButtonC.setOnClickListener {
            emailSignIn()
        }
        return binding.root
    }

    private fun notEmpty():Boolean = binding.nameETC.text.toString().trim().isNotEmpty() &&
            binding.emailETC.text.toString().trim().isNotEmpty() &&
            binding.passwordETC.text.toString().trim().isNotEmpty() &&
            binding.confirmPasswordETC.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var flag = false
        if(notEmpty() && binding.passwordETC.text.toString().trim() == binding.confirmPasswordETC.text.toString().trim()) {
            flag = true
        } else if(!notEmpty()) {
            createAccountInputArray.forEach {
                if(it.text.toString().trim().isEmpty()) {
                    it.error = "${it.hint} is required"
                }
            }
        } else {
            Toast.makeText(requireActivity(),"Passwords aren't matching", Toast.LENGTH_SHORT).show()
        }
        return flag
    }
    private fun emailSignIn() {
        if (identicalPassword()) {
            userEmail = binding.emailETC.text.toString().trim()
            userName = binding.nameETC.text.toString().trim()
            userPassword = binding.passwordETC.text.toString().trim()
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            requireActivity(),
                            "Account was successfully created",
                            Toast.LENGTH_SHORT
                        ).show()
                        val userId = firebaseAuth.currentUser?.uid.toString()
                        val userInfo = User(userName, userEmail)
                        db.collection("Users").document(userId).set(userInfo)
                        (activity as NavigationHost).navigateTo(HomeFragment(), false)
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Account creation failed",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("Info", it.exception.toString())
                    }

                }

        }
    }
}