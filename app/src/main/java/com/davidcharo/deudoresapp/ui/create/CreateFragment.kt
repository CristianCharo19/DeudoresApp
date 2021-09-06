package com.davidcharo.deudoresapp.ui.create

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.davidcharo.deudoresapp.DeudoresApp
import com.davidcharo.deudoresapp.data.local.dao.DebtorDao
import com.davidcharo.deudoresapp.data.local.entities.Debtor
import com.davidcharo.deudoresapp.data.server.DebtorServer
import com.davidcharo.deudoresapp.databinding.FragmentCreateBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.sql.Types.NULL

class CreateFragment : Fragment() {

    private lateinit var createViewModel: CreateViewModel
    private var _binding: FragmentCreateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var urlImage: String? = null
    private val REQUEST_IMAGE_CAPTURE = 1000

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.takePictureImageView.setImageBitmap(imageBitmap)
        }
    }

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
            takePictureImageView.setOnClickListener {
                dispatchTakePictureIntent()
            }
            createButton.setOnClickListener{
                saveDebtor()
            }
        }

        return root
    }

    private fun saveDebtor() {
        val db = Firebase.firestore
        val document = db.collection("deudores").document()
        val id = document.id

        val storage = FirebaseStorage.getInstance()
        val pictureRef = storage.reference.child("debtors").child(id)

        binding.takePictureImageView.isDrawingCacheEnabled = true
        binding.takePictureImageView.buildDrawingCache()
        val bitmap = (binding.takePictureImageView.drawable as BitmapDrawable).bitmap
        val baos =ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = pictureRef.putBytes(data)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful){
                task.exception?.let{
                    throw it
                }
            }
            pictureRef.downloadUrl
        }.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val urlPicture = task.result.toString()
                with(binding) {
                    val name = nameEditText.text.toString()
                    val phone = phoneEditText.text.toString()
                    val amount = amountEditText.text.toString().toLong()
                    val debtorServer = DebtorServer(id, name, amount, phone, urlPicture)
                    db.collection("deudores").document(id).set(debtorServer)
                    cleanViews()
                }
            } else {
                // Handle failures
                // ...
            }
        }

    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
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