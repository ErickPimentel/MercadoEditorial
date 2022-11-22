package com.erickpimentel.mercadoeditorial.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.adapter.BookAdapter
import com.erickpimentel.mercadoeditorial.api.ApiClient
import com.erickpimentel.mercadoeditorial.api.ApiService
import com.erickpimentel.mercadoeditorial.databinding.FragmentHomeBinding
import com.erickpimentel.mercadoeditorial.response.BookListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bookAdapter by lazy { BookAdapter() }
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            progressBar.visibility = View.VISIBLE

            val callBookApi = api.getBooksByStatusCode(1)
            callBookApi.enqueue(object : Callback<BookListResponse> {
                override fun onResponse(
                    call: Call<BookListResponse>,
                    response: Response<BookListResponse>
                ) {
                    progressBar.visibility = View.GONE

                    when(response.code()){
                        in 200..299 -> {
                            response.body().let { body ->
                                body?.books.let { data ->
                                    if (data!!.isNotEmpty()){
                                        bookAdapter.differ.submitList(data)
                                        recyclerView.apply {
                                            layoutManager = LinearLayoutManager(context)
                                            adapter=bookAdapter
                                        }
                                    }
                                }
                            }
                        }
                        in 300..399 -> {
                            Log.e("HomeFragment", "Redirection messages: ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.e("HomeFragment", "Client error messages: ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.e("HomeFragment", "Server error messages: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<BookListResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("HomeFragment", "onFailure: ${t.message}")
                }

            })
        }

        return binding.root
    }

}