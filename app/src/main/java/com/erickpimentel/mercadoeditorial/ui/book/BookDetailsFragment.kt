package com.erickpimentel.mercadoeditorial.ui.book

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import coil.size.Scale
import com.erickpimentel.mercadoeditorial.MainActivity
import com.erickpimentel.mercadoeditorial.R
import com.erickpimentel.mercadoeditorial.databinding.ActivityMainBinding
import com.erickpimentel.mercadoeditorial.databinding.FragmentBookDetailsBinding
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel

class BookDetailsFragment : Fragment() {

    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)

        binding.apply {
            bookViewModel.currentBook.value?.let {
                bookTitle.text = it.titulo
                bookImageView.load(it.imagens.imagem_primeira_capa.pequena){
                    crossfade(true)
                    placeholder(R.drawable.placeholder)
                    scale(Scale.FILL)
                }
            }
        }

        return binding.root
    }


}