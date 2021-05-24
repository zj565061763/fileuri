package com.sd.demo.fileuri

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.fileuri.databinding.ActivityMainBinding
import com.sd.lib.dldmgr.DownloadInfo
import com.sd.lib.dldmgr.FDownloadManager
import com.sd.lib.dldmgr.IDownloadManager
import com.sd.lib.fileuri.FAlbumImageUri
import com.sd.lib.fileuri.FAlbumVideoUri
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = MainActivity::class.java.simpleName
    val URL_IMAGE = "http://ilvbfanwe.oss-cn-shanghai.aliyuncs.com///public/attachment/test/noavatar_1.JPG"
    val URL_VIDEO = "http://1251020758.vod2.myqcloud.com/8a96e57evodgzp1251020758/602d1d1a5285890800849942893/tRGP04QVdCEA.mp4"

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun onClick(v: View?) {
        when (v) {
            _binding.btnClickImage -> {
                clickImage()
            }
            _binding.btnClickVideo -> {
                clickVideo()
            }
        }
    }

    /**
     * 保存图片
     */
    private fun clickImage() {
        val addTask = FDownloadManager.default.addTask(URL_IMAGE)
        if (addTask) {
            FDownloadManager.default.addUrlCallback(URL_IMAGE, object : IDownloadManager.Callback {
                override fun onPrepare(info: DownloadInfo) {
                }

                override fun onProgress(info: DownloadInfo) {
                }

                override fun onSuccess(info: DownloadInfo, file: File) {
                    AndPermission.with(this@MainActivity).runtime()
                        .permission(Permission.Group.STORAGE)
                        .onGranted {
                            val uri = FAlbumImageUri.saveFile(file, applicationContext)
                            Log.i(TAG, "image saveFile:${uri}")
                        }.onDenied {
                            finish()
                        }.start()
                }

                override fun onError(info: DownloadInfo) {
                }
            })
        }
    }

    private fun clickVideo() {
        val addTask = FDownloadManager.default.addTask(URL_VIDEO)
        if (addTask) {
            FDownloadManager.default.addUrlCallback(URL_VIDEO, object : IDownloadManager.Callback {
                override fun onPrepare(info: DownloadInfo) {
                }

                override fun onProgress(info: DownloadInfo) {
                }

                override fun onSuccess(info: DownloadInfo, file: File) {
                    AndPermission.with(this@MainActivity).runtime()
                        .permission(Permission.Group.STORAGE)
                        .onGranted {
                            val uri = FAlbumVideoUri.saveFile(file, applicationContext)
                            Log.i(TAG, "video saveFile:${uri}")
                        }.onDenied {
                            finish()
                        }.start()
                }

                override fun onError(info: DownloadInfo) {
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FDownloadManager.default.cancelTask(URL_IMAGE)
        FDownloadManager.default.cancelTask(URL_VIDEO)
    }
}