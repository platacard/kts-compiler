#!/usr/bin/env kotlin

@file:Repository("https://nexus.diftech.org/repository/commons-jvm")
@file:DependsOn("dif.tech:ci-tools:3.3.9")
@file:DependsOn("dif.tech:utils:2.2.0")
@file:Suppress("PropertyName")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default

class TestCommand : CliktCommand() {
    private val name: String by argument(
        name = "name",
        help = "Name to greet"
    ).default("World")

    override fun run() {
        println("Hello, $name!")
    }
}

TestCommand().main(args)
