#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.fasterxml.jackson.core:jackson-databind:2.16.1")
@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

data class TestData(val message: String, val number: Int)

val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
val testData = TestData("Hello from Jackson!", 42)
val json = mapper.writeValueAsString(testData)

println("JSON: $json")
