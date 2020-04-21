package com.example.photoalbumdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.photoalbumdemo.R
import com.example.photoalbumdemo.bean.MediaBucket

class ChoiceAlbumAdapter(var context: Context, var mediaBucketList: MutableList<MediaBucket>) :
    BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ItemViewHolder
        var view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_choice_album, parent, false)
            viewHolder = ItemViewHolder()
            viewHolder.imageFirst = view.findViewById(R.id.image_first)
            viewHolder.albumName = view.findViewById(R.id.album_name)
            viewHolder.albumCount = view.findViewById(R.id.album_count)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ItemViewHolder
        }
        if (mediaBucketList[position].mediaList != null && mediaBucketList[position].mediaList.size > 0) {
            Glide.with(context).load(mediaBucketList[position].mediaList[0].mediaPath)
                .into(viewHolder.imageFirst)
        }
        viewHolder.albumName.text = mediaBucketList[position].bucketName
        viewHolder.albumCount.text = "${mediaBucketList[position].mediaList.size}张图片"
        return view
    }

    override fun getItem(position: Int): Any {
        return mediaBucketList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mediaBucketList.size
    }

    class ItemViewHolder {
        lateinit var imageFirst: ImageView
        lateinit var albumName: TextView
        lateinit var albumCount: TextView
    }
}