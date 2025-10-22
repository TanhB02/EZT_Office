package org.libreoffice.androidlib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.libreoffice.androidlib.BuildConfig
import org.libreoffice.androidlib.Intent_Killed_Process
import org.libreoffice.androidlib.LOActivity
import org.libreoffice.androidlib.utils.UtilsOffice.openFile

object OtherExt {


    var documentPickerLauncher: ActivityResultLauncher<Array<String>>? = null


    fun AppCompatActivity.registerDocumentPicker() {
        documentPickerLauncher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                openFile(it)
            }
        }
    }
    fun getIntentToEdit(activity: Activity, uri: Uri?): Intent {
        val i = Intent(Intent.ACTION_EDIT, uri)
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        val componentName =
            ComponentName(activity.getPackageName(), LOActivity::class.java.getName())
        i.setComponent(componentName)
        return i
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun Activity.registerCloseReceiver(
        action: String = Intent_Killed_Process,
        onClosed: (() -> Unit)? = null
    ) {
        val filter = IntentFilter(action)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onClosed?.invoke()
                try {
                    unregisterReceiver(this)
                } catch (_: Exception) { }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(receiver, filter)
        }
    }


    fun Any.logD(log: String) {
        if (BuildConfig.DEBUG) {
            Log.d(this::class.java.simpleName, log)
        }
    }


    val mimeTypes: Array<String> = arrayOf( // ODF
        "application/vnd.oasis.opendocument.text",
        "application/vnd.oasis.opendocument.graphics",
        "application/vnd.oasis.opendocument.presentation",
        "application/vnd.oasis.opendocument.spreadsheet",
        "application/vnd.oasis.opendocument.text-flat-xml",
        "application/vnd.oasis.opendocument.graphics-flat-xml",
        "application/vnd.oasis.opendocument.presentation-flat-xml",
        "application/vnd.oasis.opendocument.spreadsheet-flat-xml",  // ODF templates

        "application/vnd.oasis.opendocument.text-template",
        "application/vnd.oasis.opendocument.spreadsheet-template",
        "application/vnd.oasis.opendocument.graphics-template",
        "application/vnd.oasis.opendocument.presentation-template",  // MS

        "application/rtf",
        "text/rtf",
        "application/msword",
        "application/vnd.ms-powerpoint",
        "application/vnd.ms-excel",
        "application/vnd.visio",
        "application/vnd.visio.xml",
        "application/x-mspublisher",
        "application/vnd.ms-excel.sheet.binary.macroenabled.12",
        "application/vnd.ms-excel.sheet.macroenabled.12",  // OOXML

        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // OOXML templates

        "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
        "application/vnd.openxmlformats-officedocument.presentationml.template",  // other

        "text/csv",
        "text/plain",
        "text/comma-separated-values",
        "application/vnd.ms-works",
        "application/vnd.apple.keynote",
        "application/x-abiword",
        "application/x-pagemaker",
        "image/x-emf",
        "image/x-svm",
        "image/x-wmf",
        "image/svg+xml",
        "application/pdf"
    )
}