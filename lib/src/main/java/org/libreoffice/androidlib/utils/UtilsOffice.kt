package org.libreoffice.androidlib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.libreoffice.androidlib.Intent_Killed_Process
import org.libreoffice.androidlib.utils.OtherExt.getIntentToEdit
import org.libreoffice.androidlib.utils.OtherExt.logD
import org.libreoffice.androidlib.utils.OtherExt.registerCloseReceiver
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object UtilsOffice {
    const val XLSX = "xlsx"
    const val DOCX = "docx"
    const val PPTX = "pptx"
    @JvmStatic
    fun Activity.openFile(uri: Uri?,onClosed: (() -> Unit)? = null) {
        if (uri == null) return
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        registerCloseReceiver(Intent_Killed_Process,onClosed)
        val i = getIntentToEdit(this, uri)
        startActivity(i)
    }

    suspend fun Context.createNewFile(uri: Uri, fileType: String? = XLSX) {
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


}