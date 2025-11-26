package battleship;

import javax.swing.JOptionPane;

public class BattleshipP2P {
    private final IGestorRed red;
    private final IInterfazUsuario ui;
    private final JuegoBattleship juego;
    private boolean turnoMio;

    public BattleshipP2P(IGestorRed red, IInterfazUsuario ui) {
        this.red = red;
        this.ui = ui;
        this.juego = new JuegoBattleship();
    }

    public void iniciar() {
        try {
            ui.mostrarMensaje("=== BATTLESHIP P2P ===");
            
            String modo = ui.leerLinea("Elige modo de red (1=Servidor, 2=Cliente):");
            
            if (modo == null) {
                ui.mostrarMensaje("Configuración cancelada.");
                return;
            }

            if ("1".equals(modo)) {
                ui.mostrarMensaje("Iniciando servidor en puerto 12345...");
                red.conectarServidor(12345);
                turnoMio = true;
                ui.mostrarMensaje("¡Cliente conectado!");
            } else {
                String ip = ui.leerLinea("Ingresa IP del servidor (ej: localhost):");
                if (ip == null || ip.isEmpty()) ip = "localhost";
                
                ui.mostrarMensaje("Conectando a " + ip + ":12345...");
                red.conectarCliente(ip, 12345);
                turnoMio = false;
                ui.mostrarMensaje("¡Conectado al servidor!");
            }

            ui.mostrarMensaje("Colocando barcos automáticamente...");
            juego.colocarBarcosAutomaticamente();
            
            jugar();

        } catch (Exception e) {
            ui.mostrarError("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            red.cerrar();
        }
    }

    private void jugar() throws Exception {
        boolean jugando = true;
        
        red.enviarMensaje(ProtocoloBattleship.LISTO);
        String respuesta = red.recibirMensaje();
        
        if (respuesta == null) return;

        ui.mostrarMensaje("¡Ambos listos! " + (turnoMio ? "TÚ ATACAS PRIMERO." : "ESPERA EL ATAQUE ENEMIGO."));

        while (jugando) {
            ui.mostrarTableros(juego.getTableroPropio(), juego.getTableroEnemigo());
            
            if (turnoMio) {
                jugando = procesarTurnoLocal();
            } else {
                jugando = procesarTurnoRemoto();
            }
            
            if (juego.hePerdido()) {
                ui.mostrarMensaje("¡TODOS TUS BARCOS HAN SIDO HUNDIDOS! HAS PERDIDO.");
                jugando = false; 
            }
        }
    }

    private boolean procesarTurnoLocal() throws Exception {
        ui.mostrarMensaje(">>> TU TURNO: Selecciona una coordenada <<<");
        Posicion objetivo = ui.leerCoordenada();
        
        if (objetivo == null) return false;

        red.enviarMensaje(ProtocoloBattleship.crearMensajeDisparo(objetivo));
        String respuestaRaw = red.recibirMensaje();
        
        if (respuestaRaw == null) return false;

        ResultadoDisparo res = ProtocoloBattleship.parsearResultado(respuestaRaw);
        juego.registrarResultadoAjeno(res);
        
        ui.mostrarResultado(res, true);
        
        turnoMio = false;
        return true;
    }

    private boolean procesarTurnoRemoto() throws Exception {
        ui.mostrarMensaje("Esperando disparo del oponente...");
        String mensaje = red.recibirMensaje();
        
        if (mensaje == null) return false;
        
        if (ProtocoloBattleship.obtenerComando(mensaje).equals("DISPARAR")) {
            Posicion p = ProtocoloBattleship.parsearCoordenadas(mensaje);
            ResultadoDisparo resultado = juego.recibirDisparo(p);
            
            red.enviarMensaje(ProtocoloBattleship.crearMensajeResultado(resultado));
            ui.mostrarResultado(resultado, false);
            
            turnoMio = true;
        }
        return true;
    }

    public static void main(String[] args) {
        String modoSeleccionado = null;

        if (args.length > 0) {
            modoSeleccionado = args[0];
        } 
        else {
            Object[] options = {"Consola", "Gráfica (GUI)"};
            int n = JOptionPane.showOptionDialog(null,
                    "¿Cómo deseas jugar?",
                    "Battleship P2P - Selector",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            
            if (n == 1) modoSeleccionado = "gui";
            else modoSeleccionado = "consola";
        }

        IInterfazUsuario ui = FabricaUI.crearInterfaz(modoSeleccionado);

        IGestorRed red = new GestorRedSocket();
        BattleshipP2P programa = new BattleshipP2P(red, ui);

        new Thread(() -> {
            programa.iniciar();
        }).start();
    }
}