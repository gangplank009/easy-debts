package com.android.gangplank.easydebts.views

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
class RecyclerItemSwipeCallback<T>(
    private val recyclerView: RecyclerView,
    private val listener: OnItemSwipeListener<T>?,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    interface OnItemSwipeListener<T> {
        fun onItemSwipe(item: T, positionInAdapter: Int)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if ((direction == ItemTouchHelper.LEFT) or (direction == ItemTouchHelper.RIGHT)) {
            val adapterPosition = viewHolder.adapterPosition
            val adapter = recyclerView.adapter as DebtorsAdapter
            val debtor = adapter.getDebtorAt(adapterPosition)
            listener?.onItemSwipe(debtor as T, adapterPosition)
        }
    }
}