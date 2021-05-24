package com.sd.lib.fileuri

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.FileNotFoundException
import java.io.OutputStream


class FAlbumImageUri {
    private val context: Context
    private var uri: Uri? = null

    private constructor(builder: Builder, context: Context) {
        this.context = context.applicationContext
        val contentValues = ContentValues().apply {
            this.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, builder.relativePath)
            this.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, builder.displayName)
            this.put(MediaStore.Images.ImageColumns.MIME_TYPE, builder.mimeType)
        }
        this.uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    /**
     * 打开输出流
     */
    fun openOutputStream(): OutputStream? {
        val uriTmp = uri ?: return null
        return try {
            context.contentResolver.openOutputStream(uriTmp)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    class Builder {
        var relativePath: String = Environment.DIRECTORY_PICTURES
        var displayName: String = ""
        var mimeType: String = ""

        fun build(context: Context): FAlbumImageUri {
            if (displayName.isEmpty()) {
                throw IllegalArgumentException("displayName is not specified")
            }

            if (mimeType.isEmpty()) {
                val extension = MimeTypeMap.getFileExtensionFromUrl(displayName)
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "image/jpeg"
            }

            return FAlbumImageUri(this, context)
        }
    }
}