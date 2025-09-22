#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.squareup.okhttp3:okhttp:4.12.0")

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

val client = OkHttpClient()

val request = Request.Builder()
    .url("https://httpbin.org/json")
    .build()

try {
    client.newCall(request).execute().use { response ->
        if (response.isSuccessful) {
            val body = response.body?.string()
            println("HTTP Response received: ${body?.length ?: 0} characters")
        } else {
            println("HTTP Error: ${response.code}")
        }
    }
} catch (e: IOException) {
    println("Network error: ${e.message}")
}
