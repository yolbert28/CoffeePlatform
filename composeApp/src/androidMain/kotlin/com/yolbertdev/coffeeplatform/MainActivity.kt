package com.yolbertdev.coffeeplatform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yolbertdev.coffeeplatform.util.ImageStorage
import com.yolbertdev.coffeeplatform.pdf.openPdf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ImageStorage.appContext = applicationContext

        setContent {
            App(onOpenPdf = { filePath ->
                openPdf(context = this, filePath = filePath)
            })
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}