package com.example.library.views

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.adapter.FirestoreAdapter
import com.example.library.data.Book
import com.example.library.databinding.FragmentHomeBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment :Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var options: FirestoreRecyclerOptions<Book>
    private lateinit var adapter: FirestoreAdapter
    private lateinit var query: Query
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private var name: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                query = firestore.collection("Books")
                filter(query)
                recyclerView.adapter = adapter
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        query = firestore.collection("Books")
                        filter(query)
                        recyclerView.adapter = adapter
                    }
                    1 -> {
                        query = firestore.collection("Books").whereEqualTo("tags",
                        "COLLEGE RECOMMENDED")
                        filter(query)
                        recyclerView.adapter = adapter
                    }
                    2 -> {
                        query = firestore.collection("Books").whereEqualTo("tags", "PROGRAMMING " +
                                "LANGUAGE")
                        filter(query)
                        recyclerView.adapter = adapter
                    }
                    3 -> {
                        query = firestore.collection("Books").whereEqualTo("tags", "CONCEPTS")
                        filter(query)
                        recyclerView.adapter = adapter
                    }
                }
            }
        }
        val documentRef = currentUser?.let { firestore.collection("Users").document(it) }
        documentRef?.get()?.addOnSuccessListener {
            name = it.getString("name")
            binding.userNameTV.text = name.toString()
            Log.i("info", name.toString())
        }


        binding.searchButton.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                query = firestore.collection("Books").whereEqualTo("title", "$p0")
                filter(query)
                recyclerView.adapter = adapter
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean{
                return false
            }
        })
        binding.requestButton.setOnClickListener{
            (activity as NavigationHost).navigateTo(UserRequest(), true)
        }
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signOut.setOnClickListener{
            firebaseAuth.signOut()
            (activity as AppCompatActivity).supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            (activity as NavigationHost).navigateTo(LoginFragment(), false)

        }
        recyclerView = binding.recyclerView
        query= firestore.collection("Books")
        options = FirestoreRecyclerOptions.Builder<Book>().setQuery(query, Book::class.java).build()
        adapter = FirestoreAdapter(options) {
            val title = it.title
            val author = it.author
            val imageLink = it.imageLink
            val docLink = it.docLink
            val pages = it.pages
            val chapters = it.chapters
            val description = it.description
            val tags = it.tags

            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("requestKey", bundleOf("bundleKey1" to title, "bundleKey2" to author, "bundleKey3" to imageLink,
            "bundleKey4" to docLink, "bundleKey5" to pages, "bundleKey6" to chapters, "bundleKey7" to description
            , "bundleKey8" to tags))
            (activity as NavigationHost).navigateTo(DetailFragment(), true)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
    fun filter(newQuery: Query) {
        Log.i("myItems", "filtering by $newQuery")
        val filteredListOptions = FirestoreRecyclerOptions.Builder<Book>()
            .setQuery(newQuery,Book::class.java)
            .build()
        Log.i("Info", "Filter was called")
        adapter.updateOptions(filteredListOptions)
    }
}