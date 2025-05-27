package com.example.project.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.project.Activity.CartActivity
import com.example.project.Activity.Login
import com.example.project.Activity.OrderActivity
import com.example.project.Model.Entity.Customer
import com.example.project.R
import com.example.project.ViewModel.ProfileViewModel
import com.example.project.databinding.FragmentProfileBinding
import java.util.Calendar

class Profile_Fragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val myViewModel: ProfileViewModel by viewModels()

    private var isEditing = false
    private var selectedImageUri: Uri? = null
    private lateinit var userId: String
    private val cal = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadId()
        loadUser()
        setUpMenu()
        setUpClick()
        obser()
    }

    private fun obser() {
        myViewModel.success.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireActivity(), "Đổi thông tin thành công!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireActivity(), "Đổi thông tin thất bại!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        myViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), "$it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun loadId() {
        val sharedPref =
            requireContext().getSharedPreferences("id_customers", AppCompatActivity.MODE_PRIVATE)
        userId = sharedPref.getString("id_customer", null).toString()

        if (userId != null) {
            myViewModel.loadUser(userId)
        }
    }

    private fun loadUser() {
        myViewModel.user.observe(viewLifecycleOwner) { customer ->
            customer?.let {
                binding.edtName.setText(it.name)
                binding.edtSDT.setText(it.sdt)
                binding.edtAddress.setText(it.address)
                binding.tvGmail.setText(it.email)
                binding.edtPassword.setText(it.password)
                binding.edtBirth.setText(it.birth)

                if (!it.srcimg.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(it.srcimg)
                        .into(binding.imgAvt)
                }

                binding.layout.visibility = View.VISIBLE
                binding.probar.visibility = View.GONE
            }
        }
    }

    private fun setUpMenu() {
        binding.btnMenu.setOnClickListener {
            if (binding.menuLayout.visibility == View.INVISIBLE) {
                binding.menuLayout.post {
                    binding.menuLayout.translationY = binding.menuLayout.height.toFloat()
                    binding.menuLayout.visibility = View.VISIBLE
                    binding.menuLayout.animate().translationY(0f).setDuration(500).withEndAction {
                        binding.btnFix.visibility = View.GONE
                        binding.dimView.visibility = View.VISIBLE
                    }.start()
                }
            } else {
                binding.btnFix.visibility = View.VISIBLE
                binding.menuLayout.animate().translationY(binding.menuLayout.height.toFloat())
                    .setDuration(500)
                    .withEndAction {
                        binding.dimView.visibility = View.GONE
                        binding.menuLayout.visibility = View.INVISIBLE
                    }.start()
            }
        }
        binding.dimView.setOnClickListener {
            binding.btnFix.visibility = View.VISIBLE
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate()
                .translationY(binding.menuLayout.height.toFloat())
                .setDuration(500)
                .withEndAction {
                    binding.menuLayout.visibility = View.INVISIBLE
                }
                .start()
        }
    }

    private fun setUpClick() {
        val logout = {
            val share = requireActivity().getSharedPreferences("id_customers", MODE_PRIVATE)
            share.edit().remove("id_customer").apply()
            val inten = Intent(requireActivity(), Login::class.java)
            inten.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(inten)
            requireActivity().finish()
        }

        binding.layoutCart.setOnClickListener {
            startActivity(Intent(requireActivity(), CartActivity::class.java))
        }
        binding.layoutOrder.setOnClickListener {
            startActivity(Intent(requireActivity(), OrderActivity::class.java))
        }
        binding.layoutOut.setOnClickListener { logout() }
        binding.btnOut.setOnClickListener { logout() }
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

    fun clickFix() {
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
        binding.edtName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.imvTime.visibility = View.VISIBLE
        isEditing = true
        binding.btnFix.setText("Xong")
    }

    fun clickFinish() {
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
        binding.edtName.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        var name = binding.edtName.text.toString()
        var sdt = binding.edtSDT.text.toString()
        var address = binding.edtAddress.text.toString()
        var password = binding.edtPassword.text.toString()
        var birth = binding.edtBirth.text.toString()
        if (selectedImageUri == null) {
            myViewModel.updateUser(userId, name, sdt, address, password, birth)
        } else {
            myViewModel.updateUser(userId, name, sdt, address, password, birth, selectedImageUri!!)
        }
        binding.imvTime.visibility = View.INVISIBLE
        isEditing = false
        binding.btnFix.setText("Sửa thông tin")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
