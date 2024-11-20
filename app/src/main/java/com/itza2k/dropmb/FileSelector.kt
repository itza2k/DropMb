package com.itza2k.dropmb

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@Composable
fun FileSelector(onFilesSelected: (List<String>) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        val filePaths = uris.mapNotNull { uri ->
            context.contentResolver.openInputStream(uri)?.use {
                uri.toString()
            }
        }
        onFilesSelected(filePaths)
    }

    Button(onClick = {
        lifecycleOwner.lifecycleScope.launch {
            filePickerLauncher.launch(arrayOf("application/pdf", "image/*"))
        }
    }) {
        Text("Select Files")
    }
}