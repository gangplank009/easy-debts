package com.android.gangplank.easydebts.views

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debtor

class DebtorsAdapter: ListAdapter<Debtor, DebtorsAdapter.DebtorsHolder>(DebtorDiffCallback()) {

    inner class DebtorsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val debtorImage = itemView.findViewById<ImageView>(R.id.debtor_image_view)
        val debtorName = itemView.findViewById<TextView>(R.id.debtor_name_text)
        val debtorPhoneNumber = itemView.findViewById<TextView>(R.id.debtor_phone_number_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtorsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.debtor_item, parent, false)
        return DebtorsHolder(itemView)
    }


    override fun onBindViewHolder(holder: DebtorsHolder, position: Int) {
        val debtor = getItem(position)
        holder.apply {
            this.debtorName.text = debtor.name
            this.debtorPhoneNumber.text = debtor.telNumber
        }
    }


    fun getDebtorAt(position: Int): Debtor {
        return getItem(position)
    }
}

class DebtorDiffCallback : DiffUtil.ItemCallback<Debtor>() {
    override fun areItemsTheSame(oldItem: Debtor, newItem: Debtor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Debtor, newItem: Debtor): Boolean {
        if (oldItem.name != newItem.name)
            return false
        if (oldItem.telNumber != newItem.telNumber)
            return false
        if (!oldItem.avatar?.contentEquals(newItem.avatar!!)!!)
            return false
        return true
    }
}

