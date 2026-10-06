package com.tuapp.chatllm.data

import com.google.firebase.firestore.PropertyName

data class ContextItem(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val category: String = "Productividad",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val discountPercent: Int = 0,
    @get:PropertyName("isActive")
    @set:PropertyName("isActive")
    var isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    val discountedPrice: Double
        get() = if (discountPercent in 1..99) price * (1.0 - discountPercent / 100.0) else price
}
