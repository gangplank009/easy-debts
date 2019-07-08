package com.android.gangplank.easydebts.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.DebtorsViewModel
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debtor
import com.android.gangplank.easydebts.views.DebtorsAdapter
import com.android.gangplank.easydebts.views.RecyclerItemClickListener
import com.android.gangplank.easydebts.views.RecyclerItemSwipeCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class DebtorsFragment : Fragment() {

    private lateinit var sharedViewModel: DebtorsViewModel
    private lateinit var rvAdapter: DebtorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_debtors, container, false)
        setHasOptionsMenu(true)

        val recyclerView = view.findViewById<RecyclerView>(R.id.debtor_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)
        rvAdapter = DebtorsAdapter()
        recyclerView.adapter = rvAdapter
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this.context!!, recyclerView,
            object : RecyclerItemClickListener.OnItemClickListener<Debtor> {
                override fun onItemClick(item: Debtor, view: View, positionInAdapter: Int) {
                    Toast.makeText(context, "Click. Position = $positionInAdapter", Toast.LENGTH_SHORT).show()
                    sharedViewModel.clickedDebtor.value = item
                    view.findNavController().navigate(DebtorsFragmentDirections.actionDebtorsFragmentToDebtsFragment())
                }

                override fun onItemLongClick(item: Debtor, view: View, positionInAdapter: Int) {
                    Toast.makeText(context, "Long click. Position = $positionInAdapter", Toast.LENGTH_SHORT).show()
                    sharedViewModel.clickedDebtor.value = item
                    view.findNavController().navigate(DebtorsFragmentDirections.actionDebtorsFragmentToAddEditDebtorFragment())
                }

            }))

        val itemSwipeHelper = ItemTouchHelper(RecyclerItemSwipeCallback<Debtor>(recyclerView, object :
            RecyclerItemSwipeCallback.OnItemSwipeListener<Debtor> {
            override fun onItemSwipe(item: Debtor, positionInAdapter: Int) {
                sharedViewModel.deleteDebtor(item)
                 Snackbar.make(view, "${item.name} deleted. Undo delete?", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        sharedViewModel.insertDebtor(item)
                    }
                    .show()
            }

        }, 0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT))
        itemSwipeHelper.attachToRecyclerView(recyclerView)

        val addDebtorFab = view.findViewById<FloatingActionButton>(R.id.add_debtor_fab)
        addDebtorFab.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(
                DebtorsFragmentDirections.actionDebtorsFragmentToAddEditDebtorFragment())

        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProviders.of(activity!!).get(DebtorsViewModel::class.java)
        sharedViewModel.allDebtors.observe(this, Observer { debtors: List<Debtor> ->
            debtors.let {
                rvAdapter.submitList(it)
            }
        })
        sharedViewModel.clickedDebtor.value = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.debtors_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete_all_debtors -> {
                sharedViewModel.deleteAllDebtors()
                return true
            }
            else -> return false
        }
    }
}
