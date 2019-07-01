package com.android.gangplank.easydebts.fragments


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.gangplank.easydebts.DebtorsViewModel
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debtor

class AddEditDebtorFragment : Fragment() {

    private lateinit var sharedViewModel: DebtorsViewModel
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_edit_debtor, container, false)
        setHasOptionsMenu(true)

        nameEditText = view.findViewById(R.id.debtor_name_text)
        phoneEditText = view.findViewById(R.id.debtor_phone_number_text)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = ViewModelProviders.of(activity!!).get(DebtorsViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add_edit_debtor_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.add_edit_menu_item -> {
                if (validateNameAndPhone(nameEditText.text, phoneEditText.text)) {
                    val name = nameEditText.text.toString()
                    val phone = phoneEditText.text.toString()
                    val newDebtor = Debtor(name, phone)
                    sharedViewModel.insertDebtor(newDebtor)
                    Toast.makeText(context, "Debtor added/edited", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Fill all text fields", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else -> return false
        }
    }

    private fun validateNameAndPhone(name: CharSequence?, phone: CharSequence?): Boolean {
        if (TextUtils.isEmpty(name?.toString() ?: "") || TextUtils.isEmpty(phone?.toString() ?: ""))
            return false
        return true
    }
}
