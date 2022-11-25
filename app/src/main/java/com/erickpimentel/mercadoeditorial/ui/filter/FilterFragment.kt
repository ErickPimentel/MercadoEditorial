package com.erickpimentel.mercadoeditorial.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.erickpimentel.mercadoeditorial.databinding.FragmentFilterBinding
import com.erickpimentel.mercadoeditorial.utils.Status
import com.erickpimentel.mercadoeditorial.utils.Type
import com.erickpimentel.mercadoeditorial.viewmodel.BookViewModel

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        binding.apply {

            restoreRadioButtons()

            clearButton.setOnClickListener {
                bookViewModel.clearAllRadioButtons()
                typeRadioGroup.clearCheck()
                statusRadioGroup.clearCheck()
            }

            applyButton.setOnClickListener {
                updateFilter()
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    private fun FragmentFilterBinding.restoreRadioButtons() {
        when (bookViewModel.type.value) {
            Type.BOOK -> typeRadioGroup.check(bookRadioButton.id)
            Type.EBOOK -> typeRadioGroup.check(ebookRadioButton.id)
            else -> {}
        }

        when (bookViewModel.status.value) {
            Status.AVAILABLE -> statusRadioGroup.check(availableRadioButton.id)
            Status.UNAVAILABLE -> statusRadioGroup.check(unavailableRadioButton.id)
            Status.PRE_RELEASE -> statusRadioGroup.check(preReleaseRadioButton.id)
            Status.OUT_OF_CATALOG -> statusRadioGroup.check(outOfCatalogRadioButton.id)
            else -> {}
        }
    }

    private fun FragmentFilterBinding.updateFilter() {
        when (typeRadioGroup.checkedRadioButtonId) {
            bookRadioButton.id -> bookViewModel.updateType(Type.BOOK)
            ebookRadioButton.id -> bookViewModel.updateType(Type.EBOOK)
            else -> bookViewModel.updateType(null)
        }

        when (statusRadioGroup.checkedRadioButtonId) {
            availableRadioButton.id -> bookViewModel.updateStatus(Status.AVAILABLE)
            unavailableRadioButton.id -> bookViewModel.updateStatus(Status.UNAVAILABLE)
            preReleaseRadioButton.id -> bookViewModel.updateStatus(Status.PRE_RELEASE)
            outOfCatalogRadioButton.id -> bookViewModel.updateStatus(Status.OUT_OF_CATALOG)
            else -> bookViewModel.updateStatus(null)
        }
    }
}