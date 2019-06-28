package com.android.gangplank.easydebts.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debtor

class DebtorsAdapter: RecyclerView.Adapter<DebtorsAdapter.DebtorsHolder>() {
    private var allDebtors: List<Debtor>? = null

    inner class DebtorsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val debtorImage = itemView.findViewById<ImageView>(R.id.debtor_image_view)
        val debtorName = itemView.findViewById<TextView>(R.id.debtor_name_text)
        val debtorPhoneNumber = itemView.findViewById<TextView>(R.id.debtor_phone_number_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtorsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.debtor_item, parent, false)
        return DebtorsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allDebtors?.size ?: 0
    }

    override fun onBindViewHolder(holder: DebtorsHolder, position: Int) {
        val debtor = allDebtors!![position]
        holder.apply {
            this.debtorName.text = debtor.name
            this.debtorPhoneNumber.text = debtor.telNumber
        }
    }

    fun setDebtors(debtors: List<Debtor>) {
        allDebtors = debtors
        notifyDataSetChanged()
    }
}