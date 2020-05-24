package com.example.photoalbumdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.photoalbumdemo.ui.PhotoAlbumActivity
import com.example.photoalbumdemo.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
        const val INTENT_REQUEST_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_goto_album.setOnClickListener {
            gotoPhotoAlbumActivity()
        }
    }

    private fun gotoPhotoAlbumActivity() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(
                    MainActivity@ this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    MainActivity@ this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //没有权限
                Toast.makeText(MainActivity@ this, "没有读取权限", Toast.LENGTH_SHORT).show()
                //手动申请权限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
                return
            }
        }
        val intent = Intent(this, PhotoAlbumActivity::class.java)
        startActivityForResult(intent, INTENT_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity@ this, "权限申请成功,请重新尝试", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(MainActivity@ this, "权限申请失败,请检查权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            INTENT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    text_select_result.text =
                        data.getStringArrayListExtra(Constants.INTENT_KEY_SELECT_ALBUM_RESULT)
                            .toString()
                }
            }
            else                -> {
                //暂不做处理
            }
        }
    }
}
