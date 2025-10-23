package org.libreoffice.androidlib.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.libreoffice.androidlib.Intent_Killed_Process
import org.libreoffice.androidlib.utils.DocumentManager.createFileFromTemplate
import org.libreoffice.androidlib.utils.DocumentManager.documentPickerLauncher
import org.libreoffice.androidlib.utils.DocumentManager.getIntentToEdit
import org.libreoffice.androidlib.utils.DocumentManager.logD
import org.libreoffice.androidlib.utils.DocumentManager.mimeTypes
import org.libreoffice.androidlib.utils.DocumentManager.pendingDocumentCallback
import org.libreoffice.androidlib.utils.DocumentManager.prepareFileCreationValues
import org.libreoffice.androidlib.utils.DocumentManager.registerCloseReceiver
import org.libreoffice.androidlib.utils.Preferences.bannerAdsId
import org.libreoffice.androidlib.utils.Preferences.init
import org.libreoffice.androidlib.utils.Preferences.showAds

object UtilsOffice {

    fun AppCompatActivity.setIDAdsBanner(idAdsBanner: String) {
        init(this)
        bannerAdsId = idAdsBanner
    }

    fun AppCompatActivity.setStateShowAds(stateShowAds: Boolean) {
        init(this)
        showAds = stateShowAds
    }
    @JvmStatic
    fun AppCompatActivity.openFile(uri: Uri, callback: DocumentCallback) {
        try {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        } catch (_: Exception) { }

        registerCloseReceiver(Intent_Killed_Process, callback)
        startActivity(getIntentToEdit(this, uri))
    }


    suspend fun AppCompatActivity.createFile(
        fileName: String,
        fileType: String = "xlsx",
        callback: DocumentCallback
    ) {
        val contentValues = prepareFileCreationValues(fileName, fileType)
        val newFileUri = createFileFromTemplate(this, contentValues, fileType)
        newFileUri?.let { openFile(it, callback) }
    }


    fun AppCompatActivity.openSystemPicker(callback: DocumentCallback) {
        pendingDocumentCallback = callback
        try {
            documentPickerLauncher?.launch(mimeTypes)
        } catch (_: ActivityNotFoundException) {
            val fallback = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
            startActivity(fallback)
        }
    }

    fun AppCompatActivity.registerDocumentPicker() {
        documentPickerLauncher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            logD("TANHXXXX =>>>>> uri:${uri}")
            uri?.let {
                pendingDocumentCallback?.let { callback ->
                    openFile(it, callback)
                    pendingDocumentCallback = null
                }
            }
        }
    }




}