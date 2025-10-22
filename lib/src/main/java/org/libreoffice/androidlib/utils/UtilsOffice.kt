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
import org.libreoffice.androidlib.utils.OtherExt.registerDocumentPicker
import java.io.IOException

object UtilsOffice {

    @JvmStatic
    fun AppCompatActivity.openFile(uri: Uri?,onClosed: (() -> Unit)? = null) {
        if (uri == null) return
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        registerCloseReceiver(Intent_Killed_Process,onClosed)
        val i = getIntentToEdit(this, uri)
        startActivity(i)
    }

    suspend fun Context.createNewFile(uri: Uri, fileType: String? = "xlsx") {
        withContext(Dispatchers.IO) {
            try {
                assets.open("templates/untitled.$fileType").use { input ->
                    contentResolver.openOutputStream(uri)?.use { output ->
                        input.copyTo(output)
                    } ?: error("Cannot open output stream for $uri")
                }
            } catch (e: IOException) {
                logD("TANHXXXX =>>>>> message:${e.message}")
            }
        }
    }


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

    fun AppCompatActivity.pickAndOpenDocument() {
        if (documentPickerLauncher == null) {
            registerDocumentPicker()
        }
        openSystemPicker()
    }



}