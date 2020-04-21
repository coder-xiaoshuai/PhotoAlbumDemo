package com.example.photoalbumdemo.bean

data class ItemMedia(
    var mediaId: Int,
    var mediaCreateTimestamp: Long,
    var mediaPath: String,
    var mediaType: MediaType,
    var selected: Boolean = false
)