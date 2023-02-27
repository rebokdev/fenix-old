import kotlin.reflect.KFunction

val Options = mapOf<String,Option>("help" to Option(::help,"Shows help", listOf("hello"), listOf()),"about" to Option(::about,"Shows about message",
    listOf(), listOf()))
var Usage: String = ""
var index: Int = 0
fun main(args: Array<String>) {
    IntegrityCheck(*Options.values.toTypedArray())
        if (args.isNotEmpty()) {
            if (Options.containsKey(args[0])) {
                Options[args[0]]?.func?.call()
            } else {
                println("There's no such an option")
            }
        } else {
            println("No option provided")
        }

}

fun about() {
    println("FENIX - Package manager built to be fast")
    println()
    println("version: 0.0.1")
}

fun help() {
    println("HELP")
    for (it in Options.keys) {
        println()
        if (it == "help") {
            println("   ${it}: Shows this help message")
        } else {
            println("   ${it}: ${Options[it]?.description}")
        }

        Usage = "    Usage: ${it} "
        Options[it]?.arg_names?.let { it1 ->
            repeat(it1.size) {
                Usage = "[${it1[index]}]"
                index++

            }
        }
        index = 0
        println(Usage)
    }
}

fun install() {
    TODO()
}

fun remove() {
    TODO()
}

data class Option(val func: KFunction<Unit>,val description: String,val arg_types: List<String>,val arg_names: List<String>)

fun IntegrityCheck(vararg Options: Option) {
    for (it in Options) {
        if (it.arg_types.size != it.arg_names.size) {
            throw Exception("Option data is not completed. More info: at ${it.func} Cause: ${it.arg_names} is not equals to ${it.arg_types} ")
        }
    }

}

