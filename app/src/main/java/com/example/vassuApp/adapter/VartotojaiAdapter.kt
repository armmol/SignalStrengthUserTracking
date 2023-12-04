package com.example.vassuApp.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.vassuApp.model.VartotojaiMAC
import java.util.regex.Matcher
import java.util.regex.Pattern


class VartotojaiAdapter(
    private val items: List<VartotojaiMAC>,
    private val onClick: (user: VartotojaiMAC) -> Unit
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
        holder.itemView.setOnClickListener {
            onClick(items[position])
            findNavController(holder.itemView).navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        holder.position.text = "${position + 1}"
        holder.macAddress.text = item.macAddress
        holder.macAddress.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val text: String = holder.macAddress.text.toString()
                if (!isValidMacAddress(text)) {
                    holder.macAddress.error = "Invalid MAC Address"
                }
            }
        }
        holder.s1.text = "S1 - ${item.s1}"
        holder.s2.text = "S2 - ${item.s2}"
        holder.s3.text = "S3 - ${item.s3}"
        holder.s1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                if (inputText.isNotEmpty()) {
                    val number = inputText.toIntOrNull()
                    if (number == null || number < 1 || number > 100) {
                        holder.s1.error = "Number should be between 1 and 100"
                    }
                } else {
                    holder.s1.error = "This field cannot be empty"
                }
            }
        })
        holder.s2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                if (inputText.isNotEmpty()) {
                    val number = inputText.toIntOrNull()
                    if (number == null || number < 1 || number > 100) {
                        holder.s2.error = "Number should be between 1 and 100"
                    }
                } else {
                    holder.s2.error = "This field cannot be empty"
                }
            }
        })
        holder.s3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                if (inputText.isNotEmpty()) {
                    val number = inputText.toIntOrNull()
                    if (number == null || number < 1 || number > 100) {
                        holder.s3.error = "Number should be between 1 and 100"
                    }
                } else {
                    holder.s3.error = "This field cannot be empty"
                }
            }
        })
    }

    override fun getItemCount() = items.size

    private fun isValidMacAddress(macAddress: String): Boolean {
        val macRegex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
        val pattern: Pattern = Pattern.compile(macRegex)
        val matcher: Matcher = pattern.matcher(macAddress)
        return matcher.find()
    }
}
