package com.android.gangplank.easydebts.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debt
import com.android.gangplank.easydebts.room.entities.Debtor
import java.text.SimpleDateFormat
import java.util.*

class DebtsAdapter: ListAdapter<Debt, DebtsAdapter.DebtsHolder>(DebtDiffCallback()) {

    inner class DebtsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val debtValueTv: TextView = itemView.findViewById(R.id.debt_item_value_label)
        val debtStatusSwitch: Switch = itemView.findViewById(R.id.debt_item_status_switch)
        val debtDateTv: TextView = itemView.findViewById(R.id.debt_item_start_date_label)
        val debtSmsBtn = itemView.findViewById<ImageButton>(R.id.debt_item_sms_img_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.debt_item, parent, false)
        return DebtsHolder(itemView)
    }

    override fun onBindViewHolder(holder: DebtsHolder, position: Int) {
        val debt = getItem(position)
        holder.apply {
            this.debtValueTv.text = debt.value.toString()
            this.debtStatusSwitch.isChecked = debt.state
            this.debtDateTv.text = debt.startDate
        }
    }

   fun getDebtAt(position: Int): Debt {
       return getItem(position)
   }
}

class DebtDiffCallback : DiffUtil.ItemCallback<Debt>() {
    override fun areItemsTheSame(oldItem: Debt, newItem: Debt): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Debt, newItem: Debt): Boolean {
        if (oldItem.value != newItem.value)
            return false
        if (oldItem.startDate.compareTo(newItem.startDate) != 0)
            return false
        if (oldItem.state != newItem.state)
            return false
        return true
    }
}