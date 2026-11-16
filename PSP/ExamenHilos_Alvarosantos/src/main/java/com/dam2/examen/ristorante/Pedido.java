
package com.dam2.examen.ristorante;

public record Pedido(
        int cliente,
        TipoPlato plato,
        long horaCreacionMs,
        Grupo grupo
) {
}
