#!/usr/bin/env kotlin

@file:Repository("https://nexus.diftech.org/repository/commons-jvm")
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

import kotlinx.coroutines.runBlocking

runBlocking {
    println("Hello from Maven Central fallback with coroutines!")
}
