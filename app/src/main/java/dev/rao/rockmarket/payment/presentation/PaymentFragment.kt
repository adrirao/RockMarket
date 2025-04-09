package dev.rao.rockmarket.payment.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.rao.rockmarket.R
import dev.rao.rockmarket.databinding.FragmentPaymentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()
    private val args: PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
        observeValidationState()
    }

    private fun setupUI() {
        // Mostrar información del producto
        binding.productTitle.text = args.productTitle
        binding.productPrice.text = "$${args.productPrice}"

        // Configurar botón de pago
        binding.btnPay.setOnClickListener {
            val cardNumber = binding.editCardNumber.text.toString()
            val cardCvv = binding.editCvv.text.toString()
            val cardExpMonth = binding.editExpMonth.text.toString()
            val cardExpYear = binding.editExpYear.text.toString()

            if (viewModel.validateForm(cardNumber, cardCvv, cardExpMonth, cardExpYear)) {
                viewModel.processPayment(
                    cardNumber = cardNumber,
                    cvv = cardCvv,
                    expirationMonth = cardExpMonth.toInt(),
                    expirationYear = cardExpYear.toInt(),
                    amount = args.productPrice.toDouble()
                )
            }
        }
    }

    private fun observeValidationState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validationState.collectLatest { validationState ->
                    // Actualizar errores en la UI
                    binding.inputCardNumber.error = validationState.cardNumberError
                    binding.inputCvv.error = validationState.cvvError
                    binding.inputExpMonth.error = validationState.expirationMonthError
                    binding.inputExpYear.error = validationState.expirationYearError
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is PaymentState.Initial -> {
                            binding.progressBar.isVisible = false
                            binding.formContainer.isVisible = true
                        }

                        is PaymentState.Loading -> {
                            binding.progressBar.isVisible = true
                            binding.formContainer.isVisible = false
                        }

                        is PaymentState.Success -> {
                            binding.progressBar.isVisible = false

                            // Mostrar mensaje de éxito con Snackbar
                            Snackbar.make(
                                binding.root,
                                "¡Pago procesado correctamente!",
                                Snackbar.LENGTH_LONG
                            ).addCallback(object : Snackbar.Callback() {
                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    // Navegar al home después de que el Snackbar desaparezca
                                    findNavController().navigate(R.id.action_paymentFragment_to_homeFragment)
                                }
                            }).show()
                        }

                        is PaymentState.Error -> {
                            binding.progressBar.isVisible = false
                            binding.formContainer.isVisible = true

                            // Mostrar mensaje de error con Snackbar
                            Snackbar.make(
                                binding.root,
                                "Error: ${state.message}",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}