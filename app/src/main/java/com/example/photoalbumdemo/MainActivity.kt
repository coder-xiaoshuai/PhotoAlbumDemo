package com.example.photoalbumdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.photoalbumdemo.bean.MediaBucket
import com.example.photoalbumdemo.bean.MediaType
import com.example.photoalbumdemo.callback.CommonCallback
import com.example.photoalbumdemo.ui.PhotoAlbumActivity
import com.example.photoalbumdemo.utils.AlbumPhotoUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_goto_album.setOnClickListener {
            val intent = Intent(this, PhotoAlbumActivity::class.java)
            startActivity(intent)
        }
    }
}
