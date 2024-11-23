package com.example.kursovaya.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.kursnewsapp.R
import com.example.kursnewsapp.databinding.FragmentArticleBinding
import com.example.kursnewsapp.ui.fragments.NewsActivity
import com.example.kursnewsapp.ui.fragments.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var newsViewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        binding.fab.setOnClickListener {
            newsViewModel.toggleFavorite(article)
        }

//        binding.fab.setOnClickListener {
//            newsViewModel.addToFavorites(article)
//            Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_SHORT).show()
//        }

//               binding.fab.setOnClickListener {
//            newsViewModel.deleteOrAddToFavorites(article)        }
//
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
