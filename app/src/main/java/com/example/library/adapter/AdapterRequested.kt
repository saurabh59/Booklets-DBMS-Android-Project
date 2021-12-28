package com.example.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library.R
import com.example.library.data.Request
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AdapterRequested(options: FirestoreRecyclerOptions<Request>, private val listener: (Request) -> Boolean):
    FirestoreRecyclerAdapter<Request, AdapterRequested.ViewHolder2>(options) {
    class ViewHolder2(itemView: View): RecyclerView.ViewHolder(itemView){
        val bookName: TextView = itemView.findViewById(R.id.requestedBookName)
        val bookAuthor: TextView = itemView.findViewById(R.id.requestedBookAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item2, parent, false)
        return ViewHolder2(view)
    }

    override fun onBindViewHolder(holder: ViewHolder2, position: Int, model: Request) {
        holder.bookAuthor.text = model.authorName
        holder.bookName.text = model.bookName
        holder.itemView.setOnLongClickListener{ listener(model) }
    }
}
