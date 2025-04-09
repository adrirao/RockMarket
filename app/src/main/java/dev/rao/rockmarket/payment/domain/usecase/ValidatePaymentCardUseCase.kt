package dev.rao.rockmarket.payment.domain.usecase

import javax.inject.Inject

class ValidatePaymentCardUseCase @Inject constructor() {

    // Tarjeta válida para pruebas
    private val validCardNumber = "4111111111111111"
    private val validCvv = "123"
    private val validExpirationMonth = 12
    private val validExpirationYear = 2025

    operator fun invoke(
        cardNumber: String,
        cvv: String,
        expirationMonth: Int,
        expirationYear: Int
    ): Boolean {
        return cardNumber == validCardNumber &&
                cvv == validCvv &&
                expirationMonth == validExpirationMonth &&
                expirationYear == validExpirationYear
    }
} 