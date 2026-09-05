package com.test.PuntoSeis
import kotlinx.coroutines.*




// --- 1. Funciones Suspendidas (Simulación de servidor) ---

suspend fun obtenerPerfil(): String {
    delay(1500L)
    return "Usuario123"
}

suspend fun obtenerNotificaciones(): Int {
    delay(1000L)
    return 8
}

suspend fun obtenerMensajes(): Int {
    delay(2000L)
    return 3
}

// --- 2. Función Principal (runBlocking) ---

fun main() = runBlocking {
    println("=== MODO SECUENCIAL ===")
    val inicioSecuencial = System.currentTimeMillis()

    // Cada función espera a que termine la anterior
    val perfil = obtenerPerfil()
    val notificaciones = obtenerNotificaciones()
    val mensajes = obtenerMensajes()

    val finSecuencial = System.currentTimeMillis()
    val tiempoSecuencial = finSecuencial - inicioSecuencial
    println("Datos cargados: $perfil, Notificaciones: $notificaciones, Mensajes: $mensajes")
    println("Tiempo secuencial: $tiempoSecuencial ms\n")

    println("=== MODO PARALELO ===")

    // Lanzamos la corutina que muestra el progreso
    val jobCargando = launch {
        while (isActive) { // Verifica si la corutina no ha sido cancelada
            println("Cargando...")
            delay(300L)
        }
    }

    val inicioParalelo = System.currentTimeMillis()

    // async lanza las consultas al mismo tiempo
    val perfilAsync = async { obtenerPerfil() }
    val notificacionesAsync = async { obtenerNotificaciones() }
    val mensajesAsync = async { obtenerMensajes() }

    // await() pausa esta línea hasta que lleguen los resultados de todas
    val perfilParalelo = perfilAsync.await()
    val notifParalelo = notificacionesAsync.await()
    val msjParalelo = mensajesAsync.await()

    val finParalelo = System.currentTimeMillis()
    val tiempoParalelo = finParalelo - inicioParalelo

    // Cancelamos el "Cargando..." ahora que ya tenemos los datos
    jobCargando.cancel()

    println("\nDatos cargados: $perfilParalelo, Notificaciones: $notifParalelo, Mensajes: $msjParalelo")
    println("Tiempo paralelo: $tiempoParalelo ms")

    println("\n=== RESULTADOS ===")
    val diferencia = tiempoSecuencial - tiempoParalelo
    println("¡El modo paralelo fue $diferencia ms más rápido!")

    /*
     * EXPLICACIÓN BASADA EN TUS RESULTADOS (4527 ms vs 2031 ms):
     *
     * En el modo SECUENCIAL, el hilo se bloquea esperando a que termine cada función
     * antes de iniciar la siguiente. La suma teórica es:
     * 1500ms + 1000ms + 2000ms = 4500 ms.
     * Los 27 ms adicionales (4527 ms) son el tiempo mínimo que tarda el procesador
     * en ejecutar las líneas de código e imprimir en consola (overhead).
     *
     * En el modo PARALELO usando 'async', las tres peticiones se disparan al mismo tiempo.
     * Como corren simultáneamente, el programa solo tiene que esperar a que termine
     * la petición MÁS LENTA (obtenerMensajes = 2000ms).
     * Las otras dos (1500ms y 1000ms) se completan de fondo mientras esperamos esa.
     * Los 31 ms adicionales (2031 ms) corresponden al pequeño esfuerzo que hace Kotlin
     * por debajo para crear, lanzar y coordinar las múltiples corutinas a la vez.
     */
}