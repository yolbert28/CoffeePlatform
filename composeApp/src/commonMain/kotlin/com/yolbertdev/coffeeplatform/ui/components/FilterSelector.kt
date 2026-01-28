package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSelector(
    modifier: Modifier = Modifier
) {
    //1. Definimos la lista base y los seleccionados
    val filters = listOf("Todos", "Deudas", "Dólares", "Quintales", "Mayor a menor", "Recientes")
    val selectedFilters = remember { mutableStateListOf("Dólares", "Recientes") }

    // 2. Ordenamos: los que están en 'selectedFilters' primero
    val sortedFilters = remember(selectedFilters.toList()) {
        filters.sortedByDescending { selectedFilters.contains(it) }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.width(12.dp))

        sortedFilters.forEach { filter ->
            val isSelected = selectedFilters.contains(filter)

            FilterChip(
                selected = isSelected,
                onClick = {
                    if (isSelected) selectedFilters.remove(filter)
                    else selectedFilters.add(filter)
                },
                label = { Text(text = filter) },
                // ... el resto del estilo que ya definimos
                leadingIcon = if (isSelected) {
                    { Icon(Icons.Rounded.Check, null, modifier = Modifier.size(18.dp)) }
                } else null,
                // Animamos el cambio de posición (opcional pero recomendado)
                modifier = Modifier.animateContentSize()
            )
        }

        Spacer(Modifier.width(12.dp))
    }
}