package com.example.genst.view.ui.inspeksipage

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.genst.R
import com.example.genst.data.result.Results
import com.example.genst.databinding.ActivityInspeksiBinding
import com.example.genst.view.ui.MainActivity
import com.example.genst.view.utils.FactoryViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

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

        viewModel = ViewModelProvider(
            this,
            FactoryViewModel.getInstance(this)
        )[InspeksiViewModel::class.java]

        setupDropdowns()
        setupListeners()
    }

    private fun setupDropdowns() {
        val optionECU = arrayOf(
            "LOW AIR PRESSURE",
            "HIGH WATER TEMPERATURE",
            "OVERCRANK",
            "OVERSPEED",
            "ENGINE STARTED"
        )
        val optionKondisi = arrayOf(
            "Sangat Bagus",
            "Bagus",
            "Perlu Sedikit Perbaikan",
            "Banyak Perbaikan",
            "Tidak Dapat Dipakai"
        )
        val rangeVoltage = (0..500 step 10).map { it.toString() }

        binding.spinnerBrVoltmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYbVoltmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRyVoltmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRnVoltmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYnVoltmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerBnVoltmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)

        // Duty Selector
        binding.spinnerBrDutySelector.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYbDutySelector.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRyDutySelector.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRnDutySelector.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYnDutySelector.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerBnDutySelector.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)

// Ammeter
        binding.spinnerBrAmmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYbAmmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRyAmmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRnAmmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYnAmmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerBnAmmeter.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)

// Battery Charger
        binding.spinnerBrBatteryCharger.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYbBatteryCharger.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRyBatteryCharger.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerRnBatteryCharger.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerYnBatteryCharger.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)
        binding.spinnerBnBatteryCharger.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rangeVoltage)

        binding.spEcu.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, optionECU)
        binding.spKondisi.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, optionKondisi)
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

        binding.etTgl.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    binding.etTgl.setText(selectedDate)

                    // Set otomatis week
                    val cal = Calendar.getInstance()
                    cal.set(year, month, dayOfMonth)
                    val week = cal.get(Calendar.WEEK_OF_YEAR)
                    binding.etWeekMaintenance.setText("Minggu ke-$week")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
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
                || binding.spinnerBrVoltmeter.selectedItem == null
                || binding.spinnerYbVoltmeter.selectedItem == null
                || binding.spinnerRyVoltmeter.selectedItem == null
                || binding.spinnerRnVoltmeter.selectedItem == null
                || binding.spinnerYnVoltmeter.selectedItem == null
                || binding.spinnerBnVoltmeter.selectedItem == null
                || binding.etKeteranganVoltmeter.text.isNullOrEmpty()
                // DUTY SELECTOR
                || binding.spinnerBrDutySelector.selectedItem == null
                || binding.spinnerYbDutySelector.selectedItem == null
                || binding.spinnerRyDutySelector.selectedItem == null
                || binding.spinnerRnDutySelector.selectedItem == null
                || binding.spinnerYnDutySelector.selectedItem == null
                || binding.spinnerBnDutySelector.selectedItem == null
                || binding.etKeteranganDutySelector.text.isNullOrEmpty()

// AMMETER
                || binding.spinnerBrAmmeter.selectedItem == null
                || binding.spinnerYbAmmeter.selectedItem == null
                || binding.spinnerRyAmmeter.selectedItem == null
                || binding.spinnerRnAmmeter.selectedItem == null
                || binding.spinnerYnAmmeter.selectedItem == null
                || binding.spinnerBnAmmeter.selectedItem == null
                || binding.etKeteranganAmmeter.text.isNullOrEmpty()

// BATTERY CHARGER
                || binding.spinnerBrBatteryCharger.selectedItem == null
                || binding.spinnerYbBatteryCharger.selectedItem == null
                || binding.spinnerRyBatteryCharger.selectedItem == null
                || binding.spinnerRnBatteryCharger.selectedItem == null
                || binding.spinnerYnBatteryCharger.selectedItem == null
                || binding.spinnerBnBatteryCharger.selectedItem == null
                || binding.etKeteranganBattCharg.text.isNullOrEmpty()

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

    private fun String.toRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun uploadReport() {
        val generatorId = intent.getIntExtra(GENERATOR_ID, 0)
        val generatorCodeName = intent.getStringExtra(GENERATOR_NAME) ?: ""

        val photoRequestBody = uploadPhotoBitmap?.let {
            bitmapToRequestBody(it, "photo.jpg")
        }
        val photoPart =
            MultipartBody.Part.createFormData("upload_photo", "photo.jpg", photoRequestBody!!)

        val signatureRequestBody = signatureBitmap?.let {
            bitmapToRequestBody(it, "signature.jpg")
        }
        val signaturePart = MultipartBody.Part.createFormData(
            "inspector_sign",
            "signature.jpg",
            signatureRequestBody!!
        )

        //Get Selected Radio Button Lock Engine
        val selectedLockEngineId = binding.rgOnoffLockeng.checkedRadioButtonId
        val selectedLockEngine = findViewById<RadioButton>(selectedLockEngineId)
        val lockEngine = selectedLockEngine.text.toString()

        //Get Selected Radio Button Circuit Breaker
        val selectedCircuitBreakerId = binding.rgOnoffCirBreak.checkedRadioButtonId
        val selectedCircuitBreaker = findViewById<RadioButton>(selectedCircuitBreakerId)
        val circuitBreaker = selectedCircuitBreaker.text.toString()

        //Get Selected Radio Button Fuel Generator
        val selectedFuelGeneratorId = binding.rgFuelG.checkedRadioButtonId
        val selectedFuelGenerator = findViewById<RadioButton>(selectedFuelGeneratorId)
        val fuelGenerator = selectedFuelGenerator.text.toString()

        //Get Selected Radio Button Oil Generator Level
        val selectedOilGeneratorLevelId = binding.rgLevelOil.checkedRadioButtonId
        val selectedOilGeneratorLevel = findViewById<RadioButton>(selectedOilGeneratorLevelId)
        val oilGeneratorLevel = selectedOilGeneratorLevel.text.toString()

        //Get Selected Radio Button Oil Generator Color
        val selectedOilGeneratorColorId = binding.rgColorOil.checkedRadioButtonId
        val selectedOilGeneratorColor = findViewById<RadioButton>(selectedOilGeneratorColorId)
        val oilGeneratorColor = selectedOilGeneratorColor.text.toString()

        //Get Selected Radio Button Radiator Water
        val selectedRadiatorWaterId = binding.rgRadWater.checkedRadioButtonId
        val selectedRadiatorWater = findViewById<RadioButton>(selectedRadiatorWaterId)
        val radiatorWater = selectedRadiatorWater.text.toString()

        //Get Selected Radio Buttonn Fuel Pump
        val selectedFuelPumpId = binding.rgFuelPump.checkedRadioButtonId
        val selectedFuelPump = findViewById<RadioButton>(selectedFuelPumpId)
        val fuelPump = selectedFuelPump.text.toString()

        //Get Selected Radio Button Battery
        val selectedBatteryId = binding.rgBattery.checkedRadioButtonId
        val selectedBattery = findViewById<RadioButton>(selectedBatteryId)
        val battery = selectedBattery.text.toString()

        //Get Selected Radio Button Generator Safe to Operate
        val selectedGeneratorSafeToOperateId = binding.rgGensetBeroperasi.checkedRadioButtonId
        val selectedGeneratorSafeToOperate =
            findViewById<RadioButton>(selectedGeneratorSafeToOperateId)
        val generatorSafeToOperate = selectedGeneratorSafeToOperate.text.toString()


        val pdfFile = generatePdf(
            generatorCodeName,
            lockEngine,
            circuitBreaker,
            fuelGenerator,
            oilGeneratorLevel,
            oilGeneratorColor,
            radiatorWater,
            fuelPump,
            generatorSafeToOperate
        )
        val pdfRequestBody = pdfFile.asRequestBody("application/pdf".toMediaTypeOrNull())

        val generatedPdf = MultipartBody.Part.createFormData(
            "report_pdf",
            pdfFile.name,
            pdfRequestBody
        )


        // Submit ke ViewModel
        viewModel.createReport(
            lockEngine = lockEngine.toRequestBody(),
            lockEngineKeterangan = binding.etLockEng.text.toString().toRequestBody(),
            circuitBreaker = circuitBreaker.toRequestBody(),
            circuitBreakerKeterangan = binding.etCirBreak.text.toString().toRequestBody(),
            fuelGenerator = fuelGenerator.toRequestBody(),
            oilGeneratorLevel = oilGeneratorLevel.toRequestBody(),
            oilGeneratorLevelKeterangan = binding.etLevelOilG.text.toString().toRequestBody(),
            oilGeneratorColor = oilGeneratorColor.toRequestBody(),
            oilGeneratorColorKeterangan = binding.etColorOilG.text.toString().toRequestBody(),
            radiatorWater = radiatorWater.toRequestBody(),
            fuelPump = fuelPump.toRequestBody(),

            // Voltmeter
            voltmeterBR = binding.spinnerBrVoltmeter.selectedItem.toString().toRequestBody(),
            voltmeterYB = binding.spinnerYbVoltmeter.selectedItem.toString().toRequestBody(),
            voltmeterRY = binding.spinnerRyVoltmeter.selectedItem.toString().toRequestBody(),
            voltmeterRN = binding.spinnerRnVoltmeter.selectedItem.toString().toRequestBody(),
            voltmeterYN = binding.spinnerYnVoltmeter.selectedItem.toString().toRequestBody(),
            voltmeterBN = binding.spinnerBnVoltmeter.selectedItem.toString().toRequestBody(),
            voltmeterKeterangan = binding.etKeteranganVoltmeter.text.toString().toRequestBody(),

            // DUTY SELECTOR
            dutySelectorBR = binding.spinnerBrDutySelector.selectedItem.toString().toRequestBody(),
            dutySelectorYB = binding.spinnerYbDutySelector.selectedItem.toString().toRequestBody(),
            dutySelectorRY = binding.spinnerRyDutySelector.selectedItem.toString().toRequestBody(),
            dutySelectorRN = binding.spinnerRnDutySelector.selectedItem.toString().toRequestBody(),
            dutySelectorYN = binding.spinnerYnDutySelector.selectedItem.toString().toRequestBody(),
            dutySelectorBN = binding.spinnerBnDutySelector.selectedItem.toString().toRequestBody(),
            dutySelectorKeterangan = binding.etKeteranganDutySelector.text.toString()
                .toRequestBody(),

            // AMMETER
            ammeterBR = binding.spinnerBrAmmeter.selectedItem.toString().toRequestBody(),
            ammeterYB = binding.spinnerYbAmmeter.selectedItem.toString().toRequestBody(),
            ammeterRY = binding.spinnerRyAmmeter.selectedItem.toString().toRequestBody(),
            ammeterRN = binding.spinnerRnAmmeter.selectedItem.toString().toRequestBody(),
            ammeterYN = binding.spinnerYnAmmeter.selectedItem.toString().toRequestBody(),
            ammeterBN = binding.spinnerBnAmmeter.selectedItem.toString().toRequestBody(),
            ammeterKeterangan = binding.etKeteranganAmmeter.text.toString().toRequestBody(),

            // BATTERY CHARGER
            batteryCharger = if (binding.rbOnBattCharg.isChecked) "ON".toRequestBody() else "OFF".toRequestBody(),
            batteryChargerBR = binding.spinnerBrBatteryCharger.selectedItem.toString()
                .toRequestBody(),
            batteryChargerYB = binding.spinnerYbBatteryCharger.selectedItem.toString()
                .toRequestBody(),
            batteryChargerRY = binding.spinnerRyBatteryCharger.selectedItem.toString()
                .toRequestBody(),
            batteryChargerRN = binding.spinnerRnBatteryCharger.selectedItem.toString()
                .toRequestBody(),
            batteryChargerYN = binding.spinnerYnBatteryCharger.selectedItem.toString()
                .toRequestBody(),
            batteryChargerBN = binding.spinnerBnBatteryCharger.selectedItem.toString()
                .toRequestBody(),
            batteryChargerKeterangan = binding.etKeteranganBattCharg.text.toString()
                .toRequestBody(),

            ecuCondition = binding.spEcu.selectedItem.toString().toRequestBody(),
            ecuDescription = binding.etEcu.text.toString().toRequestBody(),
            battery = battery.toRequestBody(),
            batteryKeterangan = binding.etCatatan.text.toString().toRequestBody(),
            uploadPhoto = photoPart,
            overallCondition = binding.spKondisi.selectedItem.toString().toRequestBody(),
            generatorSafeToOperate = generatorSafeToOperate.toRequestBody(),
            inspectorSign = signaturePart,
            weekMaintenanceByMem = binding.etWeekMaintenance.text.toString().toRequestBody(),
            reportPdf = generatedPdf,
            fkGeneratorReportId = generatorId
        )

        uploadReportObserver()
    }

    @SuppressLint("InflateParams")
    private fun generatePdf(
        generatorCodeName: String,
        lockEngine: String,
        circuitBreaker: String,
        fuelGenerator: String,
        oilGeneratorLevel: String,
        oilGeneratorColor: String,
        radiatorWater: String,
        fuelPump: String,
        generatorSafeToOperate: String
    ): File {
        // 1. Inflate layout pdfView
        val pdfView = LayoutInflater.from(this).inflate(R.layout.activity_laporan_inspeksi, null)

        // 2. Isi field

        // Header
        pdfView.findViewById<TextView>(R.id.tv_inspektur).text = getString(R.string.pdf_inspektur, binding.etInspektur.text.toString())
        pdfView.findViewById<TextView>(R.id.tv_tanggal).text = getString(R.string.pdf_tanggal, binding.etTgl.text.toString())
        pdfView.findViewById<TextView>(R.id.tvGenerator).text = getString(R.string.generator_code_name, generatorCodeName)

        // Emergency Control Panel
        pdfView.findViewById<TextView>(R.id.tvLockEngCond).text = lockEngine
        pdfView.findViewById<TextView>(R.id.tvLockEngDesc).text = binding.etLockEng.text.toString()

        pdfView.findViewById<TextView>(R.id.tvCircBreakerCond).text =
            circuitBreaker
        pdfView.findViewById<TextView>(R.id.tvCircBreakerDesc).text =
            binding.etCirBreak.text.toString()

        pdfView.findViewById<TextView>(R.id.tvFuelGeneratorCond).text =
            fuelGenerator

        pdfView.findViewById<TextView>(R.id.tvOilGeneratorLvlCond).text =
            oilGeneratorLevel
        pdfView.findViewById<TextView>(R.id.tvOilGeneratorLvlDesc).text =
            binding.etLevelOilG.text.toString()

        pdfView.findViewById<TextView>(R.id.tvOilGeneratorClrCond).text =
            oilGeneratorColor
        pdfView.findViewById<TextView>(R.id.tvOilGeneratorClrDesc).text =
            binding.etColorOilG.text.toString()

        pdfView.findViewById<TextView>(R.id.tvRadiatorWaterCond).text =
            radiatorWater
        pdfView.findViewById<TextView>(R.id.tvFuelPumpStatus).text =
            fuelPump

        // Voltmeter
        pdfView.findViewById<TextView>(R.id.tvbrVoltmeterCond).text =
            binding.spinnerBrVoltmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYbVoltmeterCond).text =
            binding.spinnerYbVoltmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRyVoltmeterCond).text =
            binding.spinnerRyVoltmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRnVoltmeterCond).text =
            binding.spinnerRnVoltmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYnVoltmeterCond).text =
            binding.spinnerYnVoltmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvBnVOltmeterCond).text =
            binding.spinnerBnVoltmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvBrVoltmeterDesc).text =
            binding.etKeteranganVoltmeter.text.toString()


        // Duty Selector
        pdfView.findViewById<TextView>(R.id.tvBrDutySlcCond).text =
            binding.spinnerBrDutySelector.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYbDutySlcCond).text =
            binding.spinnerYbDutySelector.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRyDutySlcCond).text =
            binding.spinnerRyDutySelector.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRnDutySlcCond).text =
            binding.spinnerRnDutySelector.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYnDutySlcCond).text =
            binding.spinnerYnDutySelector.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvBnDutySlcCOd).text =
            binding.spinnerBnDutySelector.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvBrDutySlcDesc).text =
            binding.etKeteranganDutySelector.text.toString()

        // Ammeter
        pdfView.findViewById<TextView>(R.id.tvBrAmmeterCond).text =
            binding.spinnerBrAmmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYbAmmeterCond).text =
            binding.spinnerYbAmmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRyAmmeterCond).text =
            binding.spinnerRyAmmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRnAmmeterCond).text =
            binding.spinnerRnAmmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYnAmmeterCond).text =
            binding.spinnerYnAmmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvBnAmmeterCond).text =
            binding.spinnerBnAmmeter.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvBrAmmeterDesc).text =
            binding.etKeteranganAmmeter.text.toString()

        // Battery Charger
        pdfView.findViewById<TextView>(R.id.tvBatteryChargCond).text =
            if (binding.rbOnBattCharg.isChecked) "ON" else "OFF"
        pdfView.findViewById<TextView>(R.id.tvBatteryChargerDesc).text =
            binding.etKeteranganBattCharg.text.toString()

        pdfView.findViewById<TextView>(R.id.tvBrBattChargCond).text =
            binding.spinnerBrBatteryCharger.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYbBattChargCond).text =
            binding.spinnerYbBatteryCharger.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRyBattChargCond).text =
            binding.spinnerRyBatteryCharger.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvRnBattChargCond).text =
            binding.spinnerRnBatteryCharger.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvYnBattChargCond).text =
            binding.spinnerYnBatteryCharger.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvBnBattChargCond).text =
            binding.spinnerBnBatteryCharger.selectedItem.toString()

        // ECU
        pdfView.findViewById<TextView>(R.id.tvEcuCond).text = binding.spEcu.selectedItem.toString()
        pdfView.findViewById<TextView>(R.id.tvEcuDesc).text = binding.etEcu.text.toString()

        // Foto
        pdfView.findViewById<ImageView>(R.id.ivUnggahFotoCond).setImageBitmap(uploadPhotoBitmap)

        //kondisi Keseluruhan
        pdfView.findViewById<TextView>(R.id.tvKondisiKeseluruhanCond).text = binding.spKondisi.selectedItem.toString()

        // Aman untuk beroperasi
        pdfView.findViewById<TextView>(R.id.tvAmanuntukBeroperasiCond).text =
            generatorSafeToOperate

        // TTD
        pdfView.findViewById<ImageView>(R.id.ivTtdInspekturCond).setImageBitmap(signatureBitmap)

        // Maintenance MEM
        pdfView.findViewById<TextView>(R.id.tvMaintenanceMemCond).text =
            binding.etWeekMaintenance.text.toString()

        // 3. Ukuran PDF A4
        val pageWidth = 595
        val pageHeight = 842

        pdfView.measure(
            View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        pdfView.layout(0, 0, pageWidth, pdfView.measuredHeight)

        val totalHeight = pdfView.measuredHeight
        var currentHeight = 0
        var pageNumber = 1

        // 4. Buat document
        val document = PdfDocument()

        // Loop tiap halaman
        while (currentHeight < totalHeight) {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            val page = document.startPage(pageInfo)

            val canvas = page.canvas
            canvas.translate(0f, -currentHeight.toFloat())  // geser supaya yang terlihat bagian currentHeight

            pdfView.draw(canvas)

            document.finishPage(page)

            currentHeight += pageHeight
            pageNumber++
        }

        // 5. Save file
        val file = File(getExternalFilesDir(null), "Laporan_Inspeksi_${System.currentTimeMillis()}.pdf")
        try {
            FileOutputStream(file).use { out ->
                document.writeTo(out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            document.close()
        }

        return file
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
                    Toast.makeText(
                        this,
                        "Gagal mengirim laporan: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Loading -> {
                    Toast.makeText(this, "Mengirim laporan...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bitmapToRequestBody(bitmap: Bitmap, filename: String): RequestBody {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val byteArray = bos.toByteArray()
        return byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
    }

    companion object {
        const val GENERATOR_ID = "GENERATOR_ID"
        const val GENERATOR_NAME = "GENERATOR_NAME"
    }
}
