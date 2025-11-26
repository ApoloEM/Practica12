package battleship;

public interface IInterfazUsuario {
    void mostrarMensaje(String mensaje);
    void mostrarError(String error);
    String leerLinea(String prompt);
    Posicion leerCoordenada();
    void mostrarTableros(Tablero propio, Tablero enemigo);
    void mostrarResultado(ResultadoDisparo resultado, boolean esMio);
}