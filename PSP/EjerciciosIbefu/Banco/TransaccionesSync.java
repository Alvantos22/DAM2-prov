package Banco;

import java.util.List;

public class TransaccionesSync implements IBanco{

    private double saldo=1000;
    private List<String> historial = new java.util.ArrayList<>();
    @Override
    public synchronized boolean retirar(double cantidad) {
        if(saldo<cantidad){
            historial.add("No se ha podido retirar "+cantidad+" por falta de saldo");
            return false;
        }else {
            saldo-=cantidad;
            historial.add("Se ha retirado "+cantidad);
            return true;
        }
    }

    @Override
    public synchronized void ingresar(double cantidad) {
        saldo+=cantidad;
        historial.add("Se ha ingresado "+cantidad);
    }

    @Override
    public double consultarSaldo() {
        return saldo;
    }

    @Override
    public List<String> obtenerHistorial() {
        return historial;
    }
}
