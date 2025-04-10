package dev.rao.rockmarket.payment.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ProcessPaymentUseCaseTest {

    private lateinit var processPaymentUseCase: ProcessPaymentUseCase
    private lateinit var validatePaymentCardUseCase: ValidatePaymentCardUseCase

    @Before
    fun setUp() {
        validatePaymentCardUseCase = mock(ValidatePaymentCardUseCase::class.java)
        processPaymentUseCase = ProcessPaymentUseCase(validatePaymentCardUseCase)
    }

    @Test
    fun `valid card returns success`() = runBlocking {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = 12
        val expirationYear = 2025
        val amount = 100.0

        `when`(
            validatePaymentCardUseCase(
                cardNumber,
                cvv,
                expirationMonth,
                expirationYear
            )
        ).thenReturn(true)

        val result = processPaymentUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear,
            amount = amount
        )

        assertTrue(result.isSuccess)
    }

    @Test
    fun `invalid card returns failure`() = runBlocking {
        val cardNumber = "4111111111111112" // Número incorrecto
        val cvv = "123"
        val expirationMonth = 12
        val expirationYear = 2025
        val amount = 100.0

        `when`(
            validatePaymentCardUseCase(
                cardNumber,
                cvv,
                expirationMonth,
                expirationYear
            )
        ).thenReturn(false)

        val result = processPaymentUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear,
            amount = amount
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun `payment processes with any valid amount`() = runBlocking {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = 12
        val expirationYear = 2025
        val amount = 0.01 // Monto mínimo

        `when`(
            validatePaymentCardUseCase(
                cardNumber,
                cvv,
                expirationMonth,
                expirationYear
            )
        ).thenReturn(true)

        val result = processPaymentUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear,
            amount = amount
        )

        assertTrue(result.isSuccess)
    }

    @Test
    fun `payment processes with large amount`() = runBlocking {
        // Given
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = 12
        val expirationYear = 2025
        val amount = 9999999.99 // Monto grande

        `when`(
            validatePaymentCardUseCase(
                cardNumber,
                cvv,
                expirationMonth,
                expirationYear
            )
        ).thenReturn(true)

        val result = processPaymentUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear,
            amount = amount
        )

        assertTrue(result.isSuccess)
    }
} 