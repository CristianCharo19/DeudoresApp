package com.davidcharo.deudoresapp.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.davidcharo.deudoresapp.DeudoresApp
import com.davidcharo.deudoresapp.data.local.dao.DebtorDao
import com.davidcharo.deudoresapp.data.local.entities.Debtor
import com.davidcharo.deudoresapp.data.server.DebtorServer
import com.davidcharo.deudoresapp.databinding.FragmentCreateBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Types.NULL

class CreateFragment : Fragment() {

    private lateinit var createViewModel: CreateViewModel
    private var _binding: FragmentCreateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewModel =
            ViewModelProvider(this).get(CreateViewModel::class.java)

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
        createViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })

        with(binding){
            createButton.setOnClickListener{
                val name = nameEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val amount = amountEditText.text.toString().toLong()

                //createDebtor(name, phone, amount)

                createDebtorServer(name, phone, amount)
            }
        }

        return root
    }

    private fun createDebtorServer(name: String, phone: String, amount: Long) {
        val db = Firebase.firestore
        val document = db.collection("deudores").document()
        val id = document.id
        val debtorServer = DebtorServer(id, name, amount, phone)
        db.collection("deudores").document(id).set(debtorServer)
        cleanViews()
    }

    private fun createDebtor(name: String, phone: String, amount: Long) {
        val debtor = Debtor(id = NULL, name = name, phone = phone, amount = amount)
        val debtorDAO : DebtorDao = DeudoresApp.database.DebtorDao()
        debtorDAO.createDebtor(debtor)
        cleanViews()

    }

    private fun cleanViews() {
        with(binding){
            nameEditText.setText("")
            phoneEditText.setText("")
            amountEditText.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}