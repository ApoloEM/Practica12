package battleship;

public class FabricaUI {
    public static IInterfazUsuario crearInterfaz(String argumento) {
        if (argumento == null) {
            return new InterfazConsola();
        }
        
        String modo = argumento.trim().toLowerCase();
        
        if (modo.equals("gui") || modo.equals("grafica") || modo.equals("g")) {
            return new InterfazGrafica();
        } else {
            return new InterfazConsola();
        }
    }
}