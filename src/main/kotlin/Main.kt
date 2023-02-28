import kotlin.reflect.KFunction

val Options = mapOf<String,Option>("help" to Option(::help,"Shows help", listOf(), listOf()),"about" to Option(::about,"Shows about message",
    listOf(), listOf()))
var Usage: String = ""
var index: Int = 0
fun main(args: Array<String>) {
    for (it in Options.values) {
        it.IntegrityCheck()
    }
        if (args.isNotEmpty()) {
            if (Options.containsKey(args[0])) {
                Options[args[0]]?.run(args)
            } else {
                println("There's no such an option")
            }
        } else {
            println("No option provided")
        }

}

fun about(Parameters: ParameterPack) {
    println("FENIX - Package manager built to be fast")
    println()
    println("version: 0.0.2")
}


fun help(Parameters: ParameterPack) {
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

fun install(Parameters: ParameterPack) {
    TODO()
}

fun remove(Parameters: ParameterPack) {
    TODO()
}

data class Option(val func: KFunction<Unit>,val description: String,val arg_types: List<String>,val arg_names: List<String>) {
    fun run(arguments: Array<String>) {
        var Parameters: MutableList<String> = mutableListOf()
        var AdditionalParameters: MutableMap<String,String> = mutableMapOf()
        for (it in arguments) {
            if (it != arguments[0]) {
                if (it[0] == '-' && it[1] == '-') {
                    if (it.contains('=')) {
                        AdditionalParameters.put(it.substring(2).split('=')[0],it.split('=')[1])

                    } else {
                        AdditionalParameters.put(it.substring(2),"true")
                    }

                } else {
                    Parameters.add(it)
                }
            }
        }

        var indexOfArg = 0
        var IntList: MutableList<Int> = mutableListOf()
        var DoubleList: MutableList<Double> = mutableListOf()
        var StringList: MutableList<String> = mutableListOf()
        for (it in arg_types) {
            when (it) {
                "Int" -> IntList.add(Parameters[indexOfArg].toInt())
                "Double" -> DoubleList.add(Parameters[indexOfArg].toDouble())
                "String" -> StringList.add(Parameters[indexOfArg].toString())
            }
        }
        func.call(ParameterPack(arg_types,IntList,DoubleList,StringList,AdditionalParameters))
    }

    fun IntegrityCheck() {
            if (arg_types.size != arg_names.size) {
                throw Exception("Option data is not completed.")
            }

    }

}

data class ParameterPack(val arg_types: List<String>,val IntList: List<Int>, val DoubleList: List<Double>, val StringList: List<String>, val AdditionalParameters: Map<String, String>) {
    fun get(index: Int): String {
        return when(arg_types[index]) {
            "Int" -> IntList[index].toString()
            "Double" -> DoubleList[index].toString()
            "String" -> StringList[index]
            else -> "NoArg"
        }
    }

    fun getAdditionalParameter(indexOfAdditionalParameter: String): String? {
        return AdditionalParameters[indexOfAdditionalParameter]
    }
}

