package dev.rao.rockmarket.payment.presentation

sealed class PaymentState {
    object Initial : PaymentState()
    object Loading : PaymentState()
    object Success : PaymentState()
    data class Error(val message: String) : PaymentState()
} 