package com.example.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.test.databinding.FragmentSecondBinding
import com.example.test.viewmodel.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val viewModel by viewModels<DatabaseViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.columnCount.observe(this.viewLifecycleOwner) { columnCount ->
            if (columnCount != null && columnCount != 0)
                binding.gridLayout.columnCount = columnCount
        }
        viewModel.rowCount.observe(this.viewLifecycleOwner) { rowCount ->
            if (rowCount != null && rowCount != 0)
                binding.gridLayout.rowCount = rowCount
        }
        val filledPositions = mutableSetOf<Pair<Int, Int>>()
        viewModel.userLocation.observe(this.viewLifecycleOwner) { userLocation ->
            if (userLocation != null) {
                val userGridItem =
                    layoutInflater.inflate(
                        R.layout.user_grid_item,
                        binding.gridLayout,
                        false
                    )
                val layoutParams = GridLayout.LayoutParams()
                layoutParams.columnSpec = GridLayout.spec(userLocation.first)
                layoutParams.rowSpec = GridLayout.spec(userLocation.second)
                userGridItem.layoutParams = layoutParams
                binding.gridLayout.addView(userGridItem)
                viewModel.matavimai.observe(this.viewLifecycleOwner) { matavimaiList ->
                    matavimaiList?.forEach {
                        val gridItemView =
                            layoutInflater.inflate(R.layout.grid_item, binding.gridLayout, false)
                        val layoutParamsGreen = GridLayout.LayoutParams()
                        layoutParamsGreen.columnSpec = GridLayout.spec(it.x)
                        layoutParamsGreen.rowSpec = GridLayout.spec(it.y)
                        gridItemView.layoutParams = layoutParamsGreen
                        val tvMatavimas = gridItemView.findViewById<TextView>(R.id.tvMatavimas)
                        tvMatavimas.text = "1"
                        if (!(it.x == userLocation.first && it.y == userLocation.second))
                            binding.gridLayout.addView(gridItemView)
                        filledPositions.add(Pair(it.x, it.y))
                    }
                }
                viewModel.rowCount.observe(this.viewLifecycleOwner) { rowCount ->
                    viewModel.columnCount.observe(this.viewLifecycleOwner) { columnCount ->
                        if (rowCount != null && columnCount != null) {
                            for (x in 0 until columnCount) {
                                for (y in 0 until rowCount) {
                                    if (Pair(x, y) !in filledPositions) {
                                        val emptyGridItemView =
                                            layoutInflater.inflate(
                                                R.layout.empty_grid_item,
                                                binding.gridLayout,
                                                false
                                            )
                                        val layoutParams = GridLayout.LayoutParams()
                                        layoutParams.columnSpec = GridLayout.spec(x)
                                        layoutParams.rowSpec = GridLayout.spec(y)
                                        emptyGridItemView.layoutParams = layoutParams
                                        binding.gridLayout.addView(emptyGridItemView)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}