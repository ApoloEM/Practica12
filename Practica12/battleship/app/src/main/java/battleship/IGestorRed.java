package battleship;

public interface IGestorRed {
    void conectarServidor(int puerto) throws Exception;
    void conectarCliente(String ip, int puerto) throws Exception;
    void enviarMensaje(String mensaje);
    String recibirMensaje() throws Exception;
    void cerrar();
}