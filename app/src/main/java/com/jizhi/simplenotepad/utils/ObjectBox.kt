package com.jizhi.simplenotepad.utils

import android.content.Context
import com.jizhi.simplenotepad.entity.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
    }
}