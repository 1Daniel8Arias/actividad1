package com.test.PuntoCuatro

enum class Prioridad(val nivel: Int) {
    BAJA(1),
    MEDIA(2),
    ALTA(3),
    CRITICA(4);

    fun esUrgente(): Boolean {
        return nivel >= 3

    }
}
