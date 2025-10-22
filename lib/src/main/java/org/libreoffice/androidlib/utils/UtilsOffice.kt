package org.libreoffice.androidlib.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.libreoffice.androidlib.Intent_Killed_Process
import org.libreoffice.androidlib.utils.OtherExt.documentPickerLauncher
import org.libreoffice.androidlib.utils.OtherExt.getIntentToEdit
import org.libreoffice.androidlib.utils.OtherExt.logD
import org.libreoffice.androidlib.utils.OtherExt.mimeTypes
import org.libreoffice.androidlib.utils.OtherExt.registerCloseReceiver
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.libreoffice.androidlib.utils.OtherExt.createFileFromTemplate
import org.libreoffice.androidlib.utils.OtherExt.prepareFileCreationValues
import java.io.File
import java.io.IOException

object UtilsOffice {

    @JvmStatic
    fun AppCompatActivity.openFile(uri: Uri?,onClosed: (() -> Unit)? = null) {
        if (uri == null) return
        try {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }catch (_: Exception){ }
        registerCloseReceiver(Intent_Killed_Process,onClosed)
        startActivity(getIntentToEdit(this, uri))
    }

    suspend fun AppCompatActivity.createFile(
        fileName: String,
        fileType: String = "xlsx",
        onClosed: (() -> Unit)? = null
    ) {
        val contentValues = prepareFileCreationValues(fileName, fileType)
        val newFileUri = createFileFromTemplate(this, contentValues, fileType)
        if (newFileUri == null) return
        this.openFile(newFileUri, onClosed)
    }

    //TODO Trong activity gọi phải gọi registerDocumentPicker
    fun AppCompatActivity.openSystemPicker() {
        try {
            documentPickerLauncher?.launch(mimeTypes)
        } catch (_: ActivityNotFoundException) {
            logD("ACTION_OPEN_DOCUMENT failed, fallback to ACTION_GET_CONTENT")
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
        ) { uri: Uri? ->
            uri?.let {
                openFile(it)
            }
        }
    }


}