package battleship;

import java.io.*;
import java.net.*;

public class GestorRedSocket implements IGestorRed {
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter salida;
    private BufferedReader entrada;

    @Override
    public void conectarServidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        socket = serverSocket.accept();
        configurarFlujos();
    }

    @Override
    public void conectarCliente(String ip, int puerto) throws IOException {
        socket = new Socket(ip, puerto);
        configurarFlujos();
    }

    private void configurarFlujos() throws IOException {
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void enviarMensaje(String mensaje) {
        if (salida != null) salida.println(mensaje);
    }

    @Override
    public String recibirMensaje() throws IOException {
        if (entrada != null) return entrada.readLine();
        return null;
    }

    @Override
    public void cerrar() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error cerrando recursos de red: " + e.getMessage());
        }
    }
}