package com.android.gangplank.easydebts.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.gangplank.easydebts.DebtorsViewModel
import com.android.gangplank.easydebts.R
import com.android.gangplank.easydebts.room.entities.Debtor
import com.android.gangplank.easydebts.views.DebtorsAdapter

class DebtorsFragment : Fragment() {

    private lateinit var sharedViewModel: DebtorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_debtors, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.debtor_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)
        val adapter = DebtorsAdapter()
        recyclerView.adapter = adapter

        sharedViewModel = ViewModelProviders.of(activity!!).get(DebtorsViewModel::class.java)
        sharedViewModel.allDebtors.observe(this, Observer { debtors: List<Debtor> ->
            debtors.let { adapter.setDebtors(it) }
        })
        return view
    }


}
