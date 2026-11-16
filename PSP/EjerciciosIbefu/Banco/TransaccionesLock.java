package Banco;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransaccionesLock implements IBanco {
    private final Lock lock = new ReentrantLock();
    private double saldo=1000;
    private List<String> historial = new java.util.ArrayList<>();

    @Override
    public boolean retirar(double cantidad) {
        lock.lock();
        try{
            if(saldo<cantidad){
                historial.add("No se ha podido retirar "+cantidad+" por falta de saldo");
                return false;
            }else {
                saldo-=cantidad;
                historial.add("Se ha retirado "+cantidad);
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public void ingresar(double cantidad) {
        lock.lock();
        try{
            saldo+=cantidad;historial.add("Se ha ingresado "+cantidad);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    @Override
    public double consultarSaldo() {
        lock.lock();
        try {
            return saldo;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();

        }
        return saldo;
    }

    @Override
    public List<String> obtenerHistorial() {
        return historial;
    }
}
