package dev.rao.rockmarket.payment.domain.result

data class CardValidationResult(
    val cardNumberError: String? = null,
    val cvvError: String? = null,
    val expirationMonthError: String? = null,
    val expirationYearError: String? = null
) {
    fun isValid(): Boolean {
        return cardNumberError == null &&
                cvvError == null &&
                expirationMonthError == null &&
                expirationYearError == null
    }
}