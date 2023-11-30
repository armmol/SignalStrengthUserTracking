package com.example.test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.test.MainActivity
import com.example.test.model.VartotojaiMAC
import com.example.test.R
import com.example.test.viewmodel.DatabaseViewModel

class VartotojaiAdapter(
    private val items: List<VartotojaiMAC>,
    private val onClick: (user : VartotojaiMAC) -> Unit
) : RecyclerView.Adapter<VartotojaiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val position: TextView = view.findViewById(R.id.textView5)
        val macAddress: TextView = view.findViewById(R.id.textView1)
        val s1: TextView = view.findViewById(R.id.textView2)
        val s2: TextView = view.findViewById(R.id.textView3)
        val s3: TextView = view.findViewById(R.id.textView4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vartotojai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.setOnClickListener() {
            onClick(items[position])
            findNavController(holder.itemView).navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        holder.position.text = "${position + 1}"
        holder.macAddress.text = item.macAddress
        holder.s1.text = "S1 - ${item.s1}"
        holder.s2.text = "S2 - ${item.s2}"
        holder.s3.text = "S3 - ${item.s3}"
    }

    override fun getItemCount() = items.size
}
