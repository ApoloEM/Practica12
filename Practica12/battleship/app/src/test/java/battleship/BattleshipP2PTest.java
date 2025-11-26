package battleship;

import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;
import java.util.Queue;
import static org.junit.Assert.*;

public class BattleshipP2PTest {

    // --- CLASES MOCK (Simuladores) ---
    
    // Simula la red. Cuando se le acaban los datos, lanza error para detener el juego.
    class MockGestorRed implements IGestorRed {
        public String ultimoMensajeEnviado;
        public Queue<String> mensajesParaRecibir = new LinkedList<>();

        @Override
        public void enviarMensaje(String mensaje) {
            this.ultimoMensajeEnviado = mensaje;
        }

        @Override
        public String recibirMensaje() {
            if (mensajesParaRecibir.isEmpty()) {
                // "Pastilla de veneno": Rompemos el bucle infinito del juego lanzando una excepción
                throw new RuntimeException("Fin simulado del test");
            }
            return mensajesParaRecibir.poll();
        }

        public void conectarServidor(int puerto) {}
        public void conectarCliente(String ip, int puerto) {}
        public void cerrar() {}
    }

    class MockUI implements IInterfazUsuario {
        public Queue<String> entradasSimuladas = new LinkedList<>();
        
        @Override
        public Posicion leerCoordenada() {
            String s = entradasSimuladas.poll();
            if (s == null) return new Posicion(0,0); // Default por seguridad
            String[] parts = s.split(",");
            return new Posicion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        
        @Override
        public String leerLinea(String prompt) {
            return entradasSimuladas.poll();
        }

        // Métodos vacíos (No nos importan para esta prueba)
        public void mostrarMensaje(String m) {}
        public void mostrarError(String e) {}
        public void mostrarTableros(Tablero p, Tablero e) {}
        public void mostrarResultado(ResultadoDisparo r, boolean m) {}
    }

    // --- TESTS ---

    private MockGestorRed red;
    private MockUI ui;
    private BattleshipP2P controlador;

    @Before
    public void setUp() {
        red = new MockGestorRed();
        ui = new MockUI();
        controlador = new BattleshipP2P(red, ui);
    }

    @Test
    public void testFlujoTurnoPropio() throws InterruptedException {
        // PREPARACIÓN DE DATOS
        ui.entradasSimuladas.add("1");    // Elegir modo Servidor
        red.mensajesParaRecibir.add("LISTO"); // El otro jugador dice LISTO
        ui.entradasSimuladas.add("2,3");  // Nosotros disparamos a (2,3)
        red.mensajesParaRecibir.add("IMPACTO|2,3"); // La red responde el resultado
        
        // EJECUCIÓN EN HILO APARTE
        Thread gameThread = new Thread(() -> {
            controlador.iniciar();
        });
        
        gameThread.start();
        
        // Esperamos a que el hilo termine (terminará cuando se acaben los mensajes del Mock)
        gameThread.join(2000); // Esperar máximo 2 segundos
        
        // VERIFICACIÓN
        // Confirmamos que el controlador intentó enviar el disparo correcto
        assertEquals("DISPARAR|2,3", red.ultimoMensajeEnviado);
    }
}