#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
@file:Suppress("PropertyName")

import kotlinx.coroutines.runBlocking

class TestRealScriptCommand {
    private val postfix: String = "main"

    fun run() = runBlocking {
        println("Testing script with dependencies")
        println("Postfix: $postfix")
    }
}

TestRealScriptCommand().run()
