package com.eltex.androidschool.view.common

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class OffsetDecoration(
    @Px private val horizontalOffset: Int,
    @Px private val verticalOffset: Int
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top += verticalOffset
        outRect.bottom += verticalOffset
        outRect.left += horizontalOffset
        outRect.right += horizontalOffset
    }
}
