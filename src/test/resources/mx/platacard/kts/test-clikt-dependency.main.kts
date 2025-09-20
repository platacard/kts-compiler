#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.apache.commons:commons-lang3:3.13.0")

import org.apache.commons.lang3.StringUtils

val name = if (args.isNotEmpty()) args[0] else "World"
val greeting = StringUtils.capitalize("hello, $name from Apache Commons!")
println(greeting)
