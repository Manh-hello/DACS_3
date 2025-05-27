package com.example.projectmanage.Fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanage.Activity.ChatActivity
import com.example.projectmanage.Adapter.ListCusAdapter
import com.example.projectmanage.Interface.ClickCUS
import com.example.projectmanage.Model.Entity.Customer
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.ChatViewModel
import com.example.projectmanage.databinding.FragmentChatBinding
import kotlin.getValue

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val myViewModel : ChatViewModel by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private var listCus = mutableListOf<Customer>()
    private lateinit var adapter: ListCusAdapter
    private lateinit var storeId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeId = requireActivity().getSharedPreferences("id_store", MODE_PRIVATE)
            .getString("id_store", null).orEmpty()
        adapter = ListCusAdapter(listCus,object : ClickCUS{
            override fun click(i: Int) {
                val inten = Intent(requireActivity(), ChatActivity::class.java)
                inten.putExtra("cusId",listCus[i].id)
                startActivity(inten)
            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireActivity())
        binding.rv.adapter = adapter
        myViewModel.loadList(storeId)
        myViewModel.listCus.observe(viewLifecycleOwner){
            listCus.clear()
            listCus.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }
}