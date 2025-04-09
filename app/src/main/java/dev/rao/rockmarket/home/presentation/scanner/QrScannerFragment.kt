package dev.rao.rockmarket.home.presentation.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import dev.rao.rockmarket.databinding.FragmentQrScannerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QrScannerFragment : Fragment() {

    private var _binding: FragmentQrScannerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QrScannerViewModel by viewModels()

    private lateinit var cameraExecutor: ExecutorService
    private var processingBarcode = false
    private lateinit var barcodeScanner: BarcodeScanner
    private var lastScannedCode: String = ""
    private var errorCooldownActive = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Snackbar.make(
                binding.root,
                "Se requieren permisos de cámara para escanear QR",
                Snackbar.LENGTH_LONG
            ).show()
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar la barra de herramientas
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Inicializar el escáner de código de barras con opciones para QR
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        // Inicializar el ejecutor de la cámara
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Observar cambios en el estado del escáner QR
        observeQrScanState()

        // Verificar permisos de cámara
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun observeQrScanState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is QrScannerState.Initial -> {
                            // No hacer nada, esperando escanear
                        }

                        is QrScannerState.Success -> {
                            showSuccessAndNavigateBack(state.code)
                        }

                        is QrScannerState.Error -> {
                            if (!errorCooldownActive) {
                                showError(state.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessAndNavigateBack(code: String) {
        Snackbar.make(
            binding.root,
            "Código QR válido: $code",
            Snackbar.LENGTH_LONG
        ).show()
        findNavController().navigateUp()
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()

        // Activar el período de enfriamiento para evitar múltiples Snackbars
        errorCooldownActive = true
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000) // 3 segundos de enfriamiento
            errorCooldownActive = false
            processingBarcode = false
            viewModel.resetState()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Configurar la vista previa
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

            // Configurar el analizador de imágenes
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor, QrCodeAnalyzer { barcode ->
                if (!processingBarcode && !errorCooldownActive) {
                    // Evitar procesar el mismo código repetidamente
                    if (barcode != lastScannedCode) {
                        lastScannedCode = barcode
                        processingBarcode = true
                        viewModel.processQrCode(barcode)
                    }
                }
            })

            try {
                // Desconectar casos de uso antes de reajustar
                cameraProvider.unbindAll()

                // Vincular casos de uso a la cámara
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error al vincular casos de uso a la cámara", e)
                showError("Error al inicializar la cámara")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private inner class QrCodeAnalyzer(private val onQrCodeDetected: (String) -> Unit) :
        ImageAnalysis.Analyzer {

        override fun analyze(image: ImageProxy) {
            val mediaImage = image.image
            if (mediaImage != null) {
                val inputImage = InputImage.fromMediaImage(
                    mediaImage,
                    image.imageInfo.rotationDegrees
                )

                barcodeScanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty() && !processingBarcode && !errorCooldownActive) {
                            val barcode = barcodes[0]
                            barcode.rawValue?.let { qrContent ->
                                onQrCodeDetected(qrContent)
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Error al procesar el código QR", it)
                    }
                    .addOnCompleteListener {
                        image.close()
                    }
            } else {
                image.close()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    companion object {
        private const val TAG = "QrScannerFragment"
    }
} 