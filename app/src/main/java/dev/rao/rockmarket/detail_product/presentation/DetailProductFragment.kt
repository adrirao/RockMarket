package dev.rao.rockmarket.detail_product.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.rao.rockmarket.R
import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.databinding.FragmentDetailProductBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailProductViewModel by viewModels()
    private val args: DetailProductFragmentArgs by navArgs()

    private var isFavorite = false
    private var currentProduct: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFavoriteButton()
        observeViewModel()
        setupBackNavigation()

        // Cargar el detalle del producto
        viewModel.loadProductDetail(args.productId)
    }

    private fun setupBackNavigation() {
        // Sobreescribir el comportamiento del botón de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToHome()
                }

            })
    }

    private fun navigateToHome() {
        val action = DetailProductFragmentDirections.actionDetailProductFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun setupFavoriteButton() {
        binding.fabFavorite.setOnClickListener {
            currentProduct?.let { product ->
                // Navegar a la pantalla de pagos
                val action =
                    DetailProductFragmentDirections.actionDetailProductFragmentToPaymentFragment(
                        productTitle = product.title,
                        productPrice = product.price.toFloat()
                    )
                findNavController().navigate(action)
            } ?: run {
                Snackbar.make(
                    binding.root,
                    "Producto no disponible para pago",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        updateFavoriteIcon()
    }

    private fun updateFavoriteIcon() {
        val iconResource = R.drawable.ic_credit
        binding.fabFavorite.setImageResource(iconResource)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is ProductDetailState.Loading -> showLoading()
                        is ProductDetailState.Success -> {
                            currentProduct = state.product
                            showProductDetail(state)
                        }

                        is ProductDetailState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.fabFavorite.isVisible = false
        binding.cardProduct.isVisible = false
        binding.cardDescription.isVisible = false
        binding.cardRating.isVisible = false
    }

    private fun showProductDetail(state: ProductDetailState.Success) {
        binding.fabFavorite.isVisible = true
        binding.fabFavorite.isVisible = true
        binding.cardProduct.isVisible = true
        binding.cardDescription.isVisible = true
        binding.cardRating.isVisible = true
        binding.progressBar.isVisible = false

        val product = state.product

        // Cargar la imagen del producto
        Glide.with(requireContext())
            .load(product.imageUrl)
            .centerCrop()
            .into(binding.productImage)

        // Establecer los datos del producto
        binding.productTitle.text = product.title
        binding.productPrice.text = "$${product.price}"
        binding.productDescription.text = product.description

        // Mostrar la categoría si está disponible
        if (product.category != null) {
            binding.productCategory.isVisible = true
            binding.productCategory.text = product.category
        } else {
            binding.productCategory.isVisible = false
        }

        // Mostrar la calificación si está disponible
        if (product.rating != null) {
            binding.ratingContainer.isVisible = true
            binding.ratingBar.rating = product.rating.rate.toFloat()
            binding.ratingCount.text = "(${product.rating.count} opiniones)"
        } else {
            binding.ratingContainer.isVisible = false
        }
    }

    private fun showError(message: String) {
        binding.progressBar.isVisible = false
        binding.fabFavorite.isVisible = false

        Snackbar.make(
            binding.root,
            "Error: $message",
            Snackbar.LENGTH_LONG
        ).addCallback(object : Snackbar.Callback() {
            override fun onDismissed(
                transientBottomBar: Snackbar?,
                event: Int
            ) {
                navigateToHome()
            }
        }).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}