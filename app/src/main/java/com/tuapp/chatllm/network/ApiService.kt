package com.tuapp.chatllm.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // --- Endpoints de Autenticación Laravel ---

    @POST("api/v1/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/v1/verify-2fa")
    suspend fun verify2fa(@Body request: Verify2faRequest): Response<Verify2faResponse>

    @POST("api/v1/resend-2fa")
    suspend fun resend2fa(@Body request: Resend2faRequest): Response<Any>

    @POST("api/v1/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/v1/logout")
    suspend fun logout(): Response<Any>

    // --- Endpoints del Catálogo y Filtro IA ---

    /**
     * 1. Consultar Catálogo al Escanear Código QR
     * GET https://qrchatai.softwarecadenas.com.mx/api/v1/catalogo/{slug}
     */
    @GET("api/v1/catalogo/{slug}")
    suspend fun getCatalogBySlug(
        @Path("slug") slug: String
    ): Response<CatalogResponse>

    /**
     * 2. Buscador Inteligente con IA (n8n LLM Filter)
     * POST https://qrchatai.softwarecadenas.com.mx/api/v1/n8n/smart-filter
     */
    @POST("api/v1/n8n/smart-filter")
    suspend fun filterProductsSmart(
        @Body request: SmartFilterRequest
    ): Response<SmartFilterResponse>
}
