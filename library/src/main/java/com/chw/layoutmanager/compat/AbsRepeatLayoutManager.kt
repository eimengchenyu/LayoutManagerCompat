package com.chw.layoutmanager.compat

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * @author hanwei.chen
 * 2021/1/6 15:18
 */
abstract class AbsRepeatLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams() = RecyclerView.LayoutParams(
        RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT
    )

    override fun isAutoMeasureEnabled() = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount <= 0 || state.isPreLayout) return
        detachAndScrapAttachedViews(recycler)
        layoutChildren(recycler)
    }

    abstract fun layoutChildren(recycler: RecyclerView.Recycler)

    abstract fun isHorizontal(): Boolean

    protected fun recycleChildView(fillLast: Boolean, recycler: Recycler) {
        if (fillLast) {
            var i = 0
            while (true) {
                val view = getChildAt(i) ?: return
                val needRecycler =
                    if (isHorizontal()) view.right < paddingLeft else view.bottom < paddingTop
                if (needRecycler) removeAndRecycleView(view, recycler) else return
                i++
            }
        } else {
            var i = childCount - 1
            while (true) {
                val view = getChildAt(i) ?: return
                val needRecycler =
                    if (isHorizontal()) view.left > width - paddingRight else view.top > height - paddingBottom
                if (needRecycler) removeAndRecycleView(view, recycler) else return
                i--
            }
        }
    }
}