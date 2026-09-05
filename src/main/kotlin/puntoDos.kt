package com.test



fun main() {
    println("--- PARTE A: Analizador de datos mixtos ---")
    val datos: List<Any?> = listOf(15, "42", 3.5, "hola", true, null, -8, "7.5", 100)
    var sumaTotal = 0.0

    // Recorrido con withIndex()
    for ((index, valor) in datos.withIndex()) {
        val tipo = clasificar(valor)
        val numero = aNumero(valor)

        // Uso del operador Elvis para el texto "no aplica"
        val numeroStr = numero?.toString() ?: "no aplica"

        // Si numero no es null, lo convertimos a Int para evaluar su rango. Si es null, Elvis devuelve "no aplica"
        val rangoStr = numero?.toInt()?.let { rangoDe(it) } ?: "no aplica"

        println("[$index] $valor -> $tipo | numérico: $numeroStr | rango: $rangoStr")

        // Sumatoria manual sin usar sum() ni filter()
        if (numero != null) {
            sumaTotal += numero
        }
    }
    println("\nSuma de valores convertibles a número: $sumaTotal")


    println("\n--- PARTE B: Ciclos y arreglos ---")

    // 1. Arreglo vacío de 10 posiciones y llenado con números pares usando step
    val arreglo = arrayOfNulls<Int>(10)
    var posicion = 0
    for (i in 2..20 step 2) {
        arreglo[posicion] = i
        posicion++
    }

    // 2. Recorrido con indices imprimiendo solo posiciones impares
    print("Posiciones impares del arreglo: ")
    for (i in arreglo.indices) {
        if (i % 2 != 0) { // Verifica si el índice es impar
            print("[Índice $i: ${arreglo[i]}] ")
        }
    }
    println()

    // 3. Cuenta regresiva en una sola línea separada por comas
    println("Cuenta regresiva: ")
    val cuentaRegresiva = mutableListOf<String>()
    for (i in 20 downTo 0 step 5) {
        cuentaRegresiva.add(i.toString())
    }
    for (cuenta in cuentaRegresiva) {
        print("${cuenta}, ")
    }

    println(" ")


    //4. EXPLICACIÓN EN COMENTARIO:

    for (i in 0 until arreglo.size){
        print("${arreglo[i]} ")
    }
println(" ")

    for (i in 0..arreglo.size){
        print("${arreglo[i]} ")
    }

    /* ¿Cuál es la diferencia entre for (i in 0..arreglo.size) y for (i in 0 until arreglo.size)?
    *
    * - '0..arreglo.size' crea un rango CERRADO. Si el arreglo tiene tamaño 10, el ciclo intentará
    *   recorrer los índices del 0 al 10 (incluyendo el 10). Esta opción FALLA y arroja un error
    *   "ArrayIndexOutOfBoundsException" porque el índice máximo válido de un arreglo de tamaño 10 es 9.
    *
    * - '0 until arreglo.size' crea un rango SEMIABIERTO (excluye el último valor).
    *   Si el tamaño es 10, iterará del 0 al 9, ajustándose perfectamente a los índices reales
    *   del arreglo sin salirse de los límites.
    */

}

fun clasificar(valor: Any?): String {
    return when (valor) {
        is Int -> "entero"
        is Double -> "decimal"
        is String -> "texto"
        is Boolean -> "booleano"
        null -> "desconocido"
        else -> "desconocido"
    }
}

fun aNumero(valor: Any?): Double? {
    return when (valor) {
        is Int -> valor.toDouble()
        is Double -> valor
        is String -> try {
            // Se utiliza try como expresión
            valor.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
        else -> null
    }
}

fun rangoDe(numero: Int): String {
    return when (numero) {
        // Usamos !in para demostrar su uso, excluyendo los números positivos y el 0
        !in 0..Int.MAX_VALUE -> "negativo"
        in 0..9 -> "dígito"
        in 10..99 -> "decena"
        else -> "grande"
    }
}

