package com.example.photoalbumdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photoalbumdemo.R
import com.example.photoalbumdemo.adapter.ChoiceAlbumAdapter
import com.example.photoalbumdemo.adapter.PhotoAlbumAdapter
import com.example.photoalbumdemo.bean.ItemMedia
import com.example.photoalbumdemo.bean.MediaBucket
import com.example.photoalbumdemo.utils.AlbumPhotoUtils
import com.example.photoalbumdemo.viewmodel.PhotoAlbumViewModel
import kotlinx.android.synthetic.main.activity_album.*


class PhotoAlbumActivity : AppCompatActivity() {
    private lateinit var photoAlbumViewModel: PhotoAlbumViewModel
    private var albumList: MutableList<MediaBucket>? = null
    private lateinit var itemMediaList: MutableList<ItemMedia>
    private lateinit var adapter: PhotoAlbumAdapter
    private var popupWindow: PopupWindow? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        photoAlbumViewModel = ViewModelProviders.of(this).get(PhotoAlbumViewModel::class.java)
        albumRecyclerView.layoutManager = GridLayoutManager(this, 3)
        itemMediaList = ArrayList()
        adapter = PhotoAlbumAdapter(this, itemMediaList)
        albumRecyclerView.adapter = adapter
        albumRecyclerView.itemAnimator = null
        queryLocalAlbumData()
        text_album_name.setOnClickListener {
            showChoiceAlbumDialog(it)
        }
    }

    /**
     * 弹出选择框
     */
    private fun showChoiceAlbumDialog(anchor: View) {
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow?.dismiss()
            return
        }
        //创建PopupWindow
        val contentView = LayoutInflater.from(this).inflate(R.layout.layout_popup_window, null)
        popupWindow = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val listView = contentView.findViewById<ListView>(R.id.album_choice_list)
        if (albumList != null) {
            listView.adapter = ChoiceAlbumAdapter(this, albumList!!)
            listView.setOnItemClickListener { parent, view, position, id ->
                switchMediaBucket(albumList!![position].bucketName)
                popupWindow?.dismiss()
            }
        }
        popupWindow!!.animationStyle = R.style.Popupwindow
        popupWindow!!.showAsDropDown(anchor, 0, 0)
        popupWindow!!.isClippingEnabled = true
    }

    /**
     * 查询本地相册数据
     */
    private fun queryLocalAlbumData() {
        photoAlbumViewModel.getAlbumPictureList(this)
        photoAlbumViewModel.photoAlbumLiveData.observe(this, Observer<MutableList<MediaBucket>> {
            albumList = it
            switchMediaBucket(AlbumPhotoUtils.defaultMediaBucketName)
        })
    }

    /**
     * 切换相册文件夹
     */
    private fun switchMediaBucket(bucketName: String) {
        text_album_name.text = bucketName
        itemMediaList = albumList?.first { mediaBucket ->
            bucketName == mediaBucket.bucketName
        }?.mediaList!!
        adapter.refreshData(itemMediaList)
    }
}