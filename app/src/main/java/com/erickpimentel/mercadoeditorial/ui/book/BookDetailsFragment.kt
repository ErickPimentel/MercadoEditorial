package com.erickpimentel.mercadoeditorial.ui.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import com.erickpimentel.mercadoeditorial.R
import com.erickpimentel.mercadoeditorial.databinding.ActivityMainBinding
import com.erickpimentel.mercadoeditorial.databinding.FragmentBookDetailsBinding
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailsFragment : Fragment() {

    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)

        binding.apply {
            bookViewModel.currentBook.value?.let { book ->
                bookTitle.text = book.titulo
                bookSynopsis.text = book.sinopse
                bookPrice.text = getString(R.string.price_symbol, book.preco.toFloat())

                val bookImage = book.imagens.imagem_primeira_capa.pequena
                bookImageView.load(bookImage){ setPlaceHolder() }
                bookBackgroundImageView.load(bookImage){ setPlaceHolder() }


            }
        }

        return binding.root
    }

    private fun ImageRequest.Builder.setPlaceHolder() {
        crossfade(true)
        placeholder(R.drawable.placeholder)
        scale(Scale.FILL)
    }


}