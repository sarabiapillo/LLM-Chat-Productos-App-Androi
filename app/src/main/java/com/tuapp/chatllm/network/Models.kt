package com.tuapp.chatllm.network

import com.google.gson.annotations.SerializedName

// --- DTOs Oficiales de SoftwareCadenas (Catálogo y Filtro IA) ---

data class SmartFilterRequest(
    val query: String,
    @SerializedName("company_id") val companyId: Int? = null
)

data class SmartFilterResponse(
    val status: String,
    @SerializedName("filter_applied") val filterApplied: String?,
    @SerializedName("total_results") val totalResults: Int,
    val data: List<ProductDto>
)

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val stock: Int,
    @SerializedName("is_active") val isActive: Boolean,
    val category: CategoryDto?
)

data class CategoryDto(
    val id: Int,
    val name: String
)

data class CatalogResponse(
    val status: String,
    val company: CompanyDto,
    val products: List<ProductDto>?
)

data class CompanyDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("qr_code_url") val qrCodeUrl: String?
)

// --- Auth Models (Laravel API) ---

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

data class LoginRequest(
    val email: String, 
    val password: String
)

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_at") val expiresAt: String,
    val role: String?
)
