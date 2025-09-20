#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.apache.commons:commons-lang3:3.13.0")

import org.apache.commons.lang3.StringUtils  // Прямая зависимость: commons-lang3
import org.apache.commons.lang3.ArrayUtils  // Прямая зависимость: commons-lang3
import org.apache.commons.lang3.time.DateUtils  // Прямая зависимость: commons-lang3
import java.util.Date  // Стандартная Java библиотека

// Тестируем Apache Commons Lang3
val text = "hello world"
val capitalized = StringUtils.capitalize(text)
println("Capitalized: $capitalized")

val array = arrayOf(1, 2, 3, 4, 5)
val reversed = ArrayUtils.reverse(array.clone())
println("Reversed array: ${reversed.contentToString()}")

val now = Date()
val truncated = DateUtils.truncate(now, java.util.Calendar.HOUR)
println("Truncated date: $truncated")

println("✅ Apache Commons Lang3 works correctly!")
