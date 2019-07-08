package com.android.gangplank.easydebts.fragments


import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.gangplank.easydebts.DebtorsViewModel
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debt
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.util.*

class AddEditDebtFragment : Fragment() {

    private var debtorId: Long = -1
    private var editingDebt = false
    private lateinit var editDebt: Debt
    private lateinit var sharedViewModel: DebtorsViewModel
    private lateinit var valueEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var debtSaveState: MaterialButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_add_edit_debt, container, false)
        valueEditText = view.findViewById(R.id.debt_value_edit_text)
        datePicker = view.findViewById(R.id.debt_datepicker)
        debtSaveState = view.findViewById(R.id.debt_save_state)
        debtSaveState.visibility = View.GONE

        val currentDate = LocalDate.now()
        datePicker.updateDate(currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = ViewModelProviders.of(activity!!).get(DebtorsViewModel::class.java)
        sharedViewModel.clickedDebt.observe(this, Observer { debt: Debt? ->
            if (debt != null) {
                valueEditText.setText(debt.value.toString())
                val startDate = LocalDate.parse(debt.startDate)
                val year = startDate.year
                val month = startDate.monthValue
                val day = startDate.dayOfMonth
                datePicker.updateDate(year, month - 1, day)
                editingDebt = true
                editDebt = debt
            }
        })
        debtorId = sharedViewModel.clickedDebtor.value?.id ?: -1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.clickedDebt.value = null
        if (sharedViewModel.clickedDebt.hasObservers())
            sharedViewModel.clickedDebt.removeObservers(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add_edit_debtor_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_edit_menu_item -> {
                if (validateValue(valueEditText.text)) {
                    val value = valueEditText.text.toString().toLong()
                    val date = getStringFromDatepicker(datePicker)

                    if (editingDebt) {
                        editDebt.apply {
                            this.value = value

                            this.startDate = date
                        }
                        sharedViewModel.updateDebt(editDebt)
                        debtSaveState.apply {
                            this.icon = resources.getDrawable(R.drawable.ic_update, null)
                            this.text = "Updated"
                            this.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.updateDebtDebtor))
                            this.visibility = View.VISIBLE
                        }
                        Toast.makeText(context, "Debt edited", Toast.LENGTH_SHORT).show()
                    } else {
                        val newDebt = Debt(debtorId, value, date, false)
                        sharedViewModel.insertDebt(newDebt)
                        debtSaveState.apply {
                            this.icon = resources.getDrawable(R.drawable.ic_add, null)
                            this.text = "Added"
                            this.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.addDebtDebtor))
                            this.visibility = View.VISIBLE
                        }
                        Toast.makeText(context, "Debt added", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Fill all text fields correctly", Toast.LENGTH_SHORT).show()
                }
                return true
            }//
            else -> return false
        }
    }

    private fun getStringFromDatepicker(datePicker: DatePicker): String {
        val year = datePicker.year
        val month = datePicker.month + 1
        val day = datePicker.dayOfMonth

        val stringBuilder = StringBuilder()
        stringBuilder.append(year)
            .append("-")
        if (month < 10)
            stringBuilder.append("0")
        stringBuilder.append(month)
        stringBuilder.append("-")
        if (day < 10)
            stringBuilder.append("0")
        stringBuilder.append(day)

        return stringBuilder.toString()
    }

    private fun validateValue(value: CharSequence?): Boolean {
        if (TextUtils.isEmpty(value?.toString() ?: ""))
            return false
        try {
            value.toString().toLong()
        } catch (exception: TypeCastException) {
            return false
        }
        return true
    }
}
