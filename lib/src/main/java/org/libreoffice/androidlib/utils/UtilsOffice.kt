package org.libreoffice.androidlib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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

    fun Context.createNewFile(uri: Uri, fileType: String? = XLSX) {
        class CreateThread : Thread() {
            override fun run() {
                var templateFileStream: InputStream? = null
                var newFileStream: OutputStream? = null
                try {
                    templateFileStream = this@createNewFile.getAssets().open("templates/untitled." + fileType)
                    newFileStream = this@createNewFile.getContentResolver().openOutputStream(uri)
                    val buffer = ByteArray(1024)
                    var length: Int
                    while ((templateFileStream.read(buffer).also { length = it }) > 0) {
                        newFileStream!!.write(buffer, 0, length)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        templateFileStream!!.close()
                        newFileStream!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        val thread = CreateThread()
        thread.run()
        try {
            thread.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}