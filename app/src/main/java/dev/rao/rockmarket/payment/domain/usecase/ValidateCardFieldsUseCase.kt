package dev.rao.rockmarket.payment.domain.usecase

import dev.rao.rockmarket.payment.domain.result.CardValidationResult
import javax.inject.Inject

class ValidateCardFieldsUseCase @Inject constructor() {

    operator fun invoke(
        cardNumber: String,
        cvv: String,
        expirationMonth: String,
        expirationYear: String
    ): CardValidationResult {
        return CardValidationResult(
            cardNumberError = validateCardNumber(cardNumber),
            cvvError = validateCvv(cvv),
            expirationMonthError = validateExpirationMonth(expirationMonth),
            expirationYearError = validateExpirationYear(expirationYear)
        )
    }

    private fun validateCardNumber(cardNumber: String): String? {
        return if (cardNumber.length != 16 || !cardNumber.all { it.isDigit() }) {
            "Número de tarjeta inválido"
        } else {
            null
        }
    }

    private fun validateCvv(cvv: String): String? {
        return if (cvv.length != 3 || !cvv.all { it.isDigit() }) {
            "CVV inválido"
        } else {
            null
        }
    }

    private fun validateExpirationMonth(expMonth: String): String? {
        return if (expMonth.isEmpty() || expMonth.toIntOrNull() == null ||
            expMonth.toInt() < 1 || expMonth.toInt() > 12
        ) {
            "Mes inválido"
        } else {
            null
        }
    }

    private fun validateExpirationYear(expYear: String): String? {
        return if (expYear.isEmpty() || expYear.toIntOrNull() == null ||
            expYear.toInt() < 2023
        ) {
            "Año inválido"
        } else {
            null
        }
    }
} 