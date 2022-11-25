package com.erickpimentel.mercadoeditorial.ui.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
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
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.R
import com.erickpimentel.mercadoeditorial.adapter.BookRecyclerViewAdapter
import com.erickpimentel.mercadoeditorial.databinding.FragmentSearchBinding
import com.erickpimentel.mercadoeditorial.repository.ApiRepository
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel
import com.erickpimentel.mercadoeditorial.viewmodel.FilterViewModel
import dagger.hilt.android.AndroidEntryPoint
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

    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupRecyclerView()

        val cursorAdapter = setCursorAdapter()

        setOnQueryTextListener(cursorAdapter, bookViewModel.suggestionsList)

        setOnSuggestionListener()

        bookRecyclerViewAdapter.setOnItemClickListener {
            bookViewModel.addSuggestion(binding.searchView.query.toString())
            bookViewModel.addCurrentBook(it)
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment())
        }

        binding.filterImageView.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToFilterFragment())
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

                lifecycleScope.launchWhenCreated {
                    val bookList = bookViewModel.getSearchResultStream(p0).cachedIn(lifecycleScope)
                    bookList.collect{
                        bookRecyclerViewAdapter.submitData(it)
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                bookViewModel.updateQuery(p0)
                addSuggestion(p0, suggestions, cursorAdapter)


                lifecycleScope.launchWhenCreated {
                    val bookList = bookViewModel.getSearchResultStream(p0).cachedIn(lifecycleScope)
                    bookList.collect{
                        bookRecyclerViewAdapter.submitData(it)
                    }
                }
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