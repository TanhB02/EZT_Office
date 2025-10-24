package org.libreoffice.androidapp.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.libreoffice.androidapp.databinding.ActivityMainBinding
import org.libreoffice.androidlib.callback.DocumentCallback
import org.libreoffice.androidlib.manager.DocumentManager.logD
import org.libreoffice.androidlib.utils.UtilsOffice.createFile
import org.libreoffice.androidlib.utils.UtilsOffice.openFile
import org.libreoffice.androidlib.utils.UtilsOffice.openSystemPicker
import org.libreoffice.androidlib.utils.UtilsOffice.registerDocumentPicker
import org.libreoffice.androidlib.utils.UtilsOffice.setStateShowAds

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addEvents()
        registerDocumentPicker(this)
        setStateShowAds(this,true)
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
        openSystemPicker(this,object : DocumentCallback {
            override fun onDocumentClosed() {
                logD("TANHXXXX =>>>>> 11111")
            }

            override fun onAdRevenueReceived(
                valueMicros: Long,
                currencyCode: String,
                precisionType: Int
            ) {
                logD("TANHXXXX =>>>>> 2222222")
            }
        })
    }

    private fun openFileFromUri() {
        openFile(this,
            Uri.parse("content://com.android.providers.downloads.documents/document/msf%3A1000005320"),
            object : DocumentCallback {
                override fun onDocumentClosed() {
                    logD("TANHXXXX =>>>>> onAdRevenueReceived121212")
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
            createFile(this@MainActivity,
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