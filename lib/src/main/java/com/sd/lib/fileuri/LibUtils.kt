package com.sd.lib.fileuri

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.InputStream

internal object LibUtils {
    /**
     * 将[inputStream]内容保存到[uri]
     */
    fun saveToUri(uri: Uri, inputStream: InputStream?, resolver: ContentResolver): Uri? {
        try {
            resolver.openOutputStream(uri)?.use { output ->
                inputStream?.use { input ->
                    val copySize = input.copyTo(output)
                    if (copySize > 0) return uri
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            runCatching {
                resolver.delete(uri, null, null)
            }
        }
        return null
    }

    fun getExt(url: String?): String {
        if (url == null) return ""
        var ext = MimeTypeMap.getFileExtensionFromUrl(url)
        if (ext == null || ext.isEmpty()) {
            val lastIndex = url.lastIndexOf(".")
            if (lastIndex > 0) {
                ext = url.substring(lastIndex + 1)
            }
        }
        return ext ?: ""
    }
}