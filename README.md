# KTS Compiler

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

A lightweight utility for checking the syntax correctness of `.main.kts` (Kotlin Script) files without executing them. This tool is particularly useful for developers working with CI/CD written in Kotlin.

## Features

- **Fast Syntax Checking**: Quickly verifies the syntax of `.main.kts` files without execution.
- **Dependency Resolution**: Supports `@DependsOn` and `@Repository` annotations for dependency management.
- **Command-Line Interface (CLI)**: Easy-to-use CLI for integrating into your development workflow.
- **Continuous Integration Friendly**: Ideal for use in CI/CD pipelines to ensure script quality.
- **Caching**: Intelligent caching of compiled scripts for faster subsequent runs.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)
- [CI/CD Integration](#cicd-integration)

## Installation

### From Source

```bash
git clone <repository-url>
cd kts-compiler
./gradlew build
```

### Using the CLI

After building, you can use the CLI directly:

```bash
./gradlew run --args="path/to/your/script.main.kts"
```

Or use the generated script:

```bash
./build/scripts/kts-compiler path/to/your/script.main.kts
```

## Usage

### Basic Usage

```bash
# Check a single script
./gradlew run --args="script.main.kts"

# Check multiple scripts
./gradlew run --args="script1.main.kts script2.main.kts script3.main.kts"
```

### Exit Codes

- `0` - All scripts compiled successfully
- `1` - One or more scripts failed to compile

### Output Format

The tool provides clear feedback:

```
✅ Success: script.main.kts
❌ Error: failed-script.main.kts
ERROR Expecting an expression (failed-script.main.kts:7:14)
```

## Examples

### Simple Script

```kotlin
#!/usr/bin/env kotlin

println("Hello, World!")
```

### Script with Dependencies

```kotlin
#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

import kotlinx.coroutines.runBlocking

runBlocking {
    println("Hello from coroutines!")
}
```

### Script with CLI Framework

```kotlin
#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.github.ajalt:clikt:4.4.0")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument

class MyCommand : CliktCommand() {
    private val name: String by argument(help = "Name to greet")
    
    override fun run() {
        println("Hello, $name!")
    }
}

MyCommand().main(args)
```

## CI/CD Integration

### Using the Standalone JAR

First, build the standalone JAR:

```bash
./gradlew shadowJar
```

Then use the provided KTS script:

```bash
# Check single script
kotlinc -script check-kts-scripts.main.kts -- script.main.kts

# Check all scripts in directory
kotlinc -script check-kts-scripts.main.kts -- /path/to/scripts/
```

### GitHub Actions

```yaml
name: Check KTS Scripts

on:
  pull_request:
    paths:
      - '**/*.main.kts'

jobs:
  check-scripts:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Build JAR
        run: ./gradlew shadowJar
        
      - name: Check KTS scripts
        run: kotlinc -script check-kts-scripts.main.kts -- /path/to/your/scripts/
```

### GitLab CI

```yaml
check-kts-scripts:
  stage: test
  image: openjdk:17
  script:
    - ./gradlew shadowJar
    - kotlinc -script check-kts-scripts.main.kts -- /path/to/your/scripts/
  only:
    changes:
      - "**/*.main.kts"
```

### Jenkins

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew shadowJar'
            }
        }
        stage('Check KTS Scripts') {
            steps {
                sh 'kotlinc -script check-kts-scripts.main.kts -- /path/to/your/scripts/'
            }
        }
    }
}
```

## Supported Features

- ✅ Kotlin Script syntax validation
- ✅ Dependency resolution via `@DependsOn`
- ✅ Repository configuration via `@Repository`
- ✅ Import statements validation
- ✅ Type checking
- ✅ Compilation error reporting
- ✅ Multiple file processing
- ✅ Caching for performance

## Limitations

- Currently supports only `.main.kts` scripts
- Dependency resolution requires internet access
- Some advanced Kotlin features may not be fully supported

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.

