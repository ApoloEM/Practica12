package battleship;

import org.junit.Test;
import static org.junit.Assert.*;

public class DominioTest {

    @Test
    public void testCeldaComportamiento() {
        Celda celda = new Celda();
        
        // 1. Estado inicial
        assertEquals('~', celda.getRepresentacion(true)); // Agua oculta
        assertFalse(celda.tieneBarco());
        
        // 2. Colocar barco
        celda.colocarBarco('S');
        assertTrue(celda.tieneBarco());
        assertEquals('~', celda.getRepresentacion(true)); // Sigue oculta al enemigo
        assertEquals('S', celda.getRepresentacion(false)); // Visible para mi
        
        // 3. Recibir impacto
        celda.recibirImpacto();
        assertTrue(celda.estaImpactada());
        assertEquals('X', celda.getRepresentacion(true)); // Tocado
    }

    @Test
    public void testTableroLimites() {
        Tablero tablero = new Tablero();
        
        // Coordenada válida
        assertNotNull(tablero.getCelda(new Posicion(5, 5)));
        
        // Coordenada inválida
        assertNull(tablero.getCelda(new Posicion(10, 10)));
        assertNull(tablero.getCelda(new Posicion(-1, 0)));
    }
    
    @Test
    public void testProtocoloParsing() {
        String mensaje = "DISPARAR|3,4";
        Posicion p = ProtocoloBattleship.parsearCoordenadas(mensaje);
        
        assertEquals(3, p.getFila());
        assertEquals(4, p.getColumna());
        assertEquals("DISPARAR", ProtocoloBattleship.obtenerComando(mensaje));
    }
}