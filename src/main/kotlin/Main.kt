val available = mapOf("help" to ::help,"about" to ::about)
val help_descriptions = mapOf<String,String>("help" to "shows this information","about" to "Shows details about Fenix")
val help_examples = mapOf<String,String>("help" to "fenix help","about" to "fenix about")
val args_counts = mapOf<String,Int>("help" to 0,"about" to 0)
val arg_types = mapOf<String,List<String>>("help" to listOf<String>(),"about" to listOf<String>())

fun main(args: Array<String>) {
    if (available.size == help_descriptions.size && help_descriptions.size == help_examples.size) {
        if (args.isNotEmpty()) {
            if (available.containsKey(args[0])) {
                available[args[0]]?.invoke()
            } else {
                println("There's no such an option")
            }
        } else {
            println("No option provided")
        }
    } else {
        println("Not enough option info")
    }

}

fun about() {
    println("FENIX - Package manager built to be fast")
    println()
    println("hourly version ↓")
    println("   feature ready v0")
    println("   hourly v1")
}

fun help() {
    println("HELP")
    for (it in available.keys) {
            println()
            println("   ${it}: ${help_descriptions[it]}")
            println("      usage: ${help_examples[it]}")
    }
}

fun install() {
    TODO()
}

fun remove() {
    TODO()
}

