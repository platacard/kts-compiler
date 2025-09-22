#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
@file:DependsOn("org.apache.commons:commons-lang3:3.13.0")

import org.apache.commons.lang3.StringUtils
import kotlinx.coroutines.runBlocking

val name = if (args.isNotEmpty()) args[0] else "World"

runBlocking {
    val greeting = StringUtils.capitalize("hello, $name from coroutines and apache commons!")
    println(greeting)
}
