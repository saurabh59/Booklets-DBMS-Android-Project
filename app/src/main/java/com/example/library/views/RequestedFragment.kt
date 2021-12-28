package com.example.library.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.adapter.AdapterRequested
import com.example.library.data.Request
import com.example.library.databinding.FragmentRequestedBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class RequestedFragment : Fragment() {
    private lateinit var binding: FragmentRequestedBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var query: Query
    private lateinit var adapter: AdapterRequested
    private lateinit var options: FirestoreRecyclerOptions<Request>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestedBinding.inflate(layoutInflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        recyclerView = binding.recyclerView2
        query = firestore.collection("Request")
        options = FirestoreRecyclerOptions.Builder<Request>().setQuery(query, Request::class.java).build()
        adapter = AdapterRequested(options){
            val docRef = firestore.collection("Request").whereEqualTo("bookName", it.bookName)
            docRef.get().addOnSuccessListener { QuerySnapshot ->
                QuerySnapshot.forEach { doc ->
                    doc.reference.delete()
                }
            }
            query = firestore.collection("Request")
            recyclerView.adapter = adapter
            true
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
    private fun filter(newQuery: Query) {
        Log.i("myItems", "filtering by $newQuery")
        val filteredListOptions = FirestoreRecyclerOptions.Builder<Request>()
            .setQuery(newQuery, Request::class.java)
            .build()
        Log.i("Info", "Filter was called")
        adapter.updateOptions(filteredListOptions)
    }
}