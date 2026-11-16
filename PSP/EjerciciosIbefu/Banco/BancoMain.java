package Banco;

public class BancoMain {
    public static void main(String[] args) {
        IBanco bancoSync = new TransaccionesSync();
        CuentaBancaria servicios = new CuentaBancaria(bancoSync);
        IBanco bancoLock= new TransaccionesLock();
        CuentaBancaria serviciosLock = new CuentaBancaria(bancoLock);


        servicios.work();
        serviciosLock.work();
    }
}
