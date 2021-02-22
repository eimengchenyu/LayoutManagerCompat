package com.chw.layoutmanager.compat

import androidx.recyclerview.widget.RecyclerView

/**
 * @author hanwei.chen
 * 2021/1/6 15:49
 */
class VerticalRepeatLayoutManager : AbsRepeatLayoutManager() {
    override fun isHorizontal() = false
    override fun canScrollVertically() = true
    override fun layoutChildren(recycler: RecyclerView.Recycler) {
        var paddingTop = paddingTop
        var i = 0
        while (true) {
            if (paddingTop > height - paddingBottom) break
            val itemView = recycler.getViewForPosition(i % itemCount)
            addView(itemView)
            measureChildWithMargins(itemView, 0, 0)
            val paddingLeft = paddingLeft
            val bottom = paddingTop + getDecoratedMeasuredHeight(itemView)
            val right = paddingLeft + getDecoratedMeasuredWidth(itemView)
            layoutDecorated(itemView, paddingLeft, paddingTop, right, bottom)
            paddingTop = bottom
            i++
        }
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        fillVertically(recycler, dy > 0)
        offsetChildrenHorizontal(-dy)
        recycleChildView(dy > 0, recycler)
        return dy
    }

    private fun fillVertically(recycler: RecyclerView.Recycler, fillLast: Boolean) {
        if (childCount == 0) return
        if (fillLast) fillVerticallyLast(recycler) else fillVerticallyFirst(recycler)
    }

    private fun fillVerticallyFirst(recycler: RecyclerView.Recycler) {
        var anchorView = getChildAt(0)!!
        val anchorPosition = getPosition(anchorView)
        while (anchorView.top > paddingTop) {
            var position = (anchorPosition - 1) % itemCount
            if (position < 0) position += itemCount
            val scrapItem = recycler.getViewForPosition(position)
            addView(scrapItem, 0)
            measureChildWithMargins(scrapItem, 0, 0)
            val left = paddingLeft
            val right = left + getDecoratedMeasuredWidth(scrapItem)
            val bottom = anchorView.top
            val top = bottom - getDecoratedMeasuredHeight(scrapItem)
            layoutDecorated(
                scrapItem, left, top,
                right, bottom
            )
            anchorView = scrapItem
        }
    }

    private fun fillVerticallyLast(recycler: RecyclerView.Recycler) {
        var anchorView = getChildAt(childCount - 1)!!
        val anchorPosition = getPosition(anchorView)
        while (anchorView.bottom < height - paddingBottom) {
            var position = (anchorPosition + 1) % itemCount
            if (position < 0) position += itemCount
            val scrapItem = recycler.getViewForPosition(position)
            addView(scrapItem)
            measureChildWithMargins(scrapItem, 0, 0)
            val left = paddingLeft
            val top = anchorView.bottom
            val right = left + getDecoratedMeasuredWidth(scrapItem)
            val bottom = top + getDecoratedMeasuredHeight(scrapItem)
            layoutDecorated(scrapItem, left, top, right, bottom)
            anchorView = scrapItem
        }
    }
}