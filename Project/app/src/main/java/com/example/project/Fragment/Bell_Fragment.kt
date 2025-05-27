package com.example.project.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.Activity.CartActivity
import com.example.project.Adapter.BellAdapter
import com.example.project.Model.Entity.Notification
import com.example.project.ViewModel.BellViewModel
import com.example.project.databinding.FragmentBellBinding

class Bell_Fragment : Fragment() {
    private lateinit var binding: FragmentBellBinding
    private lateinit var adapter :BellAdapter
    private val myViewModel: BellViewModel by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private  lateinit var cusId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBellBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref =
            requireContext().getSharedPreferences("id_customers", AppCompatActivity.MODE_PRIVATE)
        cusId = sharedPref.getString("id_customer", null).toString()
        val list = mutableListOf<Notification>()
        adapter = BellAdapter(list)
        myViewModel.loadNotification(cusId = cusId)

        myViewModel.notification.observe(viewLifecycleOwner){item->
            list.clear()
            list.addAll(item)
            adapter.notifyDataSetChanged()

            binding.rvBell.visibility = View.VISIBLE
            binding.progress.visibility = View.INVISIBLE
        }
        binding.rvBell.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.rvBell.adapter = adapter

        binding.btnCart.setOnClickListener {
            startActivity(Intent(requireActivity(),CartActivity::class.java))
        }
    }
}