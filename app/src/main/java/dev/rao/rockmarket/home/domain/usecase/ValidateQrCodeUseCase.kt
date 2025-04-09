package dev.rao.rockmarket.home.domain.usecase

import javax.inject.Inject

class ValidateQrCodeUseCase @Inject constructor() {
    operator fun invoke(qrContent: String): Result<String> {
        return if (qrContent.matches(Regex("^[0-9]+$"))) {
            Result.success(qrContent)
        } else {
            Result.failure(InvalidQrCodeException("El código QR debe contener solo números"))
        }
    }

    class InvalidQrCodeException(message: String) : Exception(message)
} 