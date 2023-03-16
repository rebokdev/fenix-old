import kotlin.reflect.KFunction

val Options = mutableMapOf<String,Option>("help" to Option(::help,"Shows help", listOf(), listOf(), listOf(), mapOf()),"about" to Option(::about,"Shows about message",
    listOf(), listOf(), listOf(), mapOf()), "install" to Option(::install,"Installs local package or remote one", listOf("String"), listOf("package name"), listOf(), mapOf())
)
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
                Usage += "[${it1[index]}]"
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

data class Option(
    val func: KFunction<Unit>,
    val description: String,
    val arg_types: List<String>,
    val arg_names: List<String>, val PossibleParams: List<Any>,
    val AvailableAdditionalParams: Map<String,String>
) {

    fun run(arguments: Array<String>) {
        var Parameters: MutableList<String> = mutableListOf()
        var AdditionalParameters: MutableMap<String,String> = mutableMapOf()
        for (it in arguments) {
            if (it != arguments[0]) {
                if (it[0] == '-' && it[1] == '-') {
                    if (it.contains('=')) {
                        AdditionalParameters[it.substring(2).split('=')[0]] = it.split('=')[1]

                    } else {
                        AdditionalParameters[it.substring(2)] = "true"
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
        var IDs: MutableList<ID> = mutableListOf()
        for (it in arg_types) {
            when (arg_types[indexOfArg]) {
                "Int" -> IDs.add(ID(listOf(indexOfArg,1)))
                "Double" -> IDs.add(ID(listOf(indexOfArg,2)))
                "String" -> IDs.add(ID(listOf(indexOfArg,2)))
                else -> "arg type is not correct"
            }
            when (arg_types[indexOfArg]) {
                "Int" -> IntList.add(Parameters[indexOfArg].toInt())
                "Double" -> DoubleList.add(Parameters[indexOfArg].toDouble())
                "String" -> StringList.add(Parameters[indexOfArg])
                else -> "arg type is not correct"
            }
            index++
        }

        var AdditionalParamsIDs: MutableMap<String, ID> = mutableMapOf()
        var IntAdditionalParams: MutableMap<String, Int> = mutableMapOf()
        var DoubleAdditionalParams: MutableMap<String, Double> = mutableMapOf()
        var StringAdditionalParams: MutableMap<String, String> = mutableMapOf()
        var BoolAdditionalParams: MutableMap<String, Boolean> = mutableMapOf()

        for (it in AvailableAdditionalParams) {
            when (it.key) {
                "Int" -> AdditionalParamsIDs.put(it.key,ID(listOf(1)))
                "Double" -> AdditionalParamsIDs.put(it.key,ID(listOf(2)))
                "String" -> AdditionalParamsIDs.put(it.key,ID(listOf(3)))
                "Boolean" -> AdditionalParamsIDs.put(it.key,ID(listOf(4)))
                else -> println("")
            }
            when (AdditionalParamsIDs[it.key]?.get()?.get(0)) {
                1 -> IntAdditionalParams.put(it.key,it.value.toInt())
                2 -> DoubleAdditionalParams.put(it.key,it.value.toDouble())
                3 -> StringAdditionalParams.put(it.key,it.value)
                4 -> BoolAdditionalParams.put(it.key,it.value.toBoolean())
            }
        }

        func.call(ParameterPack(IDs,arg_types,IntList,DoubleList,StringList,AdditionalParameters,AdditionalParamsIDs,IntAdditionalParams,
            DoubleAdditionalParams,StringAdditionalParams,BoolAdditionalParams))
    }

    fun IntegrityCheck() {
        if (arg_types.size != arg_names.size) {
            throw Exception("Option data is not completed.")
        }

        for (it in arg_types) {
            if (it != "Int" && it != "Double" && it != "String") {
                throw Exception("arg type \"${it}\" is not correct. At function: ${func.name}")
            }
        }

        for (it in AvailableAdditionalParams.keys) {
            if (it != "Int" && it != "Double" && it != "String" && it != "Boolean") {
                throw Exception("Addiyional parameter type \"${it}\" is not correct. At function: ${func.name}")
            }
        }


    }

}

data class ParameterPack(val IDs: List<ID>,
                         val arg_types: List<String>, val IntList: List<Int>, val DoubleList: List<Double>,
                         val StringList: List<String>, val AdditionalParams: Map<String,String> ,val AdditionalParamsIDs: Map<String, ID>,
                         val IntAdditionalParams: Map<String, Int>, val DoubleAdditionalParams: Map<String, Double>, val StringAdditionalParams: Map<String,String>,
                         val BoolAdditionalParams: Map<String, Boolean>) {

    fun get(index: Int): String {
        return when(arg_types[index]) {
            "Int" -> IntList[IDs[index].get()[0]].toString()
            "Double" -> DoubleList[IDs[index].get()[0]].toString()
            "String" -> StringList[IDs[index].get()[0]]
            else -> "NoArg"
        }
    }


    fun <T>getAdditionalParameter(indexOfAdditionalParameter: String): Comparable<*>? {
        return when ((AdditionalParamsIDs[indexOfAdditionalParameter]?.get()?.get(0))) {
            1 -> IntAdditionalParams[indexOfAdditionalParameter]
            2 -> DoubleAdditionalParams[indexOfAdditionalParameter]
            3 -> StringAdditionalParams[indexOfAdditionalParameter]
            4 -> BoolAdditionalParams[indexOfAdditionalParameter]
            else -> "NoParam"
        }
    }
}
data class ID(val Numbers: List<Int> ) {
    fun get(): List<Int> {
        return Numbers
    }
}
