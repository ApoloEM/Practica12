package battleship;

import java.util.*;

public class JuegoBattleship {
    private final Tablero tableroPropio;
    private final Tablero tableroEnemigo;
    private final Map<String, Integer> barcosSalud;

    public JuegoBattleship() {
        tableroPropio = new Tablero();
        tableroEnemigo = new Tablero();
        barcosSalud = new HashMap<>();
        inicializarSaludBarcos();
    }

    private void inicializarSaludBarcos() {
        barcosSalud.put("PORTAAVIONES", 5);
        barcosSalud.put("ACORAZADO", 4);
        barcosSalud.put("CRUCERO", 3);
        barcosSalud.put("SUBMARINO", 3);
        barcosSalud.put("DESTRUCTOR", 2);
    }

    public void colocarBarcosAutomaticamente() {
        colocarBarco(5, 'P');
        colocarBarco(4, 'A');
        colocarBarco(3, 'C');
        colocarBarco(3, 'S');
        colocarBarco(2, 'D');
    }

    private void colocarBarco(int longitud, char simbolo) {
        Random rnd = new Random();
        boolean colocado = false;
        while (!colocado) {
            int f = rnd.nextInt(10);
            int c = rnd.nextInt(10);
            boolean horizontal = rnd.nextBoolean();
            
            if (puedeColocar(f, c, longitud, horizontal)) {
                for (int i = 0; i < longitud; i++) {
                    int ff = horizontal ? f : f + i;
                    int cc = horizontal ? c + i : c;
                    tableroPropio.getCelda(new Posicion(ff, cc)).colocarBarco(simbolo);
                }
                colocado = true;
            }
        }
    }

    private boolean puedeColocar(int f, int c, int len, boolean horiz) {
        if (horiz && c + len > 10) return false;
        if (!horiz && f + len > 10) return false;
        for (int i = 0; i < len; i++) {
            int ff = horiz ? f : f + i;
            int cc = horiz ? c + i : c;
            if (tableroPropio.getCelda(new Posicion(ff, cc)).tieneBarco()) return false;
        }
        return true;
    }

    public ResultadoDisparo recibirDisparo(Posicion p) {
        Celda celda = tableroPropio.getCelda(p);
        
        if (celda.estaImpactada()) {
            return new ResultadoDisparo(TipoResultado.YA_DISPARADO, p);
        }

        celda.recibirImpacto();

        if (celda.tieneBarco()) {
            String nombreBarco = obtenerNombreBarco(celda.getSimboloBarco());
            disminuirSaludBarco(nombreBarco);
            
            if (estaHundido(nombreBarco)) {
                return new ResultadoDisparo(TipoResultado.HUNDIDO, p, nombreBarco);
            }
            return new ResultadoDisparo(TipoResultado.IMPACTO, p);
        }
        
        return new ResultadoDisparo(TipoResultado.AGUA, p);
    }

    public void registrarResultadoAjeno(ResultadoDisparo res) {
        if (res.getTipo() == TipoResultado.YA_DISPARADO) return;

        Celda celda = tableroEnemigo.getCelda(res.getPosicion());
        celda.recibirImpacto();
        if (res.getTipo() == TipoResultado.IMPACTO || res.getTipo() == TipoResultado.HUNDIDO) {
            celda.colocarBarco('X');
        }
    }

    private void disminuirSaludBarco(String nombre) {
        barcosSalud.put(nombre, barcosSalud.get(nombre) - 1);
    }

    private boolean estaHundido(String nombre) {
        return barcosSalud.get(nombre) <= 0;
    }
    
    public boolean hePerdido() {
        for (int salud : barcosSalud.values()) {
            if (salud > 0) return false;
        }
        return true;
    }

    private String obtenerNombreBarco(char c) {
        switch(c) {
            case 'P': return "PORTAAVIONES";
            case 'A': return "ACORAZADO";
            case 'C': return "CRUCERO";
            case 'S': return "SUBMARINO";
            case 'D': return "DESTRUCTOR";
            default: return "DESCONOCIDO";
        }
    }

    public Tablero getTableroPropio() { return tableroPropio; }
    public Tablero getTableroEnemigo() { return tableroEnemigo; }
}