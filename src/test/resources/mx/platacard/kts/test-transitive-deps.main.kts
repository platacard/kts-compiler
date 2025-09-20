#!/usr/bin/env kotlin

@file:Repository("https://nexus.diftech.org/repository/commons-jvm")
@file:DependsOn("dif.tech:ci-tools:3.3.9")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument

class TestTransitiveCommand : CliktCommand() {
    private val name: String by argument(
        name = "name",
        help = "Name to greet"
    )

    override fun run() {
        println("Hello, $name from transitive dependencies!")
    }
}

TestTransitiveCommand().main(args)
