package com.android.gangplank.easydebts.views

import android.widget.Toast
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
            val listAdapter = recyclerView.adapter
            when (listAdapter) {
                is DebtorsAdapter -> {
                    val item = listAdapter.getDebtorAt(adapterPosition)
                    listener?.onItemSwipe(item as T, adapterPosition)
                }
                is DebtsAdapter -> {
                    val item = listAdapter.getDebtAt(adapterPosition)
                    listener?.onItemSwipe(item as T, adapterPosition)
                }
                else -> Toast.makeText(recyclerView.context, "Invalid rv adapter swipe", Toast.LENGTH_SHORT).show()
            }
        }
    }
}