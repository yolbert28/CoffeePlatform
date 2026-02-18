package com.yolbertdev.coffeeplatform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yolbertdev.coffeeplatform.util.ImageStorage
import com.yolbertdev.coffeeplatform.pdf.openPdf
import com.yolbertdev.coffeeplatform.sync.AndroidBackupFileManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
// Recuperamos el BackupFileManager de Koin y registramos el launcher
        val backupFileManager: AndroidBackupFileManager by inject()
        backupFileManager.registerLauncher(this)
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