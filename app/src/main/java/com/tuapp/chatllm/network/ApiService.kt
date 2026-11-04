package com.tuapp.chatllm.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // --- Endpoints de Autenticación ---

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

    // --- Catálogo al escanear QR ---

    @GET("api/v1/catalogo/{slug}")
    suspend fun getCatalogBySlug(
        @Path("slug") slug: String
    ): Response<CatalogResponse>

    // --- Endpoints de Chat IA (n8n) y Catálogo de Ropa/Zapatos ---

    @POST("api/v1/customer/ask")
    suspend fun askAssistant(
        @Header("Authorization") token: String?,
        @Body request: ChatCustomerRequest
    ): Response<ChatCustomerResponse>

    @GET("api/v1/products")
    suspend fun getProducts(
        @Query("giro") giro: String? = null,
        @Query("has_offer") hasOffer: Int? = null,
        @Query("search") search: String? = null
    ): Response<ProductCatalogResponse>

    @GET("api/v1/products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: Int
    ): Response<Map<String, Any>>

    @FormUrlEncoded
    @POST("api/v1/products/{id}/buy")
    suspend fun buyProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("quantity") quantity: Int = 1
    ): Response<Map<String, Any>>
}
