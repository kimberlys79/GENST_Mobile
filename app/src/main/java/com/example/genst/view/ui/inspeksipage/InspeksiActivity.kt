package com.example.genst.view.ui.inspeksipage

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.genst.data.result.Results
import com.example.genst.databinding.ActivityInspeksiBinding
import com.example.genst.view.ui.MainActivity
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
class InspeksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInspeksiBinding
    private lateinit var viewModel: InspeksiViewModel

    private val requestCamera = 1
    private val requestGallery = 2
    private val requestSignature = 101

    private var signatureBitmap: Bitmap? = null
    private var uploadPhotoBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            v.setPadding(
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            )
            insets
        }

        viewModel = ViewModelProvider(this,
            com.example.genst.view.utils.FactoryViewModel.getInstance(this)
        )[InspeksiViewModel::class.java]

        setupDropdowns()
        setupListeners()
    }

    private fun setupDropdowns() {
        val optionECU = arrayOf("LOW AIR PRESSURE", "HIGH WATER TEMPERATURE", "OVERCRANK", "OVERSPEED", "ENGINE STARTED")
        val optionKondisi = arrayOf("Sangat Bagus", "Bagus", "Perlu Sedikit Perbaikan", "Banyak Perbaikan", "Tidak Dapat Dipakai")

        binding.spEcu.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, optionECU)
        binding.spKondisi.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, optionKondisi)
    }

    private fun setupListeners() {
        binding.ivUploadPhoto.setOnClickListener { showImagePickerDialog() }
        binding.ivTtdInspektur.setOnClickListener { openSignaturePad() }

        binding.btnSubmit.setOnClickListener {
            if (!isFormValid()) {
                Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                uploadReport()
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, requestCamera)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, requestGallery)
    }

    private fun openSignaturePad() {
        val intent = Intent(this, SignatureActivity::class.java)
        startActivityForResult(intent, requestSignature)
    }

    private fun showImagePickerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Pilih Gambar")
            .setItems(arrayOf("Kamera", "Galeri")) { _, which ->
                if (which == 0) openCamera() else openGallery()
            }.show()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                requestCamera -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    if (bitmap != null) {
                        uploadPhotoBitmap = bitmap
                        binding.ivUploadPhoto.setImageBitmap(bitmap)
                    }
                }
                requestGallery -> {
                    val uri = data?.data
                    if (uri != null) {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        uploadPhotoBitmap = bitmap
                        binding.ivUploadPhoto.setImageBitmap(bitmap)
                    }
                }
                requestSignature -> {
                    val path = data?.getStringExtra("signature_path")
                    if (path != null) {
                        val bitmap = BitmapFactory.decodeFile(path)
                        signatureBitmap = bitmap
                        binding.ivTtdInspektur.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        return !(binding.etInspektur.text.isNullOrEmpty()
                || binding.etTgl.text.isNullOrEmpty()
                || binding.rgOnoffLockeng.checkedRadioButtonId == -1
                || binding.etLockEng.text.isNullOrEmpty()
                || binding.rgOnoffCirBreak.checkedRadioButtonId == -1
                || binding.etCirBreak.text.isNullOrEmpty()
                || binding.rgFuelG.checkedRadioButtonId == -1
                || binding.rgLevelOil.checkedRadioButtonId == -1
                || binding.etLevelOilG.text.isNullOrEmpty()
                || binding.rgColorOil.checkedRadioButtonId == -1
                || binding.etColorOilG.text.isNullOrEmpty()
                || binding.rgRadWater.checkedRadioButtonId == -1
                || binding.rgFuelPump.checkedRadioButtonId == -1
                || binding.spEcu.selectedItem == null
                || binding.etEcu.text.isNullOrEmpty()
                || binding.rgBattery.checkedRadioButtonId == -1
                || binding.etCatatan.text.isNullOrEmpty()
                || binding.spKondisi.selectedItem == null
                || binding.rgGensetBeroperasi.checkedRadioButtonId == -1
                || binding.etWeekMaintenance.text.isNullOrEmpty()
                || uploadPhotoBitmap == null
                || signatureBitmap == null)
    }

    private fun uploadReport() {
        val generatorId = intent.getIntExtra(GENERATOR_ID, 0)

        // Submit ke ViewModel
        viewModel.createReport(
            lockEngine = "ON", // contoh, sesuaikan pengambilan dari radio button
            lockEngineKeterangan = binding.etLockEng.text.toString(),
            circuitBreaker = "OFF",
            circuitBreakerKeterangan = binding.etCirBreak.text.toString(),
            fuelGenerator = "Full",
            oilGeneratorLevel = "Max",
            oilGeneratorLevelKeterangan = binding.etLevelOilG.text.toString(),
            oilGeneratorColor = "Pale",
            oilGeneratorColorKeterangan = binding.etColorOilG.text.toString(),
            radiatorWater = "Full",
            fuelPump = "Manual",
            voltmeterBR = 1,
            voltmeterYB = 2,
            voltmeterRY = 3,
            voltmeterRN = 4,
            voltmeterYN = 5,
            voltmeterBN = 6,
            voltmeterKeterangan = binding.etKeteranganVoltmeter.text.toString(),
            ammeterBR = 1,
            ammeterYB = 2,
            ammeterRY = 3,
            ammeterRN = 4,
            ammeterYN = 5,
            ammeterBN = 6,
            ammeterKeterangan = binding.etKeteranganAmmeter.text.toString(),
            batteryCharger = "ON",
            batteryChargerBR = 1,
            batteryChargerYB = 2,
            batteryChargerRY = 3,
            batteryChargerRN = 4,
            batteryChargerYN = 5,
            batteryChargerBN = 6,
            batteryChargerKeterangan = binding.etKeteranganBattCharg.text.toString(),
            ECU = binding.spEcu.selectedItem.toString(),
            battery = "Yes",
            batteryKeterangan = binding.etCatatan.text.toString(),
            uploadPhoto = "url_or_base64_upload",
            overallCondition = binding.spKondisi.selectedItem.toString(),
            generatorSafeToOperate = "Yes",
            inspectorSign = "signature_path_or_base64",
            weekMaintenanceByMem = binding.etWeekMaintenance.text.toString(),
            fkGeneratorReportId = generatorId
        )

        uploadReportObserver()
    }

    private fun uploadReportObserver() {
        viewModel.createReport.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Results.Error -> {
                    Toast.makeText(this, "Gagal mengirim laporan: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    Toast.makeText(this, "Mengirim laporan...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeReportSubmission() {
        viewModel.createReport.observe(this) { result ->
            when (result) {
                is Results.Loading -> {
                    Toast.makeText(this, "Mengirim Laporan...", Toast.LENGTH_SHORT).show()
                }
                is Results.Success -> {
                    generatePdf()
                    Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Results.Error -> {
                    Toast.makeText(this, "Gagal kirim laporan: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun generatePdf() {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 12f

        canvas.drawText("Laporan Inspeksi Genset", 20f, 30f, paint)
        canvas.drawText("Inspektur: ${binding.etInspektur.text}", 20f, 60f, paint)
        canvas.drawText("Tanggal: ${binding.etTgl.text}", 20f, 80f, paint)
        canvas.drawText("Lock Engine: ${binding.etLockEng.text}", 20f, 100f, paint)
        canvas.drawText("Circuit Breaker: ${binding.etCirBreak.text}", 20f, 120f, paint)
        canvas.drawText("Catatan: ${binding.etCatatan.text}", 20f, 140f, paint)

        document.finishPage(page)

        val file = File(getExternalFilesDir(null), "laporan_inspeksi_${System.currentTimeMillis()}.pdf")
        try {
            document.writeTo((FileOutputStream(file)))
            document.close()
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal simpan PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val GENERATOR_ID = "generator_id"
    }
}