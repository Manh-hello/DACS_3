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
import com.example.project.Activity.ChatActivity
import com.example.project.Adapter.PersonChatAdapter
import com.example.project.Interface.ClickCUS
import com.example.project.Model.Entity.Admin
import com.example.project.Model.Person
import com.example.project.ViewModel.ChatViewModel
import com.example.project.databinding.FragmentChatBinding

class Chat_Fragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter:PersonChatAdapter
    private val myViewModel: ChatViewModel by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private lateinit var cusId: String
    private var listS = mutableListOf<Admin>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref =
            requireContext().getSharedPreferences("id_customers", AppCompatActivity.MODE_PRIVATE)
        cusId = sharedPref.getString("id_customer", null).toString()

        myViewModel.loadListStore(cusId)
        myViewModel.listStore.observe(viewLifecycleOwner){
            listS.clear()
            listS.addAll(it)
            adapter.notifyDataSetChanged()
        }
        adapter = PersonChatAdapter(listS, object : ClickCUS{
            override fun click(i: Int) {
                val inten = Intent(requireActivity(), ChatActivity::class.java)
                inten.putExtra("storeId",listS[i].id)
                startActivity(inten)
            }

        })
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewChat.adapter = adapter

    }
}