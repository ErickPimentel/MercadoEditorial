package com.erickpimentel.mercadoeditorial.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.erickpimentel.mercadoeditorial.R
import com.erickpimentel.mercadoeditorial.databinding.BookViewBinding
import com.erickpimentel.mercadoeditorial.response.Book
import com.erickpimentel.mercadoeditorial.utils.ParentalRating
import javax.inject.Inject

class BookPagingDataAdapter @Inject constructor(): PagingDataAdapter<Book, BookPagingDataAdapter.BookViewHolder>(differCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(BookViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(holder, getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    inner class BookViewHolder(private val binding: BookViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(holder: BookViewHolder, book: Book){

            binding.apply {
                bookTitle.text = book.titulo
                bookIsbn.text = holder.itemView.context.resources.getString(R.string.isbn, book.isbn)
                bookType.text = book.formato
                parentalRatingImageView.setImageResource(ParentalRating.fromInt(book.classificacao_indicativa).result())
                bookPrice.text = holder.itemView.context.resources.getString(R.string.price_symbol, book.preco.toFloat())
                bookImageView.load(book.imagens.imagem_primeira_capa.pequena){
                    crossfade(true)
                    placeholder(R.drawable.placeholder)
                    scale(Scale.FILL)
                }

                constraintLayout.setOnClickListener {
                    onItemClickListener?.let { it(book) }
                }
            }
        }
    }

    private var onItemClickListener: ((Book) -> Unit)? = null
    fun setOnItemClickListener(listener: (Book) -> Unit){
        onItemClickListener = listener
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Book>(){
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.isbn == newItem.isbn
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }

}