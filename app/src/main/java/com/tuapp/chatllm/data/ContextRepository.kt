package com.tuapp.chatllm.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ContextRepository {
    private val db by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (_: Throwable) {
            null
        }
    }
    private val collectionName = "contexts"

    private val registeredUsers = mutableListOf(
        UserAccount(email = "admin@chatllm.com", role = UserRole.ADMIN) to "admin123",
        UserAccount(email = "admin", role = UserRole.ADMIN) to "admin123",
        UserAccount(email = "cliente@chatllm.com", role = UserRole.CLIENT) to "cliente123",
        UserAccount(email = "cliente", role = UserRole.CLIENT) to "1234"
    )

    fun isEmailRegistered(emailInput: String): Boolean {
        val cleanEmail = emailInput.trim().lowercase()
        return registeredUsers.any { (user, _) -> user.email.lowercase() == cleanEmail }
    }

    fun loginUser(emailInput: String, passInput: String): UserAccount? {
        val cleanEmail = emailInput.trim().lowercase()
        val cleanPass = passInput.trim()
        val match = registeredUsers.find { (user, pass) ->
            (user.email.lowercase() == cleanEmail) && pass == cleanPass
        }
        return match?.first
    }

    fun registerUser(emailInput: String, passInput: String, role: UserRole): UserAccount? {
        val cleanEmail = emailInput.trim().lowercase()
        val cleanPass = passInput.trim()

        if (cleanEmail.isEmpty() || cleanPass.isEmpty()) return null

        val existing = registeredUsers.find { (user, _) -> user.email.lowercase() == cleanEmail }
        if (existing != null) {
            return null
        }

        val newUser = UserAccount(email = cleanEmail, role = role)
        registeredUsers.add(newUser to cleanPass)
        return newUser
    }

    private val localContexts = mutableListOf(
        ContextItem(
            id = "prod_1",
            title = "Asistente de Código Kotlin & Compose Pro",
            content = "Responde consultas técnicas sobre código Kotlin, Jetpack Compose, arquitectura MVVM/MVI y mejores prácticas.",
            category = "Desarrollo",
            imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=400",
            price = 49.99,
            discountPercent = 30,
            isActive = true
        ),
        ContextItem(
            id = "prod_2",
            title = "Planificador Pomodoro & Matriz Eisenhower",
            content = "Organiza objetivos diarios en bloques de trabajo de 25 minutos con priorización ejecutiva de tareas.",
            category = "Gestión de Tiempo",
            imageUrl = "https://images.unsplash.com/photo-1506784983877-45594efa4cbe?w=400",
            price = 29.99,
            discountPercent = 20,
            isActive = true
        ),
        ContextItem(
            id = "prod_3",
            title = "Optimizador de Emails & Pitch Ejecutivo",
            content = "Ayuda a redactar correos electrónicos de negocios directos, persuasivos y de alto impacto.",
            category = "Redacción",
            imageUrl = "https://images.unsplash.com/photo-1596526131083-e8c633c948d2?w=400",
            price = 19.99,
            discountPercent = 15,
            isActive = true
        ),
        ContextItem(
            id = "prod_4",
            title = "Generador de Resúmenes & Action Items",
            content = "Sintetiza documentos extensos en 3 conclusiones estratégicas y tareas ejecutables.",
            category = "Productividad",
            imageUrl = "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=400",
            price = 39.99,
            discountPercent = 25,
            isActive = true
        )
    )

    suspend fun getAllContexts(): List<ContextItem> {
        val firestore = db ?: return localContexts.toList()
        return try {
            val snapshot = firestore.collection(collectionName).get().await()
            val remoteList = snapshot.toObjects(ContextItem::class.java)
            if (remoteList.isEmpty() && localContexts.isNotEmpty()) {
                localContexts.toList()
            } else {
                remoteList
            }
        } catch (_: Throwable) {
            localContexts.toList()
        }
    }

    suspend fun getActiveContexts(): List<ContextItem> {
        val all = getAllContexts()
        return all.filter { it.isActive }
    }

    suspend fun saveContext(item: ContextItem) {
        val firestore = db
        val id = item.id.ifEmpty { firestore?.collection(collectionName)?.document()?.id ?: java.util.UUID.randomUUID().toString() }
        val newItem = item.copy(id = id)

        val index = localContexts.indexOfFirst { it.id == id }
        if (index >= 0) {
            localContexts[index] = newItem
        } else {
            localContexts.add(newItem)
        }

        if (firestore != null) {
            try {
                firestore.collection(collectionName).document(id).set(newItem).await()
            } catch (_: Throwable) {
            }
        }
    }

    suspend fun deleteContext(id: String) {
        localContexts.removeAll { it.id == id }
        val firestore = db ?: return
        try {
            firestore.collection(collectionName).document(id).delete().await()
        } catch (_: Throwable) {
            // Silently fallback to local
        }
    }
}
