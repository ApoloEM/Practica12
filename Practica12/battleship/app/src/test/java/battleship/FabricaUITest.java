package battleship;

import org.junit.Test;
import static org.junit.Assert.*;

public class FabricaUITest {

    @Test
    public void testSeleccionConsola() {
        // Probamos inputs que deberían dar Consola
        assertTrue(FabricaUI.crearInterfaz("consola") instanceof InterfazConsola);
        assertTrue(FabricaUI.crearInterfaz("texto") instanceof InterfazConsola);
        assertTrue(FabricaUI.crearInterfaz("") instanceof InterfazConsola);
        assertTrue(FabricaUI.crearInterfaz(null) instanceof InterfazConsola);
    }

    @Test
    public void testSeleccionGrafica() {
        // Probamos inputs que deberían dar GUI
        // Nota: Este test podría fallar en entornos sin pantalla (CI/CD), 
        // pero en tu PC funcionará.
        try {
            assertTrue(FabricaUI.crearInterfaz("gui") instanceof InterfazGrafica);
            assertTrue(FabricaUI.crearInterfaz("GRAFICA") instanceof InterfazGrafica);
            assertTrue(FabricaUI.crearInterfaz("g") instanceof InterfazGrafica);
        } catch (java.awt.HeadlessException e) {
            System.out.println("Entorno sin pantalla detectado: Saltando test de instanciación GUI");
        }
    }
}