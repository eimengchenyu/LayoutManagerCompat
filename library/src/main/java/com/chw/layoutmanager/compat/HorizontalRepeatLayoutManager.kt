package com.chw.layoutmanager.compat

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * @author hanwei.chen
 * 2021/1/6 15:45
 */
class HorizontalRepeatLayoutManager : AbsRepeatLayoutManager() {
    override fun isHorizontal() = true
    override fun canScrollHorizontally() = true
    override fun layoutChildren(recycler: RecyclerView.Recycler) {
        var paddingLeft = paddingLeft
        var i = 0
        while (true) {
            if (paddingLeft > width - paddingRight) break
            val itemView = recycler.getViewForPosition(i % itemCount)
            addView(itemView)
            measureChildWithMargins(itemView, 0, 0)
            val paddingTop = paddingTop
            val right = paddingLeft + getDecoratedMeasuredWidth(itemView)
            val bottom = paddingTop + getDecoratedMeasuredHeight(itemView)
            layoutDecorated(itemView, paddingLeft, paddingTop, right, bottom)
            paddingLeft = right
            i++
        }
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        fillHorizontal(recycler, dx > 0)
        offsetChildrenHorizontal(-dx)
        recycleChildView(dx > 0, recycler)
        return dx
    }

    private fun fillHorizontal(recycler: Recycler, fillLast: Boolean) {
        if (childCount == 0) return
        if (fillLast) fillHorizontalLast(recycler) else fillHorizontalFirst(recycler)
    }

    private fun fillHorizontalFirst(recycler: RecyclerView.Recycler) {
        var anchorView = getChildAt(0)!!
        val anchorPosition = getPosition(anchorView)
        while (anchorView.left > paddingLeft) {
            var position = (anchorPosition - 1) % itemCount
            if (position < 0) position += itemCount
            val scrapItem = recycler.getViewForPosition(position)
            addView(scrapItem, 0)
            measureChildWithMargins(scrapItem, 0, 0)
            val right = anchorView.left
            val top = paddingTop
            val left = right - getDecoratedMeasuredWidth(scrapItem)
            val bottom = top + getDecoratedMeasuredHeight(scrapItem)
            layoutDecorated(scrapItem, left, top, right, bottom)
            anchorView = scrapItem
        }
    }

    private fun fillHorizontalLast(recycler: RecyclerView.Recycler) {
        var anchorView = getChildAt(childCount - 1)!!
        val anchorPosition = getPosition(anchorView)
        while (anchorView.right < width - paddingRight) {
            var position = (anchorPosition + 1) % itemCount
            if (position < 0) position += itemCount
            val scrapItem = recycler.getViewForPosition(position)
            addView(scrapItem)
            measureChildWithMargins(scrapItem, 0, 0)
            val left = anchorView.right
            val top = paddingTop
            val right = left + getDecoratedMeasuredWidth(scrapItem)
            val bottom = top + getDecoratedMeasuredHeight(scrapItem)
            layoutDecorated(scrapItem, left, top, right, bottom)
            anchorView = scrapItem
        }
    }
}