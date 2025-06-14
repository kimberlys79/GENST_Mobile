package com.example.genst.view.ui.inspeksipage

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import com.example.genst.R
import com.github.gcacace.signaturepad.views.SignaturePad
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class SignatureActivity : AppCompatActivity() {
    private lateinit var signatureView: SignaturePad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        signatureView = findViewById(R.id.signature_pad)

        // Tombol untuk menghapus tanda tangan
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            signatureView.clear()
        }

        // Tombol untuk menyimpan tanda tangan
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val bitmap = signatureView.signatureBitmap
            val file = saveBitmapAsFile(bitmap)
            val intent = Intent().apply {
                putExtra("signature_path", file.absolutePath) // ✅ fix here
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun saveBitmapAsFile(bitmap: Bitmap): File {
        val file = File(cacheDir, "signature.png")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return file
    }
}