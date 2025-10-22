package org.libreoffice.androidlib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.libreoffice.androidlib.BuildConfig
import org.libreoffice.androidlib.Intent_Killed_Process
import org.libreoffice.androidlib.LOActivity
import org.libreoffice.androidlib.utils.UtilsOffice.openFile
import java.io.File

object OtherExt {


    var documentPickerLauncher: ActivityResultLauncher<Array<String>>? = null



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

    fun prepareFileCreationValues(fileName: String, fileType: String): ContentValues {
        val mimeType = when (fileType.lowercase()) {
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            else -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        }

        val fullFileName = "$fileName.$fileType"

        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fullFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            } else {
                @Suppress("DEPRECATION")
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val filePath = File(downloadsDir, fullFileName).absolutePath
                put(MediaStore.MediaColumns.DATA, filePath)
            }
        }
    }

    suspend fun createFileFromTemplate(
        context: Context,
        contentValues: ContentValues,
        fileType: String
    ): Uri? {
        return withContext(Dispatchers.IO) {
            var newFileUri: Uri? = null
            try {
                val collection: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    @Suppress("DEPRECATION")
                    collection = MediaStore.Files.getContentUri("external")
                }

                newFileUri = context.contentResolver.insert(collection, contentValues)
                    ?: error("Không thể tạo MediaStore entry")

                context.assets.open("templates/untitled.$fileType").use { input ->
                    context.contentResolver.openOutputStream(newFileUri)?.use { output ->
                        input.copyTo(output)
                    } ?: error("Không thể mở output stream cho $newFileUri")
                }
                newFileUri

            } catch (e: Exception) {
                logD("TANHXXXX =>>>>> Lỗi khi tạo file: ${e.message}")

                newFileUri?.let {
                    try {
                        context.contentResolver.delete(it, null, null)
                    } catch (deleteEx: Exception) {
                        logD("TANHXXXX =>>>>> Lỗi khi dọn dẹp file: ${deleteEx.message}")
                    }
                }
                null
            }
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