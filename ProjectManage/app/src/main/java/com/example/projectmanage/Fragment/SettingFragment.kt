package com.example.projectmanage.Fragment

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.projectmanage.Activity.Login
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.SettingViewModel
import com.example.projectmanage.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val myViewModel: SettingViewModel by viewModels()
    private var isEdit = false
    private var selectedImageUri: Uri? = null
    private lateinit var storeId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         storeId = requireActivity().getSharedPreferences("id_store", MODE_PRIVATE)
            .getString("id_store", null).orEmpty()

        myViewModel.loadData(storeId)
        myViewModel.info.observe (viewLifecycleOwner ){info->
            binding.apply {
                edtName.setText(info?.name?.toString() ?: "")
                edtAddress.setText(info?.address?.toString() ?: "")
                edtSDT.setText(info?.sdt?.toString() ?: "")
                edtBirth.setText(info?.created?.toString() ?: "")
                edtGmail.setText(info?.email?.toString() ?: "")
                edtPassword.setText(info?.pass?.toString() ?: "")
                if (!info?.img.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(info?.img)
                        .into(binding.imgAvt)
                }

                layout.visibility = View.VISIBLE
                probar.visibility = View.GONE
            }
        }

        binding.btnFix.setOnClickListener {
            if(isEdit == false){
                clickFix()
            }else{
                clickFinish()
            }
        }

        binding.btnOut.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("id_store", MODE_PRIVATE)
            sharedPref.edit().remove("id_store").apply()

            myViewModel.logOut()

            val intent = Intent(requireActivity(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }

        binding.imgAvt.setOnClickListener {
            if (isEdit) {
                openImagePicker()
            }
        }

        myViewModel.success.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            }
        }

        myViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(requireContext(), "Lỗi: $errorMsg", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun clickFix(){
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
        isEdit = true
        binding.btnFix.setText("Xong")
    }
    fun clickFinish(){
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
        isEdit = false
        binding.btnFix.setText("Sửa thông tin")

        val name = binding.edtName.text.toString()
        val pass = binding.edtPassword.text.toString()
        val address = binding.edtAddress.text.toString()
        val sdt = binding.edtSDT.text.toString()
        if (selectedImageUri != null) {
            myViewModel.update(storeId, pass, address, sdt, name, selectedImageUri!!)
        } else {
            myViewModel.update(storeId, pass, address, sdt, name)
        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                selectedImageUri = imageUri
                binding.imgAvt.setImageURI(imageUri)

            }
        }
    }
}