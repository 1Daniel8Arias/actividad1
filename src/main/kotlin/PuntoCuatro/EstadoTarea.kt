package com.test.PuntoCuatro

sealed class EstadoTarea {
    data object Pendiente : EstadoTarea()
    data class EnProgreso(val porcentaje: Int): EstadoTarea()
    data class Completada(val fechaFinalizacion: String): EstadoTarea()
    data class Cancelada(val motivo: String): EstadoTarea()
}