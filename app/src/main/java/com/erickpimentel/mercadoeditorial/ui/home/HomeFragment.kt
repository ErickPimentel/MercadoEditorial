package com.erickpimentel.mercadoeditorial.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.erickpimentel.mercadoeditorial.adapter.BookAdapter
import com.erickpimentel.mercadoeditorial.adapter.LoadMoreBooksAdapter
import com.erickpimentel.mercadoeditorial.databinding.FragmentHomeBinding
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by activityViewModels()

    @Inject
    lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()

        bookAdapter.setOnItemClickListener {
            bookViewModel.addCurrentBook(it)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBookDetailsFragment())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleScope.launchWhenCreated {
                bookViewModel.bookList.collect{
                    bookAdapter.submitData(it)
                }
            }

            lifecycleScope.launchWhenCreated {
                bookAdapter.loadStateFlow.collect{
                    val state = it.refresh
                    progressBar.isVisible = state is LoadState.Loading
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)

            adapter = bookAdapter.withLoadStateFooter(
                LoadMoreBooksAdapter{
                    bookAdapter.retry()
                }
            )
        }
    }

}