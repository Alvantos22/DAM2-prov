package Restaurante;

public enum TipoPlato {

        ENSALADA(2000),    // 2 segundos
        PASTA(3000),       // 3 segundos
        PIZZA(4000),       // 4 segundos
        CARNE(5000);       // 5 segundos

    private int tiempoPreparacion;

    TipoPlato(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }


}
