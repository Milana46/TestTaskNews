package com.example.kursovaya.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kursnewsapp.R
import com.example.kursnewsapp.adapter.NewsAdapter
import com.example.kursnewsapp.databinding.FragmentFavouritesBinding
import com.example.kursnewsapp.ui.fragments.NewsActivity
import com.example.kursnewsapp.ui.fragments.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentFavouritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupFavoritesRecycler()


//        newsAdapter.setOnItemClickListener { article ->
//            newsViewModel.deleteOrAddToFavorites(article)
//
//            Snackbar.make(view, "Favorite toggled", Snackbar.LENGTH_SHORT).apply {
//                setAction("Undo") {
//
//                    newsViewModel.deleteOrAddToFavorites(article)
//                }
//                show()
//            }
//        }


        newsViewModel.getAll().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }


    private fun setupFavoritesRecycler() {
        newsAdapter = NewsAdapter(requireContext())
        binding.rvFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
