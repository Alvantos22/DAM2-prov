package Parking;

public enum TipoCoches {
    NORMAL(1),  // 1€/minuto
    VIP(2);     // 2€/minuto

    private final int tarifaPorMinuto;

    TipoCoches(int tarifaPorMinuto) {
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public double getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }
}
