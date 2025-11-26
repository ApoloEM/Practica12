package battleship;

import java.util.Scanner;

public class InterfazConsola implements IInterfazUsuario {
    private final Scanner scanner;

    public InterfazConsola() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }

    @Override
    public String leerLinea(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    @Override
    public Posicion leerCoordenada() {
        while (true) {
            System.out.print("Ingresa coordenadas (fila,columna): ");
            String input = scanner.nextLine();
            try {
                String[] partes = input.split(",");
                int f = Integer.parseInt(partes[0].trim());
                int c = Integer.parseInt(partes[1].trim());
                
                if (f >= 0 && f < 10 && c >= 0 && c < 10) {
                    return new Posicion(f, c);
                } else {
                    System.out.println("Coordenadas fuera de rango (0-9).");
                }
            } catch (Exception e) {
                System.out.println("Formato inválido. Ejemplo: 5,5");
            }
        }
    }

    @Override
    public void mostrarTableros(Tablero propio, Tablero enemigo) {
        System.out.println("\n=== TU TABLERO ===");
        System.out.println(propio.toString(false));
        
        System.out.println("\n=== TABLERO ENEMIGO ===");
        System.out.println(enemigo.toString(true));
    }

    @Override
    public void mostrarResultado(ResultadoDisparo res, boolean esMio) {
        String actor = esMio ? "¡Disparaste y... " : "¡El enemigo disparó y... ";
        String efecto = "";
        
        switch (res.getTipo()) {
            case AGUA: efecto = "cayó al AGUA!"; break;
            case IMPACTO: efecto = "IMPACTÓ un barco!"; break;
            case HUNDIDO: efecto = "HUNDIÓ el " + res.getNombreBarcoHundido() + "!"; break;
            case YA_DISPARADO: efecto = "ya se había disparado ahí."; break;
            default: efecto = "hubo un error."; break;
        }
        System.out.println(actor + efecto);
    }
}