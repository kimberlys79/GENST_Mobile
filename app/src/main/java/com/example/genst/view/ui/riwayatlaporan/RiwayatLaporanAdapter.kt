package com.example.genst.view.ui.riwayatlaporan

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.genst.data.genstresponse.ReportItem
import com.example.genst.databinding.RiwayatLaporanBinding

class RiwayatLaporanAdapter(private var listLaporan: List<ReportItem>) :
    RecyclerView.Adapter<RiwayatLaporanAdapter.ViewHolder>() {

    class ViewHolder(private val binding: RiwayatLaporanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReportItem) {
            binding.tvCodeGenset.text = item.generatorCode
            binding.tvInspector.text = item.inspectorName
            binding.tvTypeGenst.text = item.generatorType
            binding.tvDate.text = item.reportDate

            binding.root.setOnClickListener {
                val context = binding.root.context

                val pdfUrl = item.reportPdfUrl

                if (!pdfUrl.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(Uri.parse(pdfUrl), "application/pdf")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Tidak bisa membuka PDF", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "File PDF tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RiwayatLaporanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listLaporan[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listLaporan.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<ReportItem>) {
        Log.d("ClientAdapter", "Updating data: ${newList.size} items")
        listLaporan = newList
        notifyDataSetChanged()
    }
}