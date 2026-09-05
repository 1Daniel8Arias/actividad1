package com.test.PuntoCuatro

fun EstadoTarea.esFinal(): Boolean {
    return when (this) {
        is EstadoTarea.Completada, is EstadoTarea.Cancelada -> true
        EstadoTarea.Pendiente, is EstadoTarea.EnProgreso -> false
    }
}

fun describir(tarea: Tarea): String {
    // Guardamos el prefijo para reutilizarlo
    val prefijo = "[${tarea.prioridad.name}] ${tarea.titulo}:"

    // 'when' usado como expresión. No necesita 'else' por ser una sealed class.
    return when (val e = tarea.estado) {
        EstadoTarea.Pendiente -> "$prefijo pendiente"
        is EstadoTarea.EnProgreso -> "$prefijo en progreso (${e.porcentaje}%)"
        is EstadoTarea.Completada -> "$prefijo completada el ${e.fechaFinalizacion}"
        is EstadoTarea.Cancelada -> "$prefijo cancelada por \"${e.motivo}\""

        /*
         * --- DOCUMENTACIÓN DE ERROR DEL COMPILADOR ---
         * Si comentas la rama de 'EstadoTarea.Cancelada' de arriba, el compilador
         * marcará la palabra 'when' en rojo y reportará el siguiente error exacto:
         *
         * "‘when’ expression must be exhaustive, add necessary 'is Cancelada' branch or 'else' branch instead."
         *
         * ¿Por qué? Porque al ser una sealed class, Kotlin conoce TODAS las subclases que existen.
         * Al usar el 'when' para retornar un valor (String), el compilador te obliga a cubrir
         * matemáticamente todos los escenarios posibles, dándote seguridad absoluta contra errores.
         */
    }
}

// 6. Función para avanzar de estado aplicando reglas de negocio
fun avanzar(tarea: Tarea, incremento: Int): Tarea {
    // Si ya terminó, retornamos la tarea intacta
    if (tarea.estado.esFinal()) return tarea

    val nuevoEstado = when (val e = tarea.estado) {
        EstadoTarea.Pendiente -> {
            // Evitamos que supere el 100% en el primer paso si el incremento es gigante
            if (incremento >= 100) EstadoTarea.Completada("2026-01-01")
            else EstadoTarea.EnProgreso(incremento)
        }
        is EstadoTarea.EnProgreso -> {
            val suma = e.porcentaje + incremento
            if (suma >= 100) {
                EstadoTarea.Completada("2026-01-01")
            } else {
                EstadoTarea.EnProgreso(suma)
            }
        }
        // Exigido por el compilador para hacer el when exhaustivo
        is EstadoTarea.Completada, is EstadoTarea.Cancelada -> e
    }

    // Usamos copy() propio de las data classes para crear un clon modificando solo el estado
    return tarea.copy(estado = nuevoEstado)
}
fun main() {
    var miTarea = Tarea(
        titulo = "Entregar informe",
        descripcion = "Informe final de ventas",
        prioridad = Prioridad.ALTA,
        estado = EstadoTarea.Pendiente
    )

    println("--- INICIO DE TAREA ---")
    println("¿Es urgente?: ${miTarea.prioridad.esUrgente()}")
    println(describir(miTarea))
    println("-----------------------")

    // Ciclo hasta que la tarea alcance un estado final (Completada o Cancelada)
    val incrementoFijo = 40
    var paso = 1

    while (!miTarea.estado.esFinal()) {
        println("Aplicando avance #$paso (+$incrementoFijo%)...")
        miTarea = avanzar(miTarea, incrementoFijo)
        println(describir(miTarea))
        paso++
    }

    println("-----------------------")
    println("La tarea ha llegado a su estado final. Proceso terminado.")
}