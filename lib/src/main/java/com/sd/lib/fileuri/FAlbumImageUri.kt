package com.sd.lib.fileuri

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File
import java.util.*

object FAlbumImageUri {
    /**
     * 保存图片
     */
    @JvmStatic
    fun saveFile(file: File, context: Context): Uri? {
        if (!file.exists()) return null

        val ext = LibUtils.getExt(file.absolutePath)
        val contentValues = createContentValues(ext)

        val resolver = context.contentResolver
        val uri: Uri = try {
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return null

        return LibUtils.saveToUri(uri, file.inputStream(), resolver)
    }

    private fun createContentValues(ext: String): ContentValues {
        val uuid = UUID.randomUUID().toString()
        val finalExt = if (ext.isNotEmpty()) ext else "jpg"
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(finalExt) ?: "image/jpeg"

        return ContentValues().apply {
            this.put(MediaStore.Images.ImageColumns.TITLE, uuid)
            this.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, uuid)
            this.put(MediaStore.Images.ImageColumns.DESCRIPTION, uuid)
            this.put(MediaStore.Images.ImageColumns.MIME_TYPE, mimeType)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }
    }
}