package dev.rao.rockmarket.payment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rao.rockmarket.payment.domain.result.CardValidationResult
import dev.rao.rockmarket.payment.domain.usecase.ProcessPaymentUseCase
import dev.rao.rockmarket.payment.domain.usecase.ValidateCardFieldsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val validateCardFieldsUseCase: ValidateCardFieldsUseCase,
    private val processPaymentUseCase: ProcessPaymentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PaymentState>(PaymentState.Initial)
    val state: StateFlow<PaymentState> = _state.asStateFlow()

    private val _validationState = MutableStateFlow<CardValidationResult>(CardValidationResult())
    val validationState: StateFlow<CardValidationResult> = _validationState.asStateFlow()

    fun validateForm(
        cardNumber: String,
        cvv: String,
        expirationMonth: String,
        expirationYear: String
    ): Boolean {
        val validationResult = validateCardFieldsUseCase(
            cardNumber,
            cvv,
            expirationMonth,
            expirationYear
        )

        _validationState.value = validationResult
        return validationResult.isValid()
    }

    fun processPayment(
        cardNumber: String,
        cvv: String,
        expirationMonth: Int,
        expirationYear: Int,
        amount: Double
    ) {
        viewModelScope.launch {
            _state.value = PaymentState.Loading

            processPaymentUseCase(
                cardNumber = cardNumber,
                cvv = cvv,
                expirationMonth = expirationMonth,
                expirationYear = expirationYear,
                amount = amount
            ).onSuccess {
                _state.value = PaymentState.Success
            }.onFailure { error ->
                _state.value = PaymentState.Error(
                    error.message ?: "Error desconocido al procesar el pago"
                )
            }
        }
    }
}

