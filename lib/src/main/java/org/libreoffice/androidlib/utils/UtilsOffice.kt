package org.libreoffice.androidlib.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import org.libreoffice.androidlib.Intent_Killed_Process
import org.libreoffice.androidlib.callback.DocumentCallback
import org.libreoffice.androidlib.manager.DocumentManager.createFileFromTemplate
import org.libreoffice.androidlib.manager.DocumentManager.documentPickerLauncher
import org.libreoffice.androidlib.manager.DocumentManager.getIntentToEdit
import org.libreoffice.androidlib.manager.DocumentManager.logD
import org.libreoffice.androidlib.manager.DocumentManager.mimeTypes
import org.libreoffice.androidlib.manager.DocumentManager.pendingDocumentCallback
import org.libreoffice.androidlib.manager.DocumentManager.registerCloseReceiver
import org.libreoffice.androidlib.utils.Preferences.bannerAdsId
import org.libreoffice.androidlib.utils.Preferences.init
import org.libreoffice.androidlib.utils.Preferences.showAds

object UtilsOffice {

    // ----------------------- ADS SETUP -----------------------

    fun setIDAdsBanner(context: Context, idAdsBanner: String) {
        init(context)
        bannerAdsId = idAdsBanner
    }

    fun setStateShowAds(context: Context, stateShowAds: Boolean) {
        init(context)
        showAds = stateShowAds
    }

    // ----------------------- DOCUMENT HANDLING -----------------------

    @JvmStatic
    fun openFile(context: Context, uri: Uri, callback: DocumentCallback) {
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        } catch (_: Exception) { }

        context.registerCloseReceiver(Intent_Killed_Process, callback)
        context.startActivity(getIntentToEdit(context, uri))
    }

    suspend fun createFile(
        context: Context,
        fileName: String,
        fileType: String = "xlsx",
        callback: DocumentCallback
    ) {
        val newFileUri = createFileFromTemplate(context, fileName, fileType)
        newFileUri?.let { openFile(context, it, callback) }
    }

    fun openSystemPicker(context: Context, callback: DocumentCallback) {
        pendingDocumentCallback = callback
        try {
            documentPickerLauncher?.launch(mimeTypes)
        } catch (_: ActivityNotFoundException) {
            val fallback = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
            context.startActivity(fallback)
        }
    }

    // ----------------------- REGISTER PICKER -----------------------

    fun registerDocumentPicker(activity: ComponentActivity) {
        documentPickerLauncher = activity.registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            logD("TANHXXXX =>>>>> uri:${uri}")
            uri?.let {
                pendingDocumentCallback?.let { callback ->
                    openFile(activity, it, callback)
                    pendingDocumentCallback = null
                }
            }
        }
    }

    fun registerDocumentPicker(fragment: Fragment, context: Context) {
        documentPickerLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                pendingDocumentCallback?.let { callback ->
                    openFile(context, it, callback)
                    pendingDocumentCallback = null
                }
            }
        }
    }
}
