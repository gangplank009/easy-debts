package com.android.gangplank.easydebts.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.DebtorsViewModel
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debt
import com.android.gangplank.easydebts.room.entities.Debtor
import com.android.gangplank.easydebts.views.DebtorsAdapter
import com.android.gangplank.easydebts.views.DebtsAdapter
import com.android.gangplank.easydebts.views.RecyclerItemClickListener
import com.android.gangplank.easydebts.views.RecyclerItemSwipeCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class DebtsFragment : Fragment() {

    private lateinit var sharedViewModel: DebtorsViewModel
    private lateinit var rvAdapter: DebtsAdapter

    private lateinit var debtorNameTv: TextView
    private lateinit var debtorPhoneTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_debts, container, false)
        setHasOptionsMenu(true)

        debtorNameTv = view.findViewById(R.id.debt_debtor_name_label)
        debtorPhoneTv = view.findViewById(R.id.debt_debtor_phone_number_label)

        val callDebtorBtn: MaterialButton = view.findViewById(R.id.debt_call_btn)
        callDebtorBtn.setOnClickListener { view: View ->
            val callIntent = Intent()
            callIntent.apply {
                val tel = sharedViewModel.clickedDebtor.value?.telNumber
                this.action = Intent.ACTION_DIAL
                this.data = Uri.parse("tel:$tel")
            }
            startActivity(callIntent)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.debt_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)
        rvAdapter = DebtsAdapter()
        recyclerView.adapter = rvAdapter
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this.context!!, recyclerView,
            object : RecyclerItemClickListener.OnItemClickListener<Debt>{
                override fun onItemClick(item: Debt, view: View, positionInAdapter: Int) {
                    val smsButton: MaterialButton? = view.findViewById(R.id.debt_item_sms_img_btn)
                    smsButton?.setOnClickListener { view: View ->
                        val smsIntent = Intent()
                        smsIntent.apply {
                            val tel = sharedViewModel.clickedDebtor.value?.telNumber
                            val debtValue = item.value

                            this.action = Intent.ACTION_SENDTO
                            this.data = Uri.parse("smsto:$tel")
                            this.putExtra("sms_body", "Your debt is $debtValue")
                        }
                        startActivity(smsIntent)
                    }

                    val statusSwitch: Switch? = view.findViewById(R.id.debt_item_status_switch)
                    statusSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
                        item.state = isChecked
                        sharedViewModel.updateDebt(item)
                    }

                    Toast.makeText(context, "Click. Position = $positionInAdapter", Toast.LENGTH_SHORT).show()
                }

                override fun onItemLongClick(item: Debt, view: View, positionInAdapter: Int) {
                    Toast.makeText(context, "Long click. Position = $positionInAdapter", Toast.LENGTH_SHORT).show()
                    sharedViewModel.clickedDebt.value = item
                    view.findNavController().navigate(DebtsFragmentDirections.actionDebtsFragmentToAddEditDebtFragment())
                }

            }))
        val itemSwipeHelper = ItemTouchHelper(RecyclerItemSwipeCallback(recyclerView,
            object : RecyclerItemSwipeCallback.OnItemSwipeListener<Debt> {
            override fun onItemSwipe(item: Debt, positionInAdapter: Int) {
                sharedViewModel.deleteDebt(item)
                Snackbar.make(view, "${item.value} deleted. Undo delete?", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        sharedViewModel.insertDebt(item)
                    }
                    .show()
            }
        }, 0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT))
        itemSwipeHelper.attachToRecyclerView(recyclerView)

        val addDebtFab = view.findViewById<FloatingActionButton>(R.id.add_debt_fab)
        addDebtFab.setOnClickListener { view: View ->
            view.findNavController().navigate(DebtsFragmentDirections.actionDebtsFragmentToAddEditDebtFragment())
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProviders.of(activity!!).get(DebtorsViewModel::class.java)
        sharedViewModel.allDebts.observe(this, Observer { debts: List<Debt> ->
            val debtsForDebtor: List<Debt> = debts.filter { debt: Debt ->
                debt.debtorId == sharedViewModel.clickedDebtor.value?.id
            }
            debtsForDebtor.let {
                rvAdapter.submitList(it)
            }
        })

        sharedViewModel.clickedDebtor.observe(this, Observer { debtor: Debtor? ->
            debtorNameTv.text = debtor?.name
            debtorPhoneTv.text = debtor?.telNumber
        })
    }

    override fun onDestroyView() {
        if (sharedViewModel.clickedDebtor.hasObservers())
            sharedViewModel.clickedDebtor.removeObservers(this)
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.debtors_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete_all_debtors -> {
                sharedViewModel.deleteAllDebts()
                return true
            }
            else -> return false
        }
    }
}
