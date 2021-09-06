package com.davidcharo.deudoresapp.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidcharo.deudoresapp.DeudoresApp
import com.davidcharo.deudoresapp.data.local.dao.DebtorDao
import com.davidcharo.deudoresapp.data.local.entities.Debtor
import com.davidcharo.deudoresapp.data.server.DebtorServer
import com.davidcharo.deudoresapp.databinding.FragmentListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel
    private var _binding: FragmentListBinding? = null
    private lateinit var debtorAdapter: DebtorAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel =
            ViewModelProvider(this).get(ListViewModel::class.java)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //       val textView: TextView = binding.textHome
        listViewModel.text.observe(viewLifecycleOwner, Observer {
            //           textView.text = it
        })

        debtorAdapter = DebtorAdapter(onItemClicked = { onDebtorItemClicked(it) })
        binding.debtorRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = debtorAdapter
            setHasFixedSize(false)
        }

        loadFromServer()
        //loadFromLocal()

        return root
    }

    private fun loadFromServer() {
        val db = Firebase.firestore
        db.collection("deudores").get().addOnSuccessListener { result ->
            var listDebtors: MutableList<DebtorServer> = arrayListOf()
            for (document in result) {
                Log.d("nombre", document.data.toString())
                listDebtors.add(document.toObject<DebtorServer>())
            }
            debtorAdapter.appenItems(listDebtors)
        }
    }

    private fun loadFromLocal() {
        val debtorDao: DebtorDao = DeudoresApp.database.DebtorDao()
        val listDebtors: MutableList<Debtor> = debtorDao.getDebtors()
        //debtorAdapter.appenItems(listDebtors)
    }

    private fun onDebtorItemClicked(debtor: DebtorServer) {
        //findNavController().navigate(ListFragmentDirections.actionNavigationListToDetailFragment(debtor = debtor))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}