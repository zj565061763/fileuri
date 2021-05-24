package com.example.fileuri

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    val URL = "http://ilvbfanwe.oss-cn-shanghai.aliyuncs.com///public/attachment/test/noavatar_1.JPG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}