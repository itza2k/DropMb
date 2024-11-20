package com.itza2k.dropmb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itza2k.dropmb.ui.theme.DropMbTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DropMbTheme {
                HomePage()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    var selectedFiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var compressionLevel by remember { mutableStateOf<String?>(null) }
    var isCompressing by remember { mutableStateOf(false) }
    var compressionResult by remember { mutableStateOf<String?>(null) }
    var compressedFileSize by remember { mutableStateOf<Long?>(null) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FileSelector { files ->
                    selectedFiles = files
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (selectedFiles.isNotEmpty()) {
                    Text("Selected Files: ${selectedFiles.joinToString()}")
                    Spacer(modifier = Modifier.height(16.dp))
                    CompressionOptions { level ->
                        compressionLevel = level
                        isCompressing = true
                        compressionResult = null
                        compressedFileSize = null
                        val compressor = FileCompressor()
                        compressedFileSize = compressor.compressFiles(selectedFiles, "output.zip", compressionLevel ?: "Medium")
                        isCompressing = false
                        compressionResult = "Compression completed successfully!"
                    }
                }
                if (isCompressing) {
                    CircularProgressIndicator()
                }
                compressionResult?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it)
                    compressedFileSize?.let { size ->
                        Text("Compressed File Size: $size bytes")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(Uri.fromFile(File("output.zip")), "application/zip")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        }) {
                            Text("View Compressed File")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CompressionOptions(onOptionSelected: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Select Compression Level")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onOptionSelected("Small") }) {
            Text("Small Compression")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onOptionSelected("Medium") }) {
            Text("Medium Compression")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onOptionSelected("High") }) {
            Text("High Compression")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    DropMbTheme {
        HomePage()
    }
}