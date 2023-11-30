package com.example.test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.adapter.VartotojaiAdapter
import com.example.test.databinding.FragmentFirstBinding
import com.example.test.viewmodel.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val viewModel by viewModels<DatabaseViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("Viewmodel F1",viewModel.hashCode().toString())
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        viewModel.vartotojaiMACList.observe(this.viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                binding.recyclerView.adapter = VartotojaiAdapter(list){ user ->
                    viewModel.getLocation(user)
                }
            } else
                binding.recyclerView.adapter = VartotojaiAdapter(emptyList()){}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}