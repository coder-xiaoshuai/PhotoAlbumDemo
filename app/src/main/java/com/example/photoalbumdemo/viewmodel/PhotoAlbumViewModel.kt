package com.example.photoalbumdemo.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoalbumdemo.bean.MediaBucket
import com.example.photoalbumdemo.bean.MediaType
import com.example.photoalbumdemo.callback.CommonCallback
import com.example.photoalbumdemo.utils.AlbumPhotoUtils

class PhotoAlbumViewModel : ViewModel() {
    val photoAlbumLiveData = MutableLiveData<MutableList<MediaBucket>>()

    fun getAlbumPictureList(context: Context) {
        AlbumPhotoUtils.getAlbumPictureList(
            context,
            MediaType.PICTURE_AND_VIDEO,
            object : CommonCallback<MutableList<MediaBucket>> {
                override fun call(t: MutableList<MediaBucket>) {
                    photoAlbumLiveData.value = t
                }

            })
    }
}