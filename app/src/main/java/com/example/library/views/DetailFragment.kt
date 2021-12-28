package com.example.library.views

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.example.library.R
import com.example.library.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.blurry.Blurry
import jp.wasabeef.picasso.transformations.BlurTransformation




class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var docLink: String
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") {
            requestKey, bundle ->
            title = bundle.getString("bundleKey1").toString()
            val author = bundle.getString("bundleKey2")
            val imageLink = bundle.getString("bundleKey3")
            docLink = bundle.getString("bundleKey4").toString()
            val pages = bundle.getString("bundleKey5")
            val chapters = bundle.getString("bundleKey6")
            val description = bundle.getString("bundleKey7")
            val tags = bundle.getString("bundleKey8")
            setData(title, author, imageLink, pages, chapters,description, tags)
        }
    }

    private fun setData(title: String?, author: String?, link: String?, pages: String?, chapters: String?
    , description: String?, tags: String?) {
        Picasso.get().load(link).placeholder(R.drawable.book).fit().into(binding.imageDetailFront)
        binding.titleDetail.text = title
        binding.authorDetail.text = author
        binding.pageDetail.text = pages
        binding.chapterDetail.text = chapters
        binding.descriptionDetail.text = description
        binding.tag1Detail.text = tags
        // val transformation = SketchFilterTransformation(requireActivity())

        val transformation1 = BlurTransformation(requireActivity())
        Picasso.get().load(link).transform(transformation1).into(binding.imageDetailBack)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        binding.downloadButton.setOnClickListener {
            val url = Uri.parse(docLink)
            val intent = Intent(Intent.ACTION_VIEW, url)
            context?.startActivity(intent)
            Log.i("Info", "Download Button Clicked")
        }
        return binding.root
    }
}