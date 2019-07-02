package com.android.gangplank.easydebts.views

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.room.entities.Debtor

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView,
                                      private val listener: OnItemCLickListener?) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    interface OnItemCLickListener {
        fun onItemClick(debtor: Debtor, positionInAdapter: Int)

        fun onItemLongClick(debtor: Debtor, positionInAdapter: Int)
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
                        val debtor = listAdapter.getDebtorAt(positionInAdapter)
                        listener.onItemLongClick(debtor, positionInAdapter)
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
            val debtor = listAdapter.getDebtorAt(positionInAdapter)
            listener.onItemClick(debtor, positionInAdapter)
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}