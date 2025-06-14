package com.example.genst.view.ui.notification

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.genst.data.genstresponse.NotificationItem
import com.example.genst.databinding.NotificationListBinding
import com.example.genst.view.ui.riwayatlaporan.RiwayatLaporanFragment

class NotificationAdapter(
    private val data: List<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(private val binding: NotificationListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationItem) {
            binding.tvTitleNotif.text = item.title.toString()
            binding.tvDescNotif.text = item.message.toString()

            val createAt = item.createAt ?: ""
            val datePart = createAt.substringBefore("T")
            val timePart = createAt.substringAfter("T").substringBefore(".")

            binding.tvKeteranganTanggal.text = "$timePart • $datePart"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NotificationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}