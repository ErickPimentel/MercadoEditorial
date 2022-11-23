package com.erickpimentel.mercadoeditorial.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.adapter.BookAdapter
import com.erickpimentel.mercadoeditorial.databinding.FragmentHomeBinding
import com.erickpimentel.mercadoeditorial.repository.ApiRepository
import com.erickpimentel.mercadoeditorial.response.BookListResponse
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by activityViewModels()

    @Inject
    lateinit var apiRepository: ApiRepository

    @Inject
    lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()

        binding.progressBar.visibility = View.VISIBLE

        getAvailableBooks()

        bookAdapter.setOnItemClickListener {
            bookViewModel.currentBook.value = it
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBookDetailsFragment())
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookAdapter
        }
    }

    private fun getAvailableBooks() {
        val callBookApi = apiRepository.getBooks(null,1, null, null)
        callBookApi.enqueue(object : Callback<BookListResponse> {
            override fun onResponse(call: Call<BookListResponse>, response: Response<BookListResponse>) {
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

}