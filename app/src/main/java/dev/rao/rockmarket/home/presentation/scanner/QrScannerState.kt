package dev.rao.rockmarket.home.presentation.scanner

sealed class QrScannerState {
    object Initial : QrScannerState()
    data class Success(val code: String) : QrScannerState()
    data class Error(val message: String) : QrScannerState()
} 