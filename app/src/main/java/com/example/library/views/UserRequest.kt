package com.example.library.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.library.data.Request
import com.example.library.databinding.FragmentUserRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRequest : Fragment() {
    private lateinit var binding: FragmentUserRequestBinding
    private val db = Firebase.firestore
    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private var name: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRequestBinding.inflate(layoutInflater, container, false)
        binding.submitButton.setOnClickListener {
            submitRequest()
        }
        return binding.root
    }

    private fun submitRequest() {
        val documentRef = currentUser?.let { db.collection("Users").document(it) }
        documentRef?.get()?.addOnSuccessListener {
            name = it.getString("name")
        }
        Log.i("Info", name.toString())
        val requestedBook= Request(binding.nameETR.text.toString(), binding.authorETR.text.toString())
        name?.let { db.collection("Request").document(it).set(requestedBook).addOnSuccessListener {
            Toast.makeText(requireContext(), "Request was submitted successfully", Toast.LENGTH_SHORT).show()
        } }

    }
}