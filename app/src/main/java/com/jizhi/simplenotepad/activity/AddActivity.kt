package com.jizhi.simplenotepad.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.jizhi.simplenotepad.R
import com.jizhi.simplenotepad.databinding.ActivityAddBinding
import com.jizhi.simplenotepad.entity.Note
import com.jizhi.simplenotepad.utils.ObjectBox
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class AddActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddBinding
    private val TAG = this::class.java.name

    private var id = -1L

    private val noteBox: Box<Note> = ObjectBox.boxStore.boxFor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        this.initActionBar()
        binding.note.doAfterTextChanged {
            binding.tvSize.text = "当前字数：${it!!.length}"
            Log.i(TAG, "onCreate: ")
        }
    }

    override fun onStart() {
        super.onStart()
        //如果是从主页面点进来的
        id = intent.getLongExtra("id", -1L)
        if (id != -1L) {
            binding.note.setText(noteBox.get(id).value)
        }
    }

    private fun initActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        title = "添加"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onPause() {
        val value = binding.note.text.toString()
        //内容非空判断
        if (value.isNotBlank()) {
            if (id == -1L) {
                noteBox.put(Note(value = value))
            } else {
                noteBox.put(Note(id = id, value = value))
            }
            finish()
        }
        super.onPause()
    }
}