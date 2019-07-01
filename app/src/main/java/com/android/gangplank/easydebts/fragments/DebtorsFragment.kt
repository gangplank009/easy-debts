package com.android.gangplank.easydebts.fragments


import android.os.Bundle
import android.view.*
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
import com.android.gangplank.easydebts.room.entities.Debtor
import com.android.gangplank.easydebts.views.DebtorsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        val simpleItemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if ((direction == ItemTouchHelper.LEFT) or (direction == ItemTouchHelper.RIGHT)) {
                    val swipedDebtor = rvAdapter.getDebtorAt(position)
                    sharedViewModel.deleteDebtor(swipedDebtor)
                //    rvAdapter.notifyItemRemoved(position)
                //    rvAdapter.notifyItemRangeChanged(position, rvAdapter.itemCount)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val fab = view.findViewById<FloatingActionButton>(R.id.add_debtor_fab)
        fab.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(
                DebtorsFragmentDirections.actionDebtorsFragmentToAddEditDebtorFragment())

        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProviders.of(activity!!).get(DebtorsViewModel::class.java)
        sharedViewModel.allDebtors.observe(this, Observer { debtors: List<Debtor> ->
            debtors.let { rvAdapter.setDebtors(it) }
        })
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
