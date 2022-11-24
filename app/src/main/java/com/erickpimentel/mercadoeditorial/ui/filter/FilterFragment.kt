package com.erickpimentel.mercadoeditorial.ui.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.erickpimentel.mercadoeditorial.databinding.FragmentFilterBinding
import com.erickpimentel.mercadoeditorial.utils.Status
import com.erickpimentel.mercadoeditorial.utils.Type
import com.erickpimentel.mercadoeditorial.viewmodel.FilterViewModel

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        binding.apply {

            clearButton.setOnClickListener {
                typeRadioGroup.clearCheck()
                statusRadioGroup.clearCheck()
            }

            applyButton.setOnClickListener {
                validate()
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    private fun FragmentFilterBinding.validate() {
        when (typeRadioGroup.checkedRadioButtonId) {
            bookRadioButton.id -> filterViewModel.updateType(Type.BOOK)
            ebookRadioButton.id -> filterViewModel.updateType(Type.EBOOK)
            else -> filterViewModel.updateType(null)
        }

        when (statusRadioGroup.checkedRadioButtonId) {
            availableRadioButton.id -> filterViewModel.updateStatus(Status.AVAILABLE)
            unavailableRadioButton.id -> filterViewModel.updateStatus(Status.UNAVAILABLE)
            preReleaseRadioButton.id -> filterViewModel.updateStatus(Status.PRE_RELEASE)
            outOfCatalogRadioButton.id -> filterViewModel.updateStatus(Status.OUT_OF_CATALOG)
            else -> filterViewModel.updateStatus(null)
        }
    }
}