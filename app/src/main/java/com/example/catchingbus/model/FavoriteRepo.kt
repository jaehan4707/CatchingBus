package com.example.catchingbus.model

import com.example.catchingbus.data.Favorite
import com.example.catchingbus.model.json.Json
import com.example.catchingbus.model.json.JsonFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.io.path.Path

object FavoriteRepo {
    private val file = JsonFile(Path("favorites.txt"))

    private val flowValue = mutableListOf<Favorite>()
    private val flow = MutableStateFlow<List<Favorite>>(flowValue)
    val data: StateFlow<List<Favorite>> = flow

    private val mutex = Mutex()

    suspend fun load() {
        mutex.withLock {
            val jsonElement = file.load()
            flow.value = Json.deserialize(Favorite::class, jsonElement)
        }
    }

    private suspend fun save() {
        val jsonString = Json.serialize(Favorite::class, flowValue)
        file.save(jsonString)
    }

    suspend fun clear() {
        mutex.withLock {
            flow.value = flowValue.also { it.clear() }
        }
        save()
    }

    suspend fun add(element: Favorite) {
        mutex.withLock {
            flow.value = flowValue.also { it.add(element) }
        }
        save()
    }

    suspend fun remove(element: Favorite) {
        mutex.withLock {
            flow.value = flowValue.also { it.remove(element) }
        }
        save()
    }
}