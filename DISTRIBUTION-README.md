# KTS Compiler Distribution

This distribution contains everything you need to compile and validate Kotlin Script (.main.kts) files.

## 📦 Contents

```
kts-compiler/
├── lib/
│   └── kts-compiler.jar          # Main JAR file
├── bin/
│   └── check-kts-scripts.main.kts # KTS script for CI/CD
└── docs/
    └── README.md                 # Full documentation
```

## 🚀 Quick Start

### Method 1: Direct JAR Usage (Recommended)
```bash
# Check a single script
java -Dkotlin.script.classpath=lib/kts-compiler.jar -jar lib/kts-compiler.jar script.main.kts

# Check all scripts in a directory
java -Dkotlin.script.classpath=lib/kts-compiler.jar -jar lib/kts-compiler.jar /path/to/scripts/
```

### Method 2: KTS Script (for CI/CD)
```bash
# Requires kotlinc to be installed
kotlinc -script bin/check-kts-scripts.main.kts -- script.main.kts
kotlinc -script bin/check-kts-scripts.main.kts -- /path/to/scripts/
```

## 🔧 Requirements

- **Java 11 or higher** - Required for running the JAR
- **Kotlin Compiler** - Only required for Method 2 (KTS script)

## 📋 Usage Examples

### Check Single Script
```bash
java -Dkotlin.script.classpath=lib/kts-compiler.jar -jar lib/kts-compiler.jar my-script.main.kts
```

### Check Multiple Scripts
```bash
java -Dkotlin.script.classpath=lib/kts-compiler.jar -jar lib/kts-compiler.jar script1.main.kts script2.main.kts script3.main.kts
```

### Check All Scripts in Directory
```bash
java -Dkotlin.script.classpath=lib/kts-compiler.jar -jar lib/kts-compiler.jar /path/to/your/scripts/
```

### CI/CD Integration
```bash
# In your CI pipeline
kotlinc -script bin/check-kts-scripts.main.kts -- /path/to/scripts/
```

## 🎯 What It Does

The KTS Compiler validates that your `.main.kts` scripts:
- ✅ **Compile successfully** - No syntax errors
- ✅ **Resolve dependencies** - All `@DependsOn` annotations work
- ✅ **Handle transitive dependencies** - Dependencies of dependencies
- ✅ **Support Maven repositories** - Including private repositories

## 📊 Output

The compiler provides detailed output:
```
=== KTS COMPILER REPORT ===
Found 3 script(s) to check
Directory: /path/to/scripts/

Compiling: /path/to/scripts/script1.main.kts
✅ Success: script1.main.kts

Compiling: /path/to/scripts/script2.main.kts
❌ Error: script2.main.kts
ERROR Unresolved reference 'someFunction' (script2.main.kts:15:8)

=== SUMMARY ===
Total scripts: 3
✅ Successful: 2
❌ Failed: 1
📊 Success rate: 66%
```

## 🛠️ Troubleshooting

### JAR Not Found
If you get "JAR not found" error:
1. Ensure you're running from the distribution directory
2. Check that `lib/kts-compiler.jar` exists
3. Verify file permissions

### Java Version Issues
If you get Java version errors:
1. Check Java version: `java -version`
2. Ensure Java 11+ is installed
3. Update `JAVA_HOME` if needed

### Permission Denied
If you get permission errors, ensure the JAR file has read permissions:
```bash
chmod +r lib/kts-compiler.jar
```

## 🔗 Integration

### GitHub Actions
```yaml
- name: Check KTS Scripts
  run: |
    unzip kts-compiler.zip
    java -Dkotlin.script.classpath=kts-compiler/lib/kts-compiler.jar -jar kts-compiler/lib/kts-compiler.jar /path/to/scripts/
```

### GitLab CI
```yaml
script:
  - unzip kts-compiler.zip
  - java -Dkotlin.script.classpath=kts-compiler/lib/kts-compiler.jar -jar kts-compiler/lib/kts-compiler.jar /path/to/scripts/
```

### Jenkins
```groovy
stage('Check KTS Scripts') {
    steps {
        sh 'unzip kts-compiler.zip'
        sh 'java -Dkotlin.script.classpath=kts-compiler/lib/kts-compiler.jar -jar kts-compiler/lib/kts-compiler.jar /path/to/scripts/'
    }
}
```

## 📚 More Information

For detailed documentation, see `docs/README.md`.

For issues and contributions, visit the project repository.
