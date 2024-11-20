package com.itza2k.dropmb

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import java.io.File

class FileCompressor {

    fun compressFiles(files: List<String>, outputZipPath: String, compressionLevel: String): Long {
        val zipFile = ZipFile(outputZipPath)
        val parameters = ZipParameters().apply {
            compressionMethod = CompressionMethod.DEFLATE
            this.compressionLevel = when (compressionLevel) {
                "Small" -> CompressionLevel.FASTEST
                "Medium" -> CompressionLevel.NORMAL
                "High" -> CompressionLevel.MAXIMUM
                else -> CompressionLevel.NORMAL
            }
        }

        files.forEach { filePath ->
            val file = File(filePath)
            if (file.exists()) {
                zipFile.addFile(file, parameters)
            }
        }

        return File(outputZipPath).length()
    }
}