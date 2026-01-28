package com.yolbertdev.coffeeplatform.ui.main.screens.customer.add

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import coil3.Bitmap

@Composable
actual fun rememberCameraLauncher(onResult: (Bitmap?) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap -> onResult(bitmap) }
    return { launcher.launch() }
}
