package org.libreoffice.androidapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.libreoffice.androidapp.databinding.ActivityMainBinding
import org.libreoffice.androidlib.utils.DocumentCallback
import org.libreoffice.androidlib.utils.DocumentManager.logD
import org.libreoffice.androidlib.utils.UtilsOffice
import org.libreoffice.androidlib.utils.UtilsOffice.createFile
import org.libreoffice.androidlib.utils.UtilsOffice.openFile
import org.libreoffice.androidlib.utils.UtilsOffice.openSystemPicker
import org.libreoffice.androidlib.utils.UtilsOffice.registerDocumentPicker
import org.libreoffice.androidlib.utils.UtilsOffice.setIDAdsBanner
import org.libreoffice.androidlib.utils.UtilsOffice.setStateShowAds

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addEvents()
        registerDocumentPicker()
        setStateShowAds(true)
    }

    private fun addEvents() {
        binding.apply {
            submitButton.setOnClickListener {
                openFileFromUri()
            }

            createFileButton.setOnClickListener {
                createFile()
            }

            openSystemPickerButton.setOnClickListener {
                openSystemPicker()
            }
        }
    }

    private fun openSystemPicker() {
        openSystemPicker(object : DocumentCallback {
            override fun onDocumentClosed() {}

            override fun onAdRevenueReceived(
                valueMicros: Long,
                currencyCode: String,
                precisionType: Int
            ) {}
        })
    }

    private fun openFileFromUri() {
        openFile(
            Uri.parse("content://com.android.providers.downloads.documents/document/msf%3A1000005320"),
            object : DocumentCallback {
                override fun onDocumentClosed() {
                    logD("TANHXXXX =>>>>> onDocumentClosed")
                }

                override fun onAdRevenueReceived(
                    valueMicros: Long,
                    currencyCode: String,
                    precisionType: Int
                ) {
                    logD("TANHXXXX =>>>>> onAdRevenueReceived")
                }
            })
    }

    private fun createFile() {
        lifecycleScope.launch {
            createFile(
                fileName = "1",
                fileType = "xlsx",
                object : DocumentCallback {
                    override fun onDocumentClosed() {}
                    override fun onAdRevenueReceived(
                        valueMicros: Long,
                        currencyCode: String,
                        precisionType: Int
                    ) {
                    }
                }
            )
        }
    }

}