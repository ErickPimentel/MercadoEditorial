package com.erickpimentel.mercadoeditorial.ui.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingSource
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.R
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

        val cursorAdapter = setCursorAdapter()

        setOnQueryTextListener(cursorAdapter, bookViewModel.suggestionsList)

        setOnSuggestionListener()

        lifecycleScope.launchWhenCreated {
            getBooksByCurrentQuery()
        }

        bookRecyclerViewAdapter.setOnItemClickListener {
            bookViewModel.addSuggestion(binding.searchView.query.toString())
            bookViewModel.currentBook.value = it
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment())
        }

        return binding.root
    }

    private fun setCursorAdapter(): SimpleCursorAdapter {
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(context, R.layout.search_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        binding.searchView.suggestionsAdapter = cursorAdapter
        return cursorAdapter
    }

    private fun setOnSuggestionListener() {
        binding.searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(p0: Int): Boolean {
                return false
            }

            @SuppressLint("Range")
            override fun onSuggestionClick(p0: Int): Boolean {
                view?.hideKeyboard()
                val cursor = binding.searchView.suggestionsAdapter.getItem(p0) as Cursor
                val selection =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                binding.searchView.setQuery(selection, false)
                return true
            }

        })
    }

    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    private suspend fun getBooksByCurrentQuery() {
        bookViewModel.currentQuery.observe(requireActivity()) { query ->
            if (!query.isNullOrEmpty() && query.length >= 2){
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

    private fun setOnQueryTextListener(cursorAdapter: SimpleCursorAdapter, suggestions: List<String>) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                view?.hideKeyboard()
                bookViewModel.updateQuery(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                bookViewModel.updateQuery(p0)
                addSuggestion(p0, suggestions, cursorAdapter)
                return false
            }
        })
    }

    private fun addSuggestion(newText: String?, suggestions: List<String>, cursorAdapter: SimpleCursorAdapter) {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
        newText?.let {
            suggestions.forEachIndexed { index, suggestion ->
                if (suggestion.contains(newText, true) && suggestion.isNotEmpty()) {
                    cursor.addRow(arrayOf(index, suggestion))
                }
            }
        }
        cursorAdapter.changeCursor(cursor)
    }

    fun View.hideKeyboard(){
        val imn = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(windowToken, 0)
    }
}