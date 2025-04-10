package dev.rao.rockmarket.payment.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateCardFieldsUseCaseTest {

    private lateinit var validateCardFieldsUseCase: ValidateCardFieldsUseCase

    @Before
    fun setUp() {
        validateCardFieldsUseCase = ValidateCardFieldsUseCase()
    }

    @Test
    fun `valid card fields return no errors`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = "12"
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertTrue(result.isValid())
        assertNull(result.cardNumberError)
        assertNull(result.cvvError)
        assertNull(result.expirationMonthError)
        assertNull(result.expirationYearError)
    }

    @Test
    fun `invalid card number returns error`() {
        val cardNumber = "411111111111111a"
        val cvv = "123"
        val expirationMonth = "12"
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNotNull(result.cardNumberError)
        assertNull(result.cvvError)
        assertNull(result.expirationMonthError)
        assertNull(result.expirationYearError)
    }

    @Test
    fun `card number with wrong length returns error`() {
        val cardNumber = "41111111111111"
        val cvv = "123"
        val expirationMonth = "12"
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNotNull(result.cardNumberError)
    }

    @Test
    fun `invalid cvv returns error`() {
        val cardNumber = "4111111111111111"
        val cvv = "12a"
        val expirationMonth = "12"
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNull(result.cardNumberError)
        assertNotNull(result.cvvError)
        assertNull(result.expirationMonthError)
        assertNull(result.expirationYearError)
    }

    @Test
    fun `cvv with wrong length returns error`() {
        val cardNumber = "4111111111111111"
        val cvv = "12"
        val expirationMonth = "12"
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNotNull(result.cvvError)
    }

    @Test
    fun `invalid expiration month returns error`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = "13" // Mes mayor a 12
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNull(result.cardNumberError)
        assertNull(result.cvvError)
        assertNotNull(result.expirationMonthError)
        assertNull(result.expirationYearError)
    }

    @Test
    fun `zero expiration month returns error`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = "0" // Mes menor a 1
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNotNull(result.expirationMonthError)
    }

    @Test
    fun `non-numeric expiration month returns error`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = "1a"
        val expirationYear = "2025"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNotNull(result.expirationMonthError)
    }

    @Test
    fun `expired year returns error`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = "12"
        val expirationYear = "2022"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNull(result.cardNumberError)
        assertNull(result.cvvError)
        assertNull(result.expirationMonthError)
        assertNotNull(result.expirationYearError)
    }

    @Test
    fun `non-numeric expiration year returns error`() {
        val cardNumber = "4111111111111111"
        val cvv = "123"
        val expirationMonth = "12"
        val expirationYear = "202a"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNotNull(result.expirationYearError)
    }

    @Test
    fun `multiple invalid fields return multiple errors`() {
        val cardNumber = "411111111111111a"
        val cvv = "12"
        val expirationMonth = "13"
        val expirationYear = "2022"

        val result = validateCardFieldsUseCase(
            cardNumber = cardNumber,
            cvv = cvv,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear
        )

        assertFalse(result.isValid())
        assertNotNull(result.cardNumberError)
        assertNotNull(result.cvvError)
        assertNotNull(result.expirationMonthError)
        assertNotNull(result.expirationYearError)
    }
} 