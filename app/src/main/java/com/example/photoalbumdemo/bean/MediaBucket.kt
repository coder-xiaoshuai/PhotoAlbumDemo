package com.example.photoalbumdemo.bean

import com.example.photoalbumdemo.bean.ItemMedia
import java.util.*

data class MediaBucket(var bucketName:String = "") {
    
    val mediaList: MutableList<ItemMedia> =
        ArrayList()

    fun addItemMedia(itemMedia: ItemMedia) {
        mediaList.add(itemMedia)
    }
}