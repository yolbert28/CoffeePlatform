package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.close
import com.yolbertdev.coffeeplatform.ui.theme.Gray200
import com.yolbertdev.coffeeplatform.ui.theme.Gray500
import com.yolbertdev.coffeeplatform.ui.theme.Gray700
import org.jetbrains.compose.resources.painterResource

@Composable
fun FilterSelector(
    rows: StaggeredGridCells = StaggeredGridCells.Fixed(2)
) {
    Column {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SelectedFilterItem(text = "De mayor a menor", onClick = {})
            SelectedFilterItem(text = "Deudas", onClick = {})
            SelectedFilterItem(text = "Algo maś", onClick = {})
            SelectedFilterItem(text = "Dolares", onClick = {})
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.height(28.dp).horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterSelectorItem("de mayor a menor")
            FilterSelectorItem("de mayor a menor")
            FilterSelectorItem("de mayor a menor")
            FilterSelectorItem("de mayor a menor")
            FilterSelectorItem("de mayor a menor")
            FilterSelectorItem("de mayor a menor")
            FilterSelectorItem("de mayor a menor")
            FilterSelectorItem("de mayor a menor")
        }
    }
}
