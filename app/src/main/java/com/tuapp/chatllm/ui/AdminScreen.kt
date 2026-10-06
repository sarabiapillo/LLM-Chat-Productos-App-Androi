package com.tuapp.chatllm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tuapp.chatllm.R
import com.tuapp.chatllm.data.ContextItem
import com.tuapp.chatllm.data.ContextRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    repository: ContextRepository,
    onLogout: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var contexts by remember { mutableStateOf<List<ContextItem>>(emptyList()) }

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var editingId by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Productividad") }
    var priceText by remember { mutableStateOf("29.99") }
    var discountText by remember { mutableStateOf("20") }
    var isActive by remember { mutableStateOf(true) }

    val categories = listOf("Productividad", "Desarrollo", "Redacción", "Gestión de Tiempo")

    LaunchedEffect(Unit) {
        contexts = repository.getAllContexts()
    }

    fun resetForm() {
        editingId = ""
        title = ""
        content = ""
        category = "Productividad"
        priceText = "29.99"
        discountText = "20"
        isActive = true
    }

    fun startEditing(item: ContextItem) {
        editingId = item.id
        title = item.title
        content = item.content
        category = item.category
        priceText = item.price.toString()
        discountText = item.discountPercent.toString()
        isActive = item.isActive
        selectedTabIndex = 1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.admin_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.admin_welcome),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.admin_logout),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, modifier = Modifier.size(20.dp)) },
                text = { Text(stringResource(R.string.admin_tab_catalog)) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                icon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp)) },
                text = {
                    Text(
                        if (editingId.isEmpty())
                            stringResource(R.string.admin_tab_form)
                        else
                            "Editar"
                    )
                }
            )
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                icon = { Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(20.dp)) },
                text = { Text(stringResource(R.string.admin_tab_stats)) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTabIndex) {
            0 -> {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.admin_saved_contexts),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        Button(
                            onClick = {
                                resetForm()
                                selectedTabIndex = 1
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.admin_create_new_btn),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(contexts, key = { it.id }) { item ->
                            ContextItemCard(
                                item = item,
                                onEdit = { startEditing(item) },
                                onToggleActive = { updatedActive ->
                                    coroutineScope.launch {
                                        repository.saveContext(item.copy(isActive = updatedActive))
                                        contexts = repository.getAllContexts()
                                    }
                                },
                                onDelete = {
                                    coroutineScope.launch {
                                        repository.deleteContext(item.id)
                                        contexts = repository.getAllContexts()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            1 -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (editingId.isEmpty()) "Crear Nuevo Producto" else "Editar Producto",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text(stringResource(R.string.admin_add_title)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            label = { Text(stringResource(R.string.admin_add_content)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 90.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text(stringResource(R.string.admin_add_price)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = discountText,
                                onValueChange = { discountText = it },
                                label = { Text(stringResource(R.string.admin_add_discount)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.admin_add_category),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            categories.forEach { cat ->
                                FilterChip(
                                    selected = category == cat,
                                    onClick = { category = cat },
                                    label = { Text(cat, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(
                                    checked = isActive,
                                    onCheckedChange = { isActive = it }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.admin_active_switch),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (editingId.isNotEmpty()) {
                                    OutlinedButton(
                                        onClick = {
                                            resetForm()
                                            selectedTabIndex = 0
                                        }
                                    ) {
                                        Text(stringResource(R.string.admin_cancel_edit))
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (title.isNotBlank() && content.isNotBlank()) {
                                            val price = priceText.toDoubleOrNull() ?: 0.0
                                            val discount = discountText.toIntOrNull() ?: 0
                                            val itemToSave = ContextItem(
                                                id = editingId,
                                                title = title.trim(),
                                                content = content.trim(),
                                                category = category,
                                                price = price,
                                                discountPercent = discount,
                                                isActive = isActive
                                            )
                                            coroutineScope.launch {
                                                repository.saveContext(itemToSave)
                                                contexts = repository.getAllContexts()
                                                resetForm()
                                                selectedTabIndex = 0
                                            }
                                        }
                                    },
                                    enabled = title.isNotBlank() && content.isNotBlank()
                                ) {
                                    Text(stringResource(R.string.admin_save_button))
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                val totalCount = contexts.size
                val activeCount = contexts.count { it.isActive }
                val promoCount = contexts.count { it.discountPercent > 0 }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.admin_stat_total), style = MaterialTheme.typography.titleMedium)
                            Text("$totalCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.admin_stat_active), style = MaterialTheme.typography.titleMedium)
                            Text("$activeCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = colorResource(R.color.active_green))
                        }
                    }

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.admin_stat_promos), style = MaterialTheme.typography.titleMedium)
                            Text("$promoCount 🔥", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = colorResource(R.color.gold_accent))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContextItemCard(
    item: ContextItem,
    onEdit: () -> Unit,
    onToggleActive: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(item.category, style = MaterialTheme.typography.labelSmall) }
                        )

                        if (item.discountPercent > 0) {
                            Surface(
                                shape = MaterialTheme.shapes.extraSmall,
                                color = colorResource(R.color.gold_accent)
                            ) {
                                Text(
                                    text = "🔥 ${item.discountPercent}% OFF",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Surface(
                    onClick = { onToggleActive(!item.isActive) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (item.isActive)
                        colorResource(R.color.active_green_container)
                    else
                        colorResource(R.color.inactive_gray_container),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = if (item.isActive)
                            stringResource(R.string.admin_status_active)
                        else
                            stringResource(R.string.admin_status_inactive),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isActive)
                            colorResource(R.color.active_green)
                        else
                            colorResource(R.color.inactive_gray),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (item.price > 0.0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (item.discountPercent > 0) {
                        Text(
                            text = "$${item.price}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = "$${item.discountedPrice} USD",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "$${item.price} USD",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.admin_edit_btn),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.admin_edit_btn), style = MaterialTheme.typography.labelMedium)
                }

                TextButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.admin_delete),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.admin_delete),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    MaterialTheme {
        AdminScreen(repository = ContextRepository())
    }
}
