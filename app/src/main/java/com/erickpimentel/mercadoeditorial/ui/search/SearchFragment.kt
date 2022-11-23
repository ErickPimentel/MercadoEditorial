package com.erickpimentel.mercadoeditorial.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingSource
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.adapter.BookRecyclerViewAdapter
import com.erickpimentel.mercadoeditorial.databinding.FragmentSearchBinding
import com.erickpimentel.mercadoeditorial.repository.ApiRepository
import com.erickpimentel.mercadoeditorial.response.Book
import com.erickpimentel.mercadoeditorial.response.BookListResponse
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(){

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var apiRepository: ApiRepository

    @Inject
    lateinit var bookRecyclerViewAdapter: BookRecyclerViewAdapter

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupRecyclerView()

        setOnQueryTextListener()

        lifecycleScope.launchWhenCreated {
            getBooksByCurrentQuery()
        }

        bookRecyclerViewAdapter.setOnItemClickListener {
            bookViewModel.currentBook.value = it
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment())
        }

        return binding.root
    }

    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    private suspend fun getBooksByCurrentQuery() {
        bookViewModel.currentQuery.observe(requireActivity()) { query ->
            if (!query.isNullOrEmpty() && query.length >= 3){
                var title: String? = null
                var isbn: String? = null

                if (isNumeric(query)) isbn = query
                else title = query

                lifecycleScope.launchWhenCreated {
                    try {
                        val response = apiRepository.getBooks(1, 1, title, isbn)
                        val data = response.body()!!.books
                        val responseData = mutableListOf<Book>()
                        responseData.addAll(data)
                        bookRecyclerViewAdapter.differ.submitList(data)

                    } catch (e: Exception){
                        Log.e("SearchFragment", "getBooksByCurrentQuery: $e", )
                    }
                }
            }
            else{
                bookRecyclerViewAdapter.differ.submitList(listOf())
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookRecyclerViewAdapter
        }
    }

    private fun setOnQueryTextListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                view?.hideKeyboard()
                bookViewModel.updateQuery(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                bookViewModel.updateQuery(p0)
                return false
            }

        })
    }

    fun View.hideKeyboard(){
        val imn = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(windowToken, 0)
    }
}