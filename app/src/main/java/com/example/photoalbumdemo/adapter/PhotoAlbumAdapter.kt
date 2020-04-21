package com.example.photoalbumdemo.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photoalbumdemo.R
import com.example.photoalbumdemo.bean.ItemMedia
import com.example.photoalbumdemo.bean.MediaBucket
import com.example.photoalbumdemo.bean.MediaType


class PhotoAlbumAdapter(var context: Context, var itemMediaList: MutableList<ItemMedia>) :
    RecyclerView.Adapter<PhotoAlbumAdapter.PhotoAlbumHolder>() {
    private var itemWidth = 100
    val selectedList: MutableList<ItemMedia> = ArrayList()

    init {
        val outMetrics = DisplayMetrics()
        (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels
        itemWidth = widthPixels.div(3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAlbumHolder {
        return PhotoAlbumHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_media,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return if (itemMediaList == null) 0 else itemMediaList.size
    }

    override fun onBindViewHolder(holder: PhotoAlbumHolder, position: Int) {
        var layoutParams = holder.itemView.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemWidth
        Glide.with(context).load(itemMediaList[position].mediaPath).into(holder.imagePhoto)
        if (itemMediaList[position].mediaType == MediaType.VIDEO) {
            holder.imageMediaType.visibility = View.VISIBLE
        } else {
            holder.imageMediaType.visibility = View.GONE
        }

        val mediaTypeIcon =
            if (itemMediaList[position].selected) R.drawable.icon_bg_select else R.drawable.icon_bg_un_select
        holder.imageSelected.setImageResource(mediaTypeIcon)
        holder.imageSelected.setOnClickListener {
            if (itemMediaList[position].selected) {
                //进行取消
                itemMediaList[position].selected = false
                selectedList.remove(itemMediaList[position])
                notifyItemChanged(position)
            } else {
                //进行选择
                if (selectedList.size >= 9) {
                    Toast.makeText(context, "最多选择9张图片", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                itemMediaList[position].selected = true
                selectedList.add(itemMediaList[position])
                notifyItemChanged(position)
            }


        }

    }

    fun refreshData(itemMediaList: MutableList<ItemMedia>?) {
        if (itemMediaList != null && itemMediaList.size > 0) {
            this.itemMediaList.clear()
            this.itemMediaList.addAll(itemMediaList)
            notifyDataSetChanged()
        }
    }


    class PhotoAlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagePhoto: ImageView = itemView.findViewById<ImageView>(R.id.image)
        var imageMediaType: ImageView = itemView.findViewById<ImageView>(R.id.media_type)
        var imageSelected: ImageView = itemView.findViewById(R.id.image_selected)
    }
}