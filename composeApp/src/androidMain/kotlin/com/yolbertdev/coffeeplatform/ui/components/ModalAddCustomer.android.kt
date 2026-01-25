package com.yolbertdev.coffeeplatform.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import coil3.Bitmap

@Composable
actual fun rememberCameraLauncher(onResult: (Bitmap?) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap -> onResult(bitmap) }
    return { launcher.launch() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun ModalAddCustomer(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ){
        CommonModalAddCustomer {  }
    }
}
