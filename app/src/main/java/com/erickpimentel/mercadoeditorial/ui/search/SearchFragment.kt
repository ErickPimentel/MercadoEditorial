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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.adapter.BookAdapter
import com.erickpimentel.mercadoeditorial.adapter.listener.Listener
import com.erickpimentel.mercadoeditorial.api.ApiClient
import com.erickpimentel.mercadoeditorial.api.ApiService
import com.erickpimentel.mercadoeditorial.databinding.FragmentSearchBinding
import com.erickpimentel.mercadoeditorial.response.Book
import com.erickpimentel.mercadoeditorial.response.BookListResponse
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), Listener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val bookAdapter by lazy { BookAdapter(this) }
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupRecyclerView()

        setOnQueryTextListener()

        getBooksByCurrentQuery()

        return binding.root
    }

    private fun getBooksByCurrentQuery() {
        bookViewModel.currentQuery.observe(requireActivity()) {
            if (!it.isNullOrEmpty()) getBooks(it)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookAdapter
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

    fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    private fun getBooks(query: String) {

        var title: String? = null
        var isbn: String? = null

        if (isNumeric(query)) isbn = query
        else title = query

        val callBookApi = api.getBooks(null, title, isbn)
        callBookApi.enqueue(object : Callback<BookListResponse> {
            override fun onResponse(
                call: Call<BookListResponse>,
                response: Response<BookListResponse>
            ) {
                when (response.code()) {
                    in 200..299 -> {
                        response.body().let { body ->
                            body?.books.let { data ->
                                if (!data.isNullOrEmpty()) {
                                    bookAdapter.differ.submitList(data)
                                }
                            }
                        }
                    }
                    in 300..599 -> {
                        Log.e("HomeFragment", "Could not get book(s): ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<BookListResponse>, t: Throwable) {
                Log.e("HomeFragment", "onFailure: ${t.message}")
            }

        })
    }

    override fun onItemClickListener(book: Book) {
        bookViewModel.addCurrentBook(book)
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment())
    }
}