package com.example.library.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.library.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private var db = Firebase.firestore
    private var firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        binding.registerButtonL.setOnClickListener{
            (activity as NavigationHost).navigateTo(RegisterFragment(), true)
        }
        binding.loginButtonL.setOnClickListener {
            signInUser()
        }
        return binding.root
    }

    private fun notEmpty(): Boolean = binding.emailETL.text.toString().trim().isNotEmpty() &&
            binding.passwordETL.text.toString().trim().isNotEmpty()
    private fun signInUser() {
        userEmail = binding.emailETL.text.toString()
        userPassword = binding.passwordETL.text.toString()
        if(notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener{ Task ->
                if(Task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid
                    val documentRef = userId?.let { it1 -> db.collection("Users").document(it1) }
                    documentRef?.get()?.addOnSuccessListener {
                        if(it.getLong("isUser") == 1L) {
                            (activity as NavigationHost).navigateTo(HomeFragment(), false)
                            Toast.makeText(requireContext(), "Sign In Successful", Toast.LENGTH_SHORT).show()
                        } else {
                            (activity as NavigationHost).navigateTo(AdminFragment(), false)
                            Toast.makeText(requireContext(), "Welcome Admin", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.i("Info", Task.exception.toString())
                    try {
                        throw Task.getException()!!
                    }catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
    /*
    override fun onStart() {
        super.onStart()
        val userId = firebaseAuth.currentUser?.uid
        val documentRef = userId?.let { it1 -> db.collection("Users").document(it1) }
        documentRef?.get()?.addOnSuccessListener {
            if (it.getLong("isUser") == 1L) {
                (activity as NavigationHost).navigateTo(HomeFragment(), false)
            } else {
                (activity as NavigationHost).navigateTo(AdminFragment(), false)
            }
        }
    }*/
}