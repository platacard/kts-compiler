package org.jetbrains.kotlin.script.examples.simpleMainKts

import kotlin.script.experimental.annotations.KotlinScript

@Suppress("unused")
// The KotlinScript annotation marks a class that can serve as a reference to the script definition for
// `createJvmCompilationConfigurationFromTemplate` call as well as for the discovery mechanism
// The marked class also become the base class for defined script type (unless redefined in the configuration)
@KotlinScript(
    // file name extension by which this script type is recognized by mechanisms built into scripting compiler plugin
    // and IDE support, it is recommendend to use double extension with the last one being "kts", so some non-specific
    // scripting support could be used, e.g. in IDE, if the specific support is not installed.
    fileExtension = "main.kts",
    // the class or object that defines script compilation configuration for this type of scripts
    compilationConfiguration = SimpleMainKtsScriptDefinition::class,
    // the class or object that defines script evaluation configuration for this type of scripts
    evaluationConfiguration = MainKtsEvaluationConfiguration::class,
)
// the class is used as the script base class, therefore it should be open or abstract. Also the constructor parameters
// of the base class are copied to the script constructor, so with this definition the script will require `args` to be
// passed to the constructor, and `args` could be used in the script as a defined variable.
abstract class SimpleMainKtsScript(
    val args: Array<String>,
)
