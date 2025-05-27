package com.example.project.Fragment.TabLayout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project.Activity.Login
import com.example.project.Adapter.OrderAdapter
import com.example.project.Fragment.TabLayout.ChoXacNhanFragment
import com.example.project.Interface.ClickBTNOrder
import com.example.project.R
import com.example.project.ViewModel.OrderViewModel
import com.example.project.databinding.FormDialogInfoBinding
import com.example.project.databinding.FragmentDaNhanHangBinding
import com.example.projectmanage.Model.Entity.Order
import com.example.projectmanage.Model.Entity.OrderDetail
import com.example.project.ViewModel.observeOnce

class DaNhanHangFragment : Fragment() , ClickBTNOrder{
    private lateinit var binding: FragmentDaNhanHangBinding
    private val myViewModel: OrderViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private lateinit var cusId: String
    private lateinit var adapter: OrderAdapter
    private lateinit var dialog: AlertDialog
    private var selectedImageUri: Uri? = null
    private var imgPreview: ImageView? = null
    private var ratingValue = 0
    private var listOrder = mutableListOf<OrderDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaNhanHangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.visibility = View.VISIBLE
        binding.rv.visibility = View.INVISIBLE
        binding.tv.visibility = View.INVISIBLE
        adapter = OrderAdapter(mutableListOf(), mutableMapOf(), this@DaNhanHangFragment)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        cusId = requireActivity().getSharedPreferences("id_customers", MODE_PRIVATE)
            .getString("id_customer", null).orEmpty()

        myViewModel.loadOrder(cusId)
        myViewModel.listOrder.observe(viewLifecycleOwner) { orders ->
            val ids = orders.map { it.id }
            myViewModel.loadOrderDetailDelivered(ids)
        }
        myViewModel.mapOrderDetails.observe(viewLifecycleOwner) { map ->
            binding.progress.visibility = View.GONE

            val orders = myViewModel.listOrder.value
                .orEmpty()
                .filter { map.containsKey(it.id) }
            if (orders.isEmpty()) {
                binding.tv.visibility = View.VISIBLE
                binding.rv.visibility = View.GONE
            } else {
                binding.tv.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
                adapter.updateData(orders, map)
            }
        }
        myViewModel.success.observe(viewLifecycleOwner){
            if (it){
                myViewModel.loadOrderDetailDelivered(
                    myViewModel.listOrder.value?.map { it.id }.orEmpty()
                )
                myViewModel.resetSuccess()
                Toast.makeText(requireContext(),"đã lưu đánh giá của bạn", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun clickSuccess(id: String){
        myViewModel.updateSuccess(id)
        myViewModel.listOrder.value
            ?.map { it.id }
            ?.let { myViewModel.loadOrderDetailWait(it) }
    }
    override fun clickError(id: String)  {
        myViewModel.updateError(id)
        myViewModel.listOrder.value?.map { it.id }?.let { myViewModel.loadOrderDetailWait(it) }
    }
    override fun clickInfo(item: OrderDetail){
        val dialogBinding = FormDialogInfoBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity(), R.style.dialog)
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.setCancelable(true)

        dialogBinding.apply {
            edtName.setText(item.name)
            edtPrice.setText(item.price)
            edtQuantity.setText(item.quantity.toString())
            edtColor.setText(item.color)
            edtSize.setText(item.size)
            myViewModel.getName(item.shiper)
            myViewModel.spName.observe(viewLifecycleOwner){
                shiper.setText(it)
            }
            tvShiper.visibility = View.VISIBLE
            shiper.visibility = View.VISIBLE
            Glide.with(img.context).load(item.src).into(img)

            btnClose.setOnClickListener {
                alertDialog.dismiss()
            }
            btnCl.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        alertDialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        alertDialog.show()
    }
    @SuppressLint("MissingInflatedId")
    override fun clickEvaluate(id: String) {
        myViewModel.select(id)
        myViewModel.orDt.observeOnce(viewLifecycleOwner) { or ->
            val build = AlertDialog.Builder(requireActivity(), R.style.dialog)
            val view = layoutInflater.inflate(R.layout.form_evaluate, null)
            build.setView(view)

            val btnClose = view.findViewById<ImageButton>(R.id.btnClose)
            val btnAccess = view.findViewById<Button>(R.id.btnAccess)
            val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
            val edtEvaluate = view.findViewById<EditText>(R.id.edtEvaluate)
            val img = view.findViewById<ImageView>(R.id.imgEvaluate)
            imgPreview = img

            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                ratingValue = rating.toInt()
            }
            img.setOnClickListener {
                openImagePicker()
            }
            btnClose.setOnClickListener { dialog.dismiss() }

            btnAccess.setOnClickListener {
                val reviewText = edtEvaluate.text.toString().trim()

                if (ratingValue == 0) {
                    Toast.makeText(requireContext(), "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show()
                } else {
                    val selected = selectedImageUri
                    if (selected != null) {
                        myViewModel.evaluate(or.id, or.productId, cusId, ratingValue, reviewText, selected)
                    } else {
                        myViewModel.evaluate(or.id, or.productId, cusId, ratingValue, reviewText)
                    }
                    dialog.dismiss()
                }
            }
            dialog = build.create()
            dialog.show()
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
                imgPreview?.let { Glide.with(this).load(imageUri).into(it) }
            }
        }
    }
}