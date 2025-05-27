package com.example.ship.Fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import com.bumptech.glide.Glide
import com.example.ship.Activity.LoginActivity
import com.example.ship.R
import com.example.ship.ViewModel.ProfileViewModel
import com.example.ship.databinding.FragmentProfileBinding
import kotlin.toString

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val myViewModel: ProfileViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private lateinit var spId: String
    private var isEditing = false
    private var selectedImageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shareRef =
            requireActivity().getSharedPreferences("id_shiper", AppCompatActivity.MODE_PRIVATE)
        spId = shareRef.getString("id_shiper", null).toString()

        if (spId.isNotEmpty()) {
            myViewModel.loadInfo(spId)
        }
        myViewModel.spInfo.observe(viewLifecycleOwner) {
            binding.apply {
                edtName.setText(it.name)
                edtAddress.setText(it.address)
                edtSDT.setText(it.sdt)
                edtPassword.setText(it.pass)
                edtGmail.setText(it.email)
                edtBirth.setText(it.date)
                if (it.src.isNotEmpty()){
                    Glide.with(imgAvt).load(it.src).into(imgAvt)
                }
                binding.layout.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }
        }
        binding.btnFix.setOnClickListener {
            if (isEditing) {
                clickFinish()
            } else {
                clickFix()
            }
        }
        binding.imgAvt.setOnClickListener {
            if (isEditing) {
                openImagePicker()
            }
        }
        binding.imvTime.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    binding.edtBirth.setText("$dayOfMonth/${month + 1}/$year")
                },
                2025,
                0,
                1
            ).show()
        }
        binding.btnOut.setOnClickListener {
            val share = requireActivity().getSharedPreferences("id_shiper", MODE_PRIVATE)
            share.edit().remove("id_shiper").apply()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                if (imageUri != null) {
                    selectedImageUri = imageUri
                    binding.imgAvt.setImageURI(imageUri)

                }
            }
        }

    private fun clickFix() {
        val editTexts = listOf(
            binding.edtName,
            binding.edtSDT,
            binding.edtAddress,
            binding.edtPassword,
        )
        for (edt in editTexts) {
            edt.isEnabled = true
            edt.background = ContextCompat.getDrawable(requireContext(), R.drawable.edt_info)
        }
        binding.imvTime.visibility = View.VISIBLE
        isEditing = true
        binding.btnFix.setText("Xong")
    }

    private fun clickFinish() {
        val editTexts = listOf(
            binding.edtName,
            binding.edtSDT,
            binding.edtAddress,
            binding.edtPassword,
        )
        for (edt in editTexts) {
            edt.isEnabled = false
            edt.background = null
        }
        var name = binding.edtName.text.toString()
        var sdt = binding.edtSDT.text.toString()
        var address = binding.edtAddress.text.toString()
        var password = binding.edtPassword.text.toString()
        var birth = binding.edtBirth.text.toString()
        if (selectedImageUri == null) {
            myViewModel.update(spId, name, sdt, address, password, birth)
        } else {
            myViewModel.update(spId, name, sdt, address, password, birth, selectedImageUri!!)
        }
        binding.imvTime.visibility = View.INVISIBLE
        isEditing = false
        binding.btnFix.setText("Sửa thông tin")
    }
}