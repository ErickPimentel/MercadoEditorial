package com.erickpimentel.mercadoeditorial.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.LoadStates
import androidx.recyclerview.widget.RecyclerView
import com.erickpimentel.mercadoeditorial.databinding.LoadMoreBooksBinding

class LoadMoreBooksAdapter(private val retry: () -> Unit): LoadStateAdapter<LoadMoreBooksAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(LoadMoreBooksBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)

    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(private val binding: LoadMoreBooksBinding, retry: () -> Unit): RecyclerView.ViewHolder(binding.root){

        init {
            binding.buttonRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(states: LoadState){
            binding.apply {
                progressBarLoadMoreBooks.isVisible = states is LoadState.Loading
                textViewError.isVisible = states is LoadState.Error
                buttonRetry.isVisible = states is LoadState.Error
            }
        }
    }
}