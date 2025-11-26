package battleship;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class JuegoBattleshipTest {

    private JuegoBattleship juego;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        juego = new JuegoBattleship();
        juego.colocarBarcosAutomaticamente();
    }

    @Test
    public void testColocarBarcosAutomaticamente() {
        int celdasOcupadas = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!juego.obtenerTipoBarcoEn(i, j).equals("DESCONOCIDO")) {
                    celdasOcupadas++;
                }
            }
        }
        assertEquals(17, celdasOcupadas);
    }

    @Test
    public void testEstaBarcoHundido() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String tipo = juego.obtenerTipoBarcoEn(i, j);
                if (tipo.equals("DESTRUCTOR")) {
                    juego.recibirDisparo(i, j);
                }
            }
        }
        assertTrue(juego.estaBarcoHundido("DESTRUCTOR"));
        assertFalse(juego.estaBarcoHundido("PORTAAVIONES"));
    }

    @Test
    public void testMostrarTableroEnemigo() {
        System.setOut(new PrintStream(outContent));
        juego.mostrarTableroEnemigo();
        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("TABLERO ENEMIGO"));
    }

    @Test
    public void testMostrarTableroPropio() {
        System.setOut(new PrintStream(outContent));
        juego.mostrarTableroPropio();
        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("TU TABLERO"));
        assertTrue(outContent.toString().contains("PORTAAVIONES"));
    }

    @Test
    public void testObtenerTipoBarcoEn() {
        boolean barcoEncontrado = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String tipo = juego.obtenerTipoBarcoEn(i, j);
                if (!tipo.equals("DESCONOCIDO")) {
                    barcoEncontrado = true;
                    assertNotNull(tipo);
                    break;
                }
            }
            if (barcoEncontrado) break;
        }
        assertTrue(barcoEncontrado);
    }

    @Test
    public void testRecibirDisparo() {
        boolean impacto = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!juego.obtenerTipoBarcoEn(i, j).equals("DESCONOCIDO")) {
                    impacto = juego.recibirDisparo(i, j);
                    assertTrue(impacto);
                    return; 
                }
            }
        }
    }

    @Test
    public void testRegistrarFallo() {
        juego.registrarFallo(0, 0);
        assertTrue(juego.yaDisparado(0, 0));
    }

    @Test
    public void testRegistrarImpacto() {
        juego.registrarImpacto(1, 1);
        assertTrue(juego.yaDisparado(1, 1));
    }

    @Test
    public void testTodosBarcosHundidos() {
        assertFalse(juego.todosBarcosHundidos());
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!juego.obtenerTipoBarcoEn(i, j).equals("DESCONOCIDO")) {
                    juego.recibirDisparo(i, j);
                }
            }
        }
        assertTrue(juego.todosBarcosHundidos());
    }

    @Test
    public void testYaDisparado() {
        assertFalse(juego.yaDisparado(5, 5));
        juego.registrarImpacto(5, 5);
        assertTrue(juego.yaDisparado(5, 5));
    }
}