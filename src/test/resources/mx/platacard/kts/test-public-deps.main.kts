#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
@file:DependsOn("com.github.ajalt:clikt:4.4.0")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import kotlinx.coroutines.runBlocking

class TestPublicDepsCommand : CliktCommand() {
    private val name: String by argument(
        name = "name",
        help = "Name to greet"
    ).default("World")

    override fun run() = runBlocking {
        println("Hello, $name from coroutines!")
    }
}

TestPublicDepsCommand().main(args)
