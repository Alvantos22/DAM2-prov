package org.example;
/*
Se simula una carrera de chapas con N jugadores, cada uno en un hilo. Por rondas,
cada jugador lanza su chapa avanzando una distancia aleatoria entre 1 y 20. Los
lanzamientos de una ronda se hacen de uno en uno y en orden (jugador 0, 1, 2...).
Cuando todos han lanzado, termina la ronda y se muestra la clasificación. Gana quien,
al terminar una ronda, haya alcanzado la meta (100); si hay varios, el que esté más
lejos. Para q el publico siga la partida hay un juez sacando mensajes a una pizarra,
esos mensajes le llegan de una cola de mensajes, que los jugadores al final de cada
tirada llenan con su posición. Al final del turno tb se pondrá un mensaje de fin de turno
y el juez cuando vea ese mensaje en la cola, asyncronamente pedirá a los jugadores
por donde van y mostrará el resumen.
Puntos clave:
- Los turnos se completan siempre.
- La comprobacion de quien gana se hace al final de un turno.
- Solo el juez pone cosas en la pizarra(consola)
- Los mensaje de de la cola de mensajes son de informacion de jugadores, o el de
fin de turno.
- Los hilos tendrán que pararse cuando toque para que el programa se cierre.
- PISTA: como se puede hacer algo que sirva para sincronizar pasos de n hilos
para que ninguno siga antes de que lleguen los demas? Hay un constructor de
ese algo que puede ejecutar una accion(runnable) cada vez que se llegan todos
a cada paso.

 */

import org.example.constantes.Constantes;
import org.example.model.Juez;
import org.example.model.Jugador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;


public class CarreraChapas {
    private List<Thread> jugadores = new ArrayList<>();
    private String[] tablero = new String[Constantes.NUM_CASILLAS];
    private final CountDownLatch contadorJugadores = new CountDownLatch(Constantes.NUM_JUGADORES);
    private List<Jugador> colaJugadores = new ArrayList<>(Constantes.NUM_JUGADORES);
    private Juez juez=new Juez();
    boolean partidaFin=false;

    void main() {

        juez.dice("-- CARRERA CHAPAS --");

       juez.dice("Los jugadores se preparan...");

        prepararJugadores();
        rellenarTablero(tablero);

        if (partidaFin) {
            try {
                contadorJugadores.await();
                for (Thread jugador : jugadores) {
                    jugador.interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return;
        }

        while (!partidaFin) {
            try {
            for (int i = 0; i < colaJugadores.size(); i++) {

                Jugador jugadorActual = colaJugadores.get(i);

                int tiradaActual = tirarChapa();

                juez.dice("jugador-" + i + " tira un dado y sale " + tiradaActual);

                    Thread.sleep(1000);

                jugadorActual.sumarPosicion(tiradaActual);

                juez.dice("Jugador " + i + " va por la posicion " + jugadorActual.getPosicion()
                +"\n");
                Thread.sleep(500);

                if (jugadorActual.getPosicion() >= tablero.length) {
                    juez.dice("GANADOR = " + jugadorActual);
                    partidaFin=true;
                    break;
                }
                contadorJugadores.countDown();
            }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

    }


    public void prepararJugadores() {

            Thread generador = Thread.ofPlatform()
                    .name("generador")
                    .start(() -> generarJugadores());


            try {
                generador.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


    }

    public void generarJugadores() {
        for (int i = 0; i < Constantes.NUM_JUGADORES; i++) {

            colaJugadores.add(new Jugador(i, 0));

        }

    }


    public void rellenarTablero(String[] tablero) {
        Arrays.fill(tablero, "-");
    }

    public int tirarChapa() {
        return ThreadLocalRandom.current().nextInt(1, 21);

    }

}
