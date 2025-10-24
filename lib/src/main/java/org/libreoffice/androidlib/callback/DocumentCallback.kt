package org.libreoffice.androidlib.callback

interface DocumentCallback {
    fun onDocumentClosed()
    fun onAdRevenueReceived(valueMicros: Long, currencyCode: String, precisionType: Int)
}