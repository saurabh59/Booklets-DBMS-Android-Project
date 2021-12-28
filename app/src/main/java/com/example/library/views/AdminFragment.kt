package com.example.library.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.library.data.Book
import com.example.library.databinding.FragmentAdminBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class AdminFragment : Fragment() {
    private val db = Firebase.firestore
    private lateinit var binding: FragmentAdminBinding
    private lateinit var bookInputArray: Array<EditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(layoutInflater, container, false)
        fetchLastAdded()
        bookInputArray = arrayOf(binding.bookIdET,binding.bookNameET, binding.authorET,
            binding.editionET, binding.docLinkET,
            binding.coverLinkET, binding.categoryET, binding.descriptionET, binding.chaptersET, binding.pagesIdET)
        binding.addButton.setOnClickListener {
            saveToFirebase()
        }
        binding.goToHome.setOnClickListener {
            (activity as NavigationHost).navigateTo(HomeFragment(), true)
        }
        binding.checkRequestButton.setOnClickListener {
            (activity as NavigationHost).navigateTo(RequestedFragment(), true)
        }
        return binding.root
    }

    private fun saveToFirebase() {
        if(notEmpty()) {
            val newBook = Book(binding.bookIdET.text.toString().toInt()
                ,binding.bookNameET.text.toString(), binding.authorET.text.toString(),
                binding.editionET.text.toString(), binding.docLinkET.text.toString(),
                binding.coverLinkET.text.toString(), binding.pagesIdET.text.toString(), binding.chaptersET.text.toString(),
                binding.categoryET.text.toString().uppercase(), binding.descriptionET.text.toString())
            db.collection("Books").document(binding.bookIdET.text.toString()).set(newBook).
            addOnSuccessListener {
                Toast.makeText(requireContext(), "Book Successfully Added",
                    Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Book Wasn't Added For Some Reason",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            bookInputArray.forEach {
                if(it.text.toString().isEmpty()) {
                    it.error = "${it.hint} is Empty"
                }
            }
        }

    }

    private fun notEmpty() = binding.bookIdET.text.toString().isNotEmpty()
            && binding.bookNameET.text.toString().isNotEmpty()
            && binding.authorET.text.toString().isNotEmpty()
            && binding.editionET.text.toString().isNotEmpty()
            && binding.docLinkET.text.toString().isNotEmpty()
            && binding.coverLinkET.text.toString().isNotEmpty()

    private fun fetchLastAdded() {
        val query = db.collection("Books").orderBy("bookId",
            Query.Direction.DESCENDING).limit(1)
        query.get().addOnSuccessListener {
            if(it.isEmpty) {
                Log.i("Info", "It is empty indeed")
            } else {
                for(doc in it) {
                    val lastBook = doc.toObject(Book::class.java)
                    binding.bookIdTV.text = doc.id
                    binding.bootTitleTV.text = lastBook.title
                    binding.authorTV.text = lastBook.author
                    binding.editionTV.text = lastBook.edition
                }
            }
        }.addOnFailureListener {
            Log.i("Info", "Failed For Some Reason")
        }
    }
}