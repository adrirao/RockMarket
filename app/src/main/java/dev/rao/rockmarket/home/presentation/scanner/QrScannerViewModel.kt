package dev.rao.rockmarket.home.presentation.scanner

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rao.rockmarket.home.domain.usecase.ValidateQrCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QrScannerViewModel @Inject constructor(
    private val validateQrCodeUseCase: ValidateQrCodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<QrScannerState>(QrScannerState.Initial)
    val state: StateFlow<QrScannerState> = _state.asStateFlow()

    fun processQrCode(qrContent: String) {
        val result = validateQrCodeUseCase(qrContent)

        result.fold(
            onSuccess = { validCode ->
                _state.value = QrScannerState.Success(validCode)
            },
            onFailure = { error ->
                _state.value = QrScannerState.Error(error.message ?: "Error desconocido")
            }
        )
    }

    fun resetState() {
        _state.value = QrScannerState.Initial
    }
}


