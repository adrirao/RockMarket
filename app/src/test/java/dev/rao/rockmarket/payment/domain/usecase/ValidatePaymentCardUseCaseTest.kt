package dev.rao.rockmarket.payment.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatePaymentCardUseCaseTest {

    private lateinit var validatePaymentCardUseCase: ValidatePaymentCardUseCase

    @Before
    fun setUp() {
        validatePaymentCardUseCase = ValidatePaymentCardUseCase()
    }

    @Test
    fun `valid card information returns true`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = 12
        val expirationYear = 2025

        val result = validatePaymentCardUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertTrue(result)
    }

    @Test
    fun `invalid card number returns false`() {
        val cardNumber = "4111111111111112" // Número incorrecto
        val cvv = "123"
        val expirationMonth = 12
        val expirationYear = 2025

        val result = validatePaymentCardUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result)
    }

    @Test
    fun `invalid cvv returns false`() {
        val cardNumber = "4111111111111111"
        val cvv = "124" // CVV incorrecto
        val expirationMonth = 12
        val expirationYear = 2025

        val result = validatePaymentCardUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result)
    }

    @Test
    fun `invalid expiration month returns false`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = 11 // Mes incorrecto
        val expirationYear = 2025

        val result = validatePaymentCardUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result)
    }

    @Test
    fun `invalid expiration year returns false`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = 12
        val expirationYear = 2024 // Año incorrecto

        val result = validatePaymentCardUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result)
    }
} 