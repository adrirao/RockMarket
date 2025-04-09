package dev.rao.rockmarket.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import dagger.hilt.android.AndroidEntryPoint
import dev.rao.rockmarket.R
import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.home.presentation.components.NeumorphicButton
import dev.rao.rockmarket.home.presentation.components.NeumorphicCard
import dev.rao.rockmarket.home.presentation.theme.NeuColors
import dev.rao.rockmarket.home.presentation.theme.RockMarketTheme
import dev.rao.rockmarket.home.util.truncateWithEllipsis

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                RockMarketTheme {
                    HomeScreen(
                        viewModel = viewModel,
                        onLogout = {
                            viewModel.signOut()
                            navigateToLogin()
                        },
                        onScanQr = {
                            navigateToQrScanner()
                        }
                    )
                }
            }
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    private fun navigateToQrScanner() {
        findNavController().navigate(R.id.action_homeFragment_to_qrScannerFragment)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        viewModel: HomeViewModel,
        onLogout: () -> Unit,
        onScanQr: () -> Unit
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle(initialValue = HomeState.Initial)
        val products by viewModel.products.collectAsStateWithLifecycle(initialValue = emptyList())

        HomeScreenContent(
            state = state,
            products = products,
            onLogout = onLogout,
            onScanQr = onScanQr
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreenContent(
        state: HomeState,
        products: List<Product>,
        onLogout: () -> Unit,
        onScanQr: () -> Unit
    ) {
        var showMenu by remember { mutableStateOf(false) }

        /*NeumorphicSurface(
            modifier = Modifier.fillMaxSize(),
        ) {*/
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "MarketRock",
                            color = NeuColors.text,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Más opciones",
                                tint = NeuColors.text
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Cerrar sesión",
                                        color = NeuColors.text
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onLogout()
                                }
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                NeumorphicButton(
                    onClick = onScanQr,
                    cornerRadius = 28.dp,
                    elevation = 12.dp,
                    modifier = Modifier
                        .size(56.dp),
                    background = NeuColors.text
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.QrCodeScanner,
                            contentDescription = "Escanear QR",
                            tint = NeuColors.background,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            },
            content = { paddingValues ->
                HomeContent(
                    state = state,
                    products = products,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                )
            }
        )
        /*}*/
    }

    @Composable
    fun HomeContent(
        state: HomeState,
        products: List<Product>,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is HomeState.Initial -> {
                    Text(
                        text = "Cargando información del país...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = NeuColors.text
                    )
                }

                is HomeState.Success -> {
                    val country = state.country
                    if (country != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Productos Destacados",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = NeuColors.text,
                            )
                            Text(
                                text = country.id,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = NeuColors.text,
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        ProductsBanner(products)
                    } else {
                        Text(
                            text = "No se ha seleccionado ningún país",
                            style = MaterialTheme.typography.bodyLarge,
                            color = NeuColors.text
                        )
                    }
                }

                is HomeState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    @Composable
    fun ProductsBanner(products: List<Product>) {
        if (products.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cargando productos...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = NeuColors.text
                    )
                    CircularProgressIndicator(
                        color = NeuColors.accent
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
            ) {
                items(products) { product ->
                    FeaturedProductItem(product) {
                        navigateToProductDetail(product.id)
                    }
                }
            }
        }
    }

    @Composable
    fun FeaturedProductItem(product: Product, onClick: () -> Unit) {
        var isFavorite by rememberSaveable { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }

        NeumorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 300.dp)
                .clickable(onClick = onClick)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(product.imageUrl),
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .background(
                                color = NeuColors.background,
                                shape = RoundedCornerShape(percent = 50)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { isFavorite = !isFavorite },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = if (isFavorite) {
                                "Quitar de favoritos"
                            } else {
                                "Agregar a favoritos"
                            },
                            tint = if (isFavorite) Color(0xFFE91E63) else Color(0xFF666666),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = product.title.truncateWithEllipsis(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = NeuColors.text
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$${product.price}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = NeuColors.accent,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            modifier = Modifier.align(Alignment.Bottom),
                            text = "Ver detalles",
                            style = MaterialTheme.typography.bodySmall,
                            color = NeuColors.text
                        )
                    }
                }
            }
        }
    }

    private fun navigateToProductDetail(productId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(productId)
        findNavController().navigate(action)
    }
}