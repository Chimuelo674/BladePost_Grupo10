package com.example.bladepost_grupo10.utils


import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getNewImageFile(context: Context): File {


    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"

    val storageDir: File? = File(context.getExternalFilesDir(null), "BladePost_Images")

    // Asegúrate de que la carpeta exista. Si no, la crea.
    if (storageDir != null && !storageDir.exists()) {
        storageDir.mkdirs()
    }

    // Crea el archivo de imagen
    return File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )
}


fun getUriForFile(context: Context, file: File): Uri {
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}
