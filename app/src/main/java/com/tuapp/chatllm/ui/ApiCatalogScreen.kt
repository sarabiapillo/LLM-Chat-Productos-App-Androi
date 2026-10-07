package com.tuapp.chatllm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuapp.chatllm.data.NetworkResult
import com.tuapp.chatllm.network.ProductDto
import com.tuapp.chatllm.viewmodel.CatalogViewModel

@Composable
fun ApiCatalogScreen(
    slug: String,
    viewModel: CatalogViewModel = viewModel()
) {
    val catalogState by viewModel.catalogState.collectAsState()

    LaunchedEffect(slug) {
        viewModel.loadCatalog(slug)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Catálogo de Productos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        when (catalogState) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Success -> {
                val data = (catalogState as NetworkResult.Success).data
                Text("Empresa: ${data.company.name}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    data.products?.let { products ->
                        items(products) { product ->
                            ProductCard(product)
                        }
                    }
                }
            }
            is NetworkResult.Error -> {
                Text(
                    text = (catalogState as NetworkResult.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            null -> {}
        }
    }
}

@Composable
fun ProductCard(product: ProductDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(product.name, style = MaterialTheme.typography.bodyLarge)
            Text("$${product.price}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            if (!product.isActive) {
                Text("Agotado", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            product.category?.let {
                Text("Categoría: ${it.name}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}
