package com.erickpimentel.mercadoeditorial.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.erickpimentel.mercadoeditorial.R
import com.erickpimentel.mercadoeditorial.adapter.listener.Listener
import com.erickpimentel.mercadoeditorial.databinding.BookViewBinding
import com.erickpimentel.mercadoeditorial.response.Book

class BookAdapter(
    private val listener: Listener
): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(BookViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class BookViewHolder(private val binding: BookViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book){
            binding.apply {
                bookTitle.text = book.titulo
                bookImageView.load(book.imagens.imagem_primeira_capa.pequena){
                    crossfade(true)
                    placeholder(R.drawable.placeholder)
                    scale(Scale.FILL)
                }

                constraintLayout.setOnClickListener {
                    listener.onItemClickListener(book)
                }
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Book>(){
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.isbn == newItem.isbn
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}