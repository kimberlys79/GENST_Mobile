package com.example.genst.view.ui.riwayatlaporan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genst.data.result.Results
import com.example.genst.databinding.FragmentRiwayatLaporanBinding
import com.example.genst.view.utils.FactoryViewModel
import kotlinx.coroutines.launch

class RiwayatLaporanFragment : Fragment() {

    private val riwayatLaporanViewModelViewModel by viewModels<RiwayatLaporanViewModel> {
        FactoryViewModel.getInstance(requireContext())
    }
    private var _binding: FragmentRiwayatLaporanBinding? = null
    private val binding get() = _binding!!
    private lateinit var riwayatLaporanAdapter: RiwayatLaporanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatLaporanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        riwayatLaporanAdapter = RiwayatLaporanAdapter(emptyList())
        binding.rvDataLaporan.adapter = riwayatLaporanAdapter
        binding.rvDataLaporan.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            riwayatLaporanViewModelViewModel.fetchAllReport()
        }

        // Observe data laporan
        riwayatLaporanViewModelViewModel.allReport.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Results.Loading -> {
                    // Tampilkan loading
                }

                is Results.Success -> {
                    val data = result.data.report!!.filterNotNull()
                    riwayatLaporanAdapter.updateData(data)
                }

                is Results.Error -> {
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}