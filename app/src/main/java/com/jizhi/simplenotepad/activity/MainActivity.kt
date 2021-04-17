package com.jizhi.simplenotepad.activity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jizhi.simplenotepad.R
import com.jizhi.simplenotepad.databinding.ActivityMainBinding
import com.jizhi.simplenotepad.entity.Note
import com.jizhi.simplenotepad.utils.ObjectBox
import io.objectbox.Box
import io.objectbox.kotlin.boxFor


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = this::class.java.name

    val noteBox: Box<Note> = ObjectBox.boxStore.boxFor()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO 添加机型版本
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        val explode: Transition =
            TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right)
        window.enterTransition = explode
        //TODO


        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))


        //长按删除
        binding.list.setOnItemLongClickListener { parent, view, position, id ->

            AlertDialog.Builder(this)
                .setTitle("删除")
                .setMessage("确认是否删除")
                .setPositiveButton("确定") { _, _ ->
                    noteBox.remove(id)
                    this.flushListData()
                }
                .setNeutralButton("取消", null)
                .create()
                .show()

            return@setOnItemLongClickListener true
        }

        //点击进入
        binding.list.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    private fun flushListData() {
        val all = noteBox.all
        if (all.size > 0) {
            binding.tvNull.visibility = View.INVISIBLE
        } else {
            binding.tvNull.visibility = View.VISIBLE
        }
        binding.list.adapter = object : BaseAdapter() {
            private val mInflater: LayoutInflater = LayoutInflater.from(applicationContext)
            override fun getCount(): Int {
                return all.size
            }

            override fun getItem(position: Int): Any {
                return all[position]
            }

            override fun getItemId(position: Int): Long {
                return all[position].id
            }

            @SuppressLint("ViewHolder")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = mInflater.inflate(R.layout.note, parent, false)
                val titleText = view.findViewById(R.id.text) as TextView
                titleText.text = all[position].value
                return view
            }
        }
    }

    override fun onResume() {
        Log.i(TAG, "onResume: ")
        this.flushListData()
        super.onResume()
    }

}