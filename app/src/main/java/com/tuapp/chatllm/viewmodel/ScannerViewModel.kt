package com.tuapp.chatllm.viewmodel

import androidx.lifecycle.ViewModel
import com.tuapp.chatllm.util.QrScannerUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScannerViewModel : ViewModel() {
    private val _scannedSlug = MutableStateFlow<String?>(null)
    val scannedSlug: StateFlow<String?> = _scannedSlug.asStateFlow()

    fun processQrCode(url: String) {
        val slug = QrScannerUtil.extractSlugFromUrl(url)
        if (slug != null) {
            _scannedSlug.value = slug
        }
    }

    fun resetScanner() {
        _scannedSlug.value = null
    }
}
