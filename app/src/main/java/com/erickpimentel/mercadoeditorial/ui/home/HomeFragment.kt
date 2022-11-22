package com.erickpimentel.mercadoeditorial.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.R
import com.erickpimentel.mercadoeditorial.adapter.BookAdapter
import com.erickpimentel.mercadoeditorial.adapter.listener.Listener
import com.erickpimentel.mercadoeditorial.api.ApiClient
import com.erickpimentel.mercadoeditorial.api.ApiService
import com.erickpimentel.mercadoeditorial.databinding.FragmentHomeBinding
import com.erickpimentel.mercadoeditorial.response.Book
import com.erickpimentel.mercadoeditorial.response.BookListResponse
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), Listener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by activityViewModels()

    private val bookAdapter by lazy { BookAdapter(this) }
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()

        binding.progressBar.visibility = View.VISIBLE

        getAvailableBooks()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookAdapter
        }
    }

    private fun getAvailableBooks() {
        val callBookApi = api.getBooks(1, null, null)
        callBookApi.enqueue(object : Callback<BookListResponse> {
            override fun onResponse(
                call: Call<BookListResponse>,
                response: Response<BookListResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                when (response.code()) {
                    in 200..299 -> {
                        response.body().let { body ->
                            body?.books.let { data ->
                                if (!data.isNullOrEmpty()) {
                                    bookAdapter.differ.submitList(data)
                                } else {
                                    binding.noResults.visibility = View.VISIBLE
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
                binding.progressBar.visibility = View.GONE
                Log.e("HomeFragment", "onFailure: ${t.message}")
            }

        })
    }

    override fun onItemClickListener(book: Book) {
        bookViewModel.addCurrentBook(book)
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBookDetailsFragment())
    }

}