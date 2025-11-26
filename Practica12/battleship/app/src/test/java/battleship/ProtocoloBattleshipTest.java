package battleship;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProtocoloBattleshipTest {

    @Test
    public void testConstruirMensajeDisparo() {
        String resultado = ProtocoloBattleship.construirMensajeDisparo(5, 7);
        assertEquals("DISPARAR|5,7", resultado);
    }

    @Test
    public void testConstruirMensajeResultado() {
        String resImpacto = ProtocoloBattleship.construirMensajeResultado(ProtocoloBattleship.IMPACTO, 2, 3, null);
        assertEquals("IMPACTO|2,3", resImpacto);

        String resHundido = ProtocoloBattleship.construirMensajeResultado(ProtocoloBattleship.HUNDIDO, 4, 4, "SUBMARINO");
        assertEquals("HUNDIDO|4,4|SUBMARINO", resHundido);
    }

    @Test
    public void testParsearMensaje() {
        ProtocoloBattleship.Mensaje m = ProtocoloBattleship.parsearMensaje("DISPARAR|1,1");
        assertEquals("DISPARAR", m.comando);
        assertEquals(1, m.x);
        assertEquals(1, m.y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsearMensajeFormatoInvalido() {
        ProtocoloBattleship.parsearMensaje("DISPARAR|A,B");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsearMensajeNulo() {
        ProtocoloBattleship.parsearMensaje(null);
    }
}