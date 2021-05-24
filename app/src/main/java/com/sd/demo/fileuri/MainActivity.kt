package com.sd.demo.fileuri

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.fileuri.databinding.ActivityMainBinding
import com.sd.lib.dldmgr.DownloadInfo
import com.sd.lib.dldmgr.DownloadManagerConfig
import com.sd.lib.dldmgr.FDownloadManager
import com.sd.lib.dldmgr.IDownloadManager
import com.sd.lib.fileuri.FAlbumImageUri
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = MainActivity::class.java.simpleName
    val URL = "http://ilvbfanwe.oss-cn-shanghai.aliyuncs.com///public/attachment/test/noavatar_1.JPG"

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        DownloadManagerConfig.init(DownloadManagerConfig.Builder().build(this))

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        FDownloadManager.default.addCallback(_downloadCallback)
    }

    override fun onClick(v: View?) {
        when (v) {
            _binding.btnClick -> {
                FDownloadManager.default.addTask(URL)
            }
        }
    }

    /**
     * 下载回调
     */
    private val _downloadCallback = object : IDownloadManager.Callback {
        override fun onPrepare(info: DownloadInfo) {
        }

        override fun onProgress(info: DownloadInfo) {
        }

        override fun onSuccess(info: DownloadInfo, file: File) {
            val uuid = UUID.randomUUID().toString()
            val imageUri = FAlbumImageUri.Builder().apply {
                this.displayName = uuid + file.name
            }.build(this@MainActivity)

            imageUri.openOutputStream()?.use { outputStream ->
                file.inputStream().use { inputStream ->
                    val copyTo = inputStream.copyTo(outputStream)
                    Log.i(TAG, "copyTo:${copyTo}")
                }
            }
        }

        override fun onError(info: DownloadInfo) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FDownloadManager.default.removeCallback(_downloadCallback)
    }
}