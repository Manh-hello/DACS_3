package com.example.projectmanage.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanage.Model.Entity.Chat
import com.example.projectmanage.databinding.ReceiveBinding
import com.example.projectmanage.databinding.SentBinding

class LoadMessageAdapter(private val storeId: String, private val list: MutableList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SENT = 1
        private const val TYPE_RECEIVE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].send == storeId) TYPE_SENT else TYPE_RECEIVE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SENT) {
            val binding = SentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentViewHolder(binding)
        } else {
            val binding = ReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceiveViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = list[position]
        if (holder is SentViewHolder) {
            holder.bind(chat)
        } else if (holder is ReceiveViewHolder) {
            holder.bind(chat)
        }
    }

    override fun getItemCount() = list.size

    fun updateMessages(newMessages: List<Chat>) {
        list.clear()
        list.addAll(newMessages)
        notifyDataSetChanged()
    }

    class SentViewHolder(private val binding: SentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.txtSentMessage.text = chat.message
        }
    }

    class ReceiveViewHolder(private val binding: ReceiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.txtReceiveMessage.text = chat.message
        }
    }
}
