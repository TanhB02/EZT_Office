package org.libreoffice.androidlib.utils

interface DocumentCallback {
    fun onDocumentClosed()
    fun onAdRevenueReceived(valueMicros: Long, currencyCode: String, precisionType: Int)
}