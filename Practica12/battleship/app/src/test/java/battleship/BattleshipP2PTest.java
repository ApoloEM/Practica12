package battleship;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BattleshipP2PTest {

    private final InputStream originalIn = System.in;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    public void testIniciar() {
        String inputSimulado = "JugadorTest\n2\n127.0.0.1\nn\n";
        System.setIn(new ByteArrayInputStream(inputSimulado.getBytes()));

        BattleshipP2P juego = new BattleshipP2P();
        juego.iniciar();
    }

    @Test
    public void testMain() {
        String inputSimulado = "JugadorMain\n2\n127.0.0.1\nn\n";
        System.setIn(new ByteArrayInputStream(inputSimulado.getBytes()));

        BattleshipP2P.main(new String[]{});
    }
}