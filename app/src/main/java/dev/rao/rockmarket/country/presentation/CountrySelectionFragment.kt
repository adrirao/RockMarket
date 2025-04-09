package dev.rao.rockmarket.country.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.rao.rockmarket.R
import dev.rao.rockmarket.databinding.FragmentCountrySelectionBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountrySelectionFragment : Fragment() {
    private var _binding: FragmentCountrySelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountrySelectionViewModel by viewModels()
    private val countryAdapter = CountryAdapter { country ->
        viewModel.selectCountry(country)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountrySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        binding.countriesRecyclerView.adapter = countryAdapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is CountrySelectionState.Initial -> {
                        binding.progressBar.isVisible = false
                    }

                    is CountrySelectionState.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is CountrySelectionState.Success -> {
                        binding.progressBar.isVisible = false
                        countryAdapter.submitList(state.countries)
                        state.selectedCountry?.let { country ->
                            countryAdapter.setSelectedCountry(country)
                            navigateToHome()
                        }
                    }

                    is CountrySelectionState.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_country_selection_to_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}