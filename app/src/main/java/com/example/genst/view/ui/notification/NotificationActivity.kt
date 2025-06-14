package com.example.genst.view.ui.notification

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genst.data.result.Results
import com.example.genst.databinding.ActivityNotificationBinding
import com.example.genst.injection.Data
import com.example.genst.view.ui.riwayatlaporan.RiwayatLaporanFragment

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var viewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Data.provideRepository(this)
        viewModel = NotificationViewModel(repository)

        binding.listNotif.layoutManager = LinearLayoutManager(this)

        viewModel.fetchNotifications()

        viewModel.notification.observe(this) { result ->
            when (result) {
                is Results.Loading -> {
                    // Loading indikator jika perlu
                }
                is Results.Success -> {
                    val notification = result.data.notification
                    val adapter = NotificationAdapter(notification?.filterNotNull() ?: emptyList())
                    binding.listNotif.adapter = adapter
                }
                is Results.Error -> {
                    Toast.makeText(this, "Gagal memuat notifikasi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}