package com.jizhi.simplenotepad

import android.app.Application
import com.jizhi.simplenotepad.utils.ObjectBox

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}