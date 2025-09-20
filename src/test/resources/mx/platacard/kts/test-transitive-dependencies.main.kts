#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.fasterxml.jackson.core:jackson-databind:2.16.1")
@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

import com.fasterxml.jackson.databind.ObjectMapper  // Прямая зависимость: jackson-databind
import com.fasterxml.jackson.module.kotlin.KotlinModule  // Прямая зависимость: jackson-module-kotlin
import com.fasterxml.jackson.databind.JsonNode  // ТРАНЗИТИВНАЯ зависимость: jackson-core (через jackson-databind)
import com.fasterxml.jackson.core.JsonParser  // ТРАНЗИТИВНАЯ зависимость: jackson-core (через jackson-databind)
import com.fasterxml.jackson.core.type.TypeReference  // ТРАНЗИТИВНАЯ зависимость: jackson-core (через jackson-databind)

// Тестируем использование транзитивных зависимостей Jackson
data class User(val name: String, val age: Int, val email: String)

val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

// Создаем тестовые данные
val user = User("John Doe", 30, "john@example.com")

// Сериализуем в JSON
val json = mapper.writeValueAsString(user)
println("Serialized JSON: $json")

// Десериализуем обратно
val deserializedUser = mapper.readValue(json, User::class.java)
println("Deserialized user: $deserializedUser")

// Тестируем JsonNode (транзитивная зависимость)
val jsonNode: JsonNode = mapper.readTree(json)
println("JSON Node - name: ${jsonNode.get("name").asText()}")
println("JSON Node - age: ${jsonNode.get("age").asInt()}")

// Тестируем TypeReference (транзитивная зависимость)
val userList = listOf(user, User("Jane Doe", 25, "jane@example.com"))
val listJson = mapper.writeValueAsString(userList)
val deserializedList = mapper.readValue(listJson, object : TypeReference<List<User>>() {})
println("Deserialized list size: ${deserializedList.size}")

println("✅ All transitive dependencies work correctly!")
