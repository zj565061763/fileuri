package com.sd.demo.fileuri

import android.app.Application
import com.sd.lib.dldmgr.DownloadManagerConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化下载器
        DownloadManagerConfig.init(DownloadManagerConfig.Builder().build(this))
    }
}