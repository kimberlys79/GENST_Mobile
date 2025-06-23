package com.example.genst.view.ui.laporaninspeksi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.genst.R
import com.example.genst.data.result.Results
import com.example.genst.databinding.ActivityLaporanInspeksiBinding
import com.example.genst.view.utils.FactoryViewModel

class LaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanInspeksiBinding
    private val laporanViewModelViewModel by viewModels<LaporanViewModel> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLaporanInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ambil token dan ID laporan dari intent
        val token = intent.getStringExtra("TOKEN") ?: return
        val reportId = intent.getIntExtra("REPORT_ID", -1)
        if (reportId == -1) {
            Toast.makeText(this, "ID laporan tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Panggil API untuk mengambil detail laporan
        laporanViewModelViewModel.fetchReportDetail(token, reportId)

        // Observe hasilnya
        laporanViewModelViewModel.getreportDetailResponse.observe(this) { result ->
            when (result) {
                is Results.Loading -> {
                    // Tampilkan loading jika perlu
                }

                is Results.Success -> {
                    val laporan = result.data.reportDetail?.firstOrNull() ?: return@observe
                    binding.tvInspektur.text = laporan.fkUserReportId.toString()
                    binding.tvTanggal.text = laporan.dateTime.toString()
                }

                is Results.Error -> {
                    Toast.makeText(this, "Gagal memuat laporan: ${result.error}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}
