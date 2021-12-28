package com.example.library.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library.R
import com.example.library.data.Book
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso

class FirestoreAdapter(options: FirestoreRecyclerOptions<Book>, private val listener: (Book) -> Unit):
    FirestoreRecyclerAdapter<Book, FirestoreAdapter.ViewHolder>(options) {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.testimage)
        val title: TextView = itemView.findViewById(R.id.testtitle)
        val author: TextView = itemView.findViewById(R.id.testauthor_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Book) {
        holder.title.text = model.title
        holder.author.text = model.author
        Picasso.get().load(model.imageLink).placeholder(R.drawable.book).fit().into(holder.imageView)
        holder.itemView.setOnClickListener{ listener(model) }
    }
}
