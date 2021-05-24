package com.sd.lib.fileuri

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class FAlbumImageUri {
    private val context: Context

    private val ext: String
    private val mimeType: String
    private val title: String
    private val description: String

    private constructor(builder: Builder, context: Context) {
        this.context = context.applicationContext
        this.ext = builder.ext
        this.mimeType = builder.mimeType

        val uuid = UUID.randomUUID().toString()
        this.title = "${uuid}.${builder.ext}"
        this.description = uuid
    }

    private fun createContentValues(): ContentValues {
        return ContentValues().apply {
            this.put(MediaStore.Images.ImageColumns.TITLE, title)
            this.put(MediaStore.Images.ImageColumns.MIME_TYPE, mimeType)
            this.put(MediaStore.Images.ImageColumns.DESCRIPTION, description)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }
    }

    /**
     * 保存图片
     */
    fun saveFile(file: File): Uri? {
        if (file == null || !file.exists()) return null

        val bitmap: Bitmap = try {
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return null

        val uri = saveBitmap(bitmap)
        bitmap.recycle()
        return uri
    }

    /**
     * 保存图片
     */
    fun saveBitmap(bitmap: Bitmap): Uri? {
        val contentValues = createContentValues()
        val resolver = context.contentResolver
        val uri: Uri = try {
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return null

        val outputStream = try {
            resolver.openOutputStream(uri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            runCatching {
                resolver.delete(uri, null, null)
            }
            null
        } ?: return null

        val success: Boolean = outputStream.use {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        return if (success) uri else null
    }

    class Builder {
        var ext: String = ""
        var mimeType: String = ""

        fun build(context: Context): FAlbumImageUri {
            if (ext.isEmpty()) ext = "jpg"

            if (mimeType.isEmpty()) {
                val extension = MimeTypeMap.getFileExtensionFromUrl(ext)
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "image/jpeg"
            }
            return FAlbumImageUri(this, context)
        }
    }
}