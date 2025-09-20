#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.apache.commons:commons-lang3:3.13.0")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
@file:DependsOn("com.fasterxml.jackson.core:jackson-databind:2.16.1")
@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

import org.apache.commons.lang3.StringUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

data class UserData(val name: String, val age: Int)

val name = if (args.isNotEmpty()) args[0] else "TestUser"

runBlocking {
    println("Processing user: $name")
    delay(100) // Simulate async work
    
    val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    val userData = UserData(StringUtils.capitalize(name), 25)
    val json = mapper.writeValueAsString(userData)
    
    println("User data: $json")
}
