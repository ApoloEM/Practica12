package battleship;

public class ResultadoDisparo {
    private final TipoResultado tipo;
    private final String nombreBarcoHundido;
    private final Posicion posicion;

    public ResultadoDisparo(TipoResultado tipo, Posicion posicion) {
        this(tipo, posicion, null);
    }

    public ResultadoDisparo(TipoResultado tipo, Posicion posicion, String nombreBarcoHundido) {
        this.tipo = tipo;
        this.posicion = posicion;
        this.nombreBarcoHundido = nombreBarcoHundido;
    }

    public TipoResultado getTipo() { return tipo; }
    public String getNombreBarcoHundido() { return nombreBarcoHundido; }
    public Posicion getPosicion() { return posicion; }
}