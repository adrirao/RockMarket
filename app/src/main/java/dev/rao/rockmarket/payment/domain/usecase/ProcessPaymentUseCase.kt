package dev.rao.rockmarket.payment.domain.usecase

import kotlinx.coroutines.delay
import javax.inject.Inject

class ProcessPaymentUseCase @Inject constructor(
    private val validatePaymentCardUseCase: ValidatePaymentCardUseCase
) {
    suspend operator fun invoke(
        cardNumber: String,
        cvv: String,
        expirationMonth: Int,
        expirationYear: Int,
        amount: Double
    ): Result<Unit> {
        // Simular procesamiento de pago (retardo de 1.5 segundos)
        delay(1500)

        return if (validatePaymentCardUseCase(cardNumber, cvv, expirationMonth, expirationYear)) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Tarjeta de crédito inválida. Por favor, intente nuevamente."))
        }
    }
} 