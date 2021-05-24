package com.sd.lib.fileuri

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object FFileUri {
    @JvmStatic
    fun getUri(file: File, context: Context): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                context.packageName + "." + FFileProvider::class.java.simpleName.toLowerCase(),
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }
}