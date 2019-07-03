package com.android.gangplank.easydebts.views

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.room.entities.Debtor

@Suppress("UNCHECKED_CAST")
class RecyclerItemClickListener <T> (context: Context, recyclerView: RecyclerView,
                                   private val listener: OnItemClickListener <T>?) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    interface OnItemClickListener <T> {
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
                        val listAdapter = recyclerView.adapter as DebtorsAdapter
                        val positionInAdapter = recyclerView.getChildAdapterPosition(childView)
                        val item  = listAdapter.getDebtorAt(positionInAdapter)
                        listener.onItemLongClick(item as T, positionInAdapter)
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
            val listAdapter = recyclerView.adapter as DebtorsAdapter
            val positionInAdapter = recyclerView.getChildAdapterPosition(childView)
            val item = listAdapter.getDebtorAt(positionInAdapter)
            listener.onItemClick(item as T, positionInAdapter)
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}