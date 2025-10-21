package org.libreoffice.androidlib.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import org.libreoffice.androidlib.utils.OtherExt.getIntentToEdit
import org.libreoffice.androidlib.utils.OtherExt.logD
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipInputStream

object UtilsOffice {
    const val XLSX = "xlsx"
    const val DOCX = "docx"
    const val PPTX = "pptx"

    private val extractionLocks = mutableMapOf<String, Any>()

    @Synchronized
    private fun getExtractionLock(libraryName: String): Any {
        return extractionLocks.getOrPut(libraryName) { Any() }
    }

    @JvmStatic
    fun Activity.openFile( uri: Uri?) {
        logD("TANHXXXX =>>>>> uri:${uri}")
        if (uri == null) return
        val i = getIntentToEdit(this, uri)
        this.startActivity(i)
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

    /**
     * Extract native library from zip file in assets to filesDir
     * This should be called once in the main UI activity
     * Thread-safe: Multiple calls will be synchronized
     * @param context Application context
     * @param zipAssetPath Path to zip file in assets (e.g., "libandroidapp.zip")
     * @param libraryName Name of library without "lib" prefix and ".so" suffix (e.g., "androidapp")
     * @return true if extraction successful, false otherwise
     */
    @JvmStatic
    fun extractLibraryFromZip(context: Context, zipAssetPath: String, libraryName: String): Boolean {
        val TAG = "ExtractLibrary"

        // Synchronize on per-library lock to prevent race conditions
        synchronized(getExtractionLock(libraryName)) {
            try {
                // Extract to filesDir/native_libs directory (writable location)
                val nativeLibDir = File(context.filesDir, "native_libs")
                if (!nativeLibDir.exists()) {
                    nativeLibDir.mkdirs()
                }

                val libFileName = "lib$libraryName.so"
                val extractedLibFile = File(nativeLibDir, libFileName)

                // Check if library already extracted
                if (extractedLibFile.exists() && extractedLibFile.length() > 0) {
                    Log.d(TAG, "Library already extracted at: ${extractedLibFile.absolutePath}")
                    return true
                }

                // Clean up any incomplete extraction
                val tempFile = File(nativeLibDir, "$libFileName.tmp")
                if (tempFile.exists()) {
                    tempFile.delete()
                    Log.d(TAG, "Cleaned up incomplete extraction temp file")
                }

                // Extract library from zip
                Log.d(TAG, "Extracting library from assets: $zipAssetPath")
                var inputStream: InputStream? = null
                var zipInputStream: ZipInputStream? = null
                var outputStream: FileOutputStream? = null

                try {
                    inputStream = context.assets.open(zipAssetPath)
                    zipInputStream = ZipInputStream(inputStream)

                    var zipEntry = zipInputStream.nextEntry
                    var found = false

                    while (zipEntry != null) {
                        if (zipEntry.name == libFileName || zipEntry.name.endsWith("/$libFileName")) {
                            found = true
                            Log.d(TAG, "Found library in zip: ${zipEntry.name}")

                            // Extract to temporary file first
                            outputStream = FileOutputStream(tempFile)

                            val buffer = ByteArray(8192)
                            var bytesRead: Int
                            var totalBytes: Long = 0

                            while (zipInputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                totalBytes += bytesRead
                            }

                            outputStream.close()
                            outputStream = null

                            Log.d(TAG, "Extracted $totalBytes bytes")

                            // Rename temp file to final file
                            if (tempFile.renameTo(extractedLibFile)) {
                                Log.d(TAG, "Successfully extracted to: ${extractedLibFile.absolutePath}")
                            } else {
                                Log.e(TAG, "Failed to rename temp file")
                                tempFile.delete()
                                return false
                            }

                            break
                        }
                        zipInputStream.closeEntry()
                        zipEntry = zipInputStream.nextEntry
                    }

                    if (!found) {
                        Log.e(TAG, "Library $libFileName not found in zip file")
                        return false
                    }

                    return true

                } finally {
                    outputStream?.close()
                    zipInputStream?.close()
                    inputStream?.close()
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error extracting library: ${e.message}")
                e.printStackTrace()
                return false
            }
        }
    }

    /**
     * Load the extracted native library
     * This should be called in LOActivity before using native methods
     * @param context Application context
     * @param libraryName Name of library without "lib" prefix and ".so" suffix (e.g., "androidapp")
     * @return true if loading successful, false otherwise
     */
    @JvmStatic
    fun loadExtractedLibrary(context: Context, libraryName: String): Boolean {
        val TAG = "LoadExtractedLibrary"

        try {
            val nativeLibDir = File(context.filesDir, "native_libs")
            val libFileName = "lib$libraryName.so"
            val extractedLibFile = File(nativeLibDir, libFileName)

            if (!extractedLibFile.exists()) {
                Log.e(TAG, "Library not found at: ${extractedLibFile.absolutePath}")
                return false
            }

            // Load the extracted library
            System.load(extractedLibFile.absolutePath)
            Log.d(TAG, "Successfully loaded library from: ${extractedLibFile.absolutePath}")
            return true

        } catch (e: Exception) {
            Log.e(TAG, "Failed to load library: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

}