package com.example.photoalbumdemo.utils

import android.R
import android.content.Context
import android.provider.MediaStore
import android.text.TextUtils
import com.example.photoalbumdemo.bean.ItemMedia
import com.example.photoalbumdemo.bean.MediaBucket
import com.example.photoalbumdemo.bean.MediaType
import com.example.photoalbumdemo.callback.CommonCallback
import java.io.File
import java.util.concurrent.Callable


class AlbumPhotoUtils {
    companion object {
        private val systemFolderNameList = listOf("Camera", "相机")
        const val defaultMediaBucketName = "手机相册"

        fun getAlbumPictureList(
            context: Context,
            mediaType: MediaType,
            successCallback: CommonCallback<MutableList<MediaBucket>>
        ) {
            RxJavaUtils.asyncDo(Callable<MutableList<MediaBucket>> {
                getMediaList(context, mediaType)
            }, successCallback)
        }

        private fun getMediaList(context: Context, mediaType: MediaType): MutableList<MediaBucket> {
            val uri = MediaStore.Files.getContentUri("external")
            //查询条件
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.TITLE
            )
            var selection = when {
                MediaType.PICTURE_AND_VIDEO == mediaType -> {
                    MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                }
                MediaType.VIDEO == mediaType -> {
                    MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                }
                else -> {
                    MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                }
            }
            var cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            )
            var mediaBucketMap = LinkedHashMap<String, MediaBucket>()
            var systemGalleryPath = ""
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val filePath = cursor.getString(1)

                    if (!checkFileSizeZero(filePath)) {
                        var createTime = getMediaCreateTime(filePath)
                        var mediaType =
                            if (MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO == cursor.getInt(3)) MediaType.VIDEO else MediaType.PICTURE
                        var itemMedia = ItemMedia(cursor.getInt(0), createTime, filePath, mediaType)
                        val parent = File(filePath).parent
                        if (!TextUtils.isEmpty(parent)) {
                            var mediaBucket = mediaBucketMap[parent]
                            if (mediaBucket == null) {
                                mediaBucket = MediaBucket()
                                val folderName = File(parent).name
                                if (systemFolderNameList.contains(folderName)) {
                                    systemGalleryPath = parent;
                                }
                                mediaBucket.bucketName = folderName
                                mediaBucketMap[parent] = mediaBucket
                            }

                            if (!checkFileSizeZero(itemMedia.mediaPath)) {
                                mediaBucket.addItemMedia(itemMedia)
                            }
                        }
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
            return prepareMediaList(mediaBucketMap, systemGalleryPath)
        }

        private fun prepareMediaList(
            mediaBucketMap: MutableMap<String, MediaBucket>,
            systemGalleryPath: String
        ): MutableList<MediaBucket> {
            val mediaBucketList = ArrayList<MediaBucket>()
            var galleryBucket: MediaBucket? = null
            if (mediaBucketMap.isNotEmpty() && !TextUtils.isEmpty(systemGalleryPath)) {
                galleryBucket = mediaBucketMap.remove(systemGalleryPath)
                galleryBucket!!.bucketName = defaultMediaBucketName
            }
            if (galleryBucket != null) {
                mediaBucketList.add(galleryBucket);
            }
            mediaBucketList.addAll(mediaBucketMap.values);
            return mediaBucketList;
        }

        private fun checkFileSizeZero(filePath: String): Boolean {
            val file = File(filePath)
            return file.length().equals(0)
        }

        private fun getMediaCreateTime(filePath: String): Long {
            return File(filePath).lastModified()
        }
    }

}