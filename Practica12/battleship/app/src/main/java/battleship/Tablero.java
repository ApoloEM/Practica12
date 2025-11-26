package battleship;

public class Tablero {
    private final Celda[][] celdas;
    private final int tamanio = 10;

    public Tablero() {
        celdas = new Celda[tamanio][tamanio];
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                celdas[i][j] = new Celda();
            }
        }
    }

    public Celda getCelda(Posicion p) {
        if (!esPosicionValida(p)) return null;
        return celdas[p.getFila()][p.getColumna()];
    }

    public boolean esPosicionValida(Posicion p) {
        return p.getFila() >= 0 && p.getFila() < tamanio &&
               p.getColumna() >= 0 && p.getColumna() < tamanio;
    }

    public int getTamanio() { return tamanio; }

    public String toString(boolean ocultarBarcos) {
        StringBuilder sb = new StringBuilder();
        sb.append("  0 1 2 3 4 5 6 7 8 9\n");
        for (int i = 0; i < tamanio; i++) {
            sb.append(i).append(" ");
            for (int j = 0; j < tamanio; j++) {
                sb.append(celdas[i][j].getRepresentacion(ocultarBarcos)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}