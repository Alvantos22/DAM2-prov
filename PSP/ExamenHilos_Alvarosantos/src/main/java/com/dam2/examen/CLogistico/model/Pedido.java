package com.dam2.examen.CLogistico.model;

public record Pedido(
        int id,
        long tiempoLlegada,
        Prioridad prioridad
) {
}
