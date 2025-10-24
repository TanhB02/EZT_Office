package org.libreoffice.androidlib.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebView

class WebViewInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        setupWebViewDataDirectory()
        return true
    }

    /**
     * Setup WebView data directory riêng cho loactivity process
     * Giải quyết lỗi: "Using WebView from more than one process at once with the same data directory is not supported"
     */
    private fun setupWebViewDataDirectory() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val processName = Application.getProcessName()
                if (processName != null && processName.contains(":loactivity")) {
                    WebView.setDataDirectorySuffix("loactivity")
                    Log.d(TAG, "WebView data directory suffix set to: loactivity for process: $processName")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting WebView data directory", e)
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    companion object {
        private const val TAG = "WebViewInitProvider"
    }
}