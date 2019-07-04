package com.android.gangplank.easydebts.views

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
class RecyclerItemClickListener<T>(
    context: Context, recyclerView: RecyclerView,
    private val listener: OnItemClickListener<T>?
) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector

    interface OnItemClickListener<T> {
        fun onItemClick(item: T, positionInAdapter: Int)

        fun onItemLongClick(item: T, positionInAdapter: Int)
    }

    init {
        gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent?) {
                    e ?: return
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && listener != null) {
                        val positionInAdapter = recyclerView.getChildAdapterPosition(childView)
                        val listAdapter = recyclerView.adapter
                        when (listAdapter) {
                            is DebtorsAdapter -> {
                                val item = listAdapter.getDebtorAt(positionInAdapter)
                                listener.onItemLongClick(item as T, positionInAdapter)
                            }
                            is DebtsAdapter -> {
                                val item = listAdapter.getDebtAt(positionInAdapter)
                                listener.onItemLongClick(item as T, positionInAdapter)
                            }
                            else -> Toast.makeText(context, "Invalid rv adapter", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInterceptTouchEvent(recyclerView: RecyclerView, e: MotionEvent): Boolean {
        val childView = recyclerView.findChildViewUnder(e.x, e.y)
        if (childView != null && listener != null && gestureDetector.onTouchEvent(e)) {
            val listAdapter = recyclerView.adapter
            val positionInAdapter = recyclerView.getChildAdapterPosition(childView)
            when (listAdapter) {
                is DebtorsAdapter -> {
                    val item = listAdapter.getDebtorAt(positionInAdapter)
                    listener.onItemClick(item as T, positionInAdapter)
                    return true
                }
                is DebtsAdapter -> {
                    val item = listAdapter.getDebtAt(positionInAdapter)
                    listener.onItemClick(item as T, positionInAdapter)
                    return true
                }
                else -> Toast.makeText(recyclerView.context, "Invalid rv adapter click", Toast.LENGTH_SHORT).show()
            }
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}