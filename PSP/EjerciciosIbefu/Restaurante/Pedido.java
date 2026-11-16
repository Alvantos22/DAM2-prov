package Restaurante;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pedido {

    private final String nombre;
    private final int id;
    private final int tiempoPreparacion;
    private final TipoPlato tipoPlato;

    public Pedido(TipoPlato tipoPlato, int id) {
        this.id = id;
        this.tipoPlato = tipoPlato;
        this.nombre = id+"_"+tipoPlato.name();
        this.tiempoPreparacion = tipoPlato.getTiempoPreparacion();
    }


}
