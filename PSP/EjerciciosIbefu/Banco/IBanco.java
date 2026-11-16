package Banco;

import java.util.List;

public interface IBanco {
    public boolean retirar(double cantidad);
    public void ingresar(double cantidad);
    public double consultarSaldo();
    public List<String> obtenerHistorial();
}
