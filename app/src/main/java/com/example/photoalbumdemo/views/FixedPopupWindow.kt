package com.example.photoalbumdemo.views

import android.graphics.Rect
import android.os.Build
import android.view.View
import android.widget.PopupWindow


class FixedPopupWindow(contentView: View?, width: Int, height: Int) :
    PopupWindow(contentView, width, height) {
    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor!!.getGlobalVisibleRect(rect) // 以屏幕 左上角 为参考系的
            val h: Int =
                anchor.resources.displayMetrics.heightPixels - rect.bottom //屏幕高度减去 anchor 的 bottom
            height = h // 重新设置PopupWindow高度
        }
        super.showAsDropDown(anchor, xoff, yoff)
    }
}