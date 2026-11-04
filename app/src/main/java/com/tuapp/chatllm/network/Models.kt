package com.tuapp.chatllm.network

import com.google.gson.annotations.SerializedName

// --- Autenticación ---

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("user") val user: UserDto?
)

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirmation") val passwordConfirmation: String
)

data class RegisterResponse(
    @SerializedName("code_2fa") val code2fa: String,
    @SerializedName("is_2fa_verified") val is2faVerified: Boolean
)

data class Verify2faRequest(
    val email: String,
    val code: String
)

data class Verify2faResponse(
    val status: String,
    @SerializedName("is_2fa_verified") val is2faVerified: Boolean
)

data class Resend2faRequest(
    val email: String
)

// --- Chat con IA y n8n ---

data class ChatCustomerRequest(
    @SerializedName("question") val question: String,
    @SerializedName("cliente_nombre") val clienteNombre: String? = null,
    @SerializedName("cliente_email") val clienteEmail: String? = null
)

data class ChatCustomerResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("is_appropriate") val isAppropriate: Boolean,
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String,
    @SerializedName("recommended_products") val recommendedProducts: List<RecommendedProduct>?,
    @SerializedName("timestamp") val timestamp: String?
)

data class RecommendedProduct(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("giro") val giro: String, // "ropa" o "zapatos"
    @SerializedName("categoria") val categoria: String?,
    @SerializedName("precio_regular") val precioRegular: Double,
    @SerializedName("tiene_oferta") val tieneOferta: Boolean,
    @SerializedName("precio_oferta") val precioOferta: Double?,
    @SerializedName("descuento") val descuento: String?,
    @SerializedName("detalles") val detalles: String?
)

// --- Catálogo General de Productos ---

data class ProductCatalogResponse(
    @SerializedName("status") val status: String,
    @SerializedName("total") val total: Int,
    @SerializedName("data") val data: List<ProductItem>
)

data class ProductItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("business_type") val businessType: String,
    @SerializedName("price") val price: Double,
    @SerializedName("has_offer") val hasOffer: Boolean,
    @SerializedName("offer_price") val offerPrice: Double?,
    @SerializedName("discount_percentage") val discountPercentage: Int?,
    @SerializedName("final_price") val finalPrice: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("size") val size: String?,
    @SerializedName("shoe_size") val shoeSize: String?,
    @SerializedName("material") val material: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("image_url") val imageUrl: String?
)

data class CatalogResponse(
    val status: String,
    val company: CompanyDto,
    val products: List<ProductItem>?
)

data class CompanyDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("qr_code_url") val qrCodeUrl: String?
)
