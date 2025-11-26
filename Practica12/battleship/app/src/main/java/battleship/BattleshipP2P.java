package battleship;

public class BattleshipP2P {
    private final IGestorRed red;
    private final IInterfazUsuario ui;
    private final JuegoBattleship juego;
    private boolean turnoMio;

    // Inyección de dependencias
    public BattleshipP2P(IGestorRed red, IInterfazUsuario ui) {
        this.red = red;
        this.ui = ui;
        this.juego = new JuegoBattleship();
    }

    public void iniciar() {
        try {
            ui.mostrarMensaje("=== BATTLESHIP P2P (REFACTORIZADO) ===");
            String modo = ui.leerLinea("Elige modo (1=Servidor, 2=Cliente):");

            if ("1".equals(modo)) {
                ui.mostrarMensaje("Esperando conexion en puerto 12345...");
                red.conectarServidor(12345);
                turnoMio = true; // Servidor empieza
            } else {
                String ip = ui.leerLinea("IP del servidor:");
                red.conectarCliente(ip, 12345);
                turnoMio = false;
            }

            ui.mostrarMensaje("¡Conectado! Iniciando juego...");
            juego.colocarBarcosAutomaticamente();
            jugar();

        } catch (Exception e) {
            ui.mostrarError("Error fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            red.cerrar();
        }
    }

    private void jugar() throws Exception {
        boolean jugando = true;
        
        red.enviarMensaje(ProtocoloBattleship.LISTO);
        String respuesta = red.recibirMensaje();
        
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
        ui.mostrarMensaje(">>> TU TURNO <<<");
        Posicion objetivo = ui.leerCoordenada();
        
        red.enviarMensaje(ProtocoloBattleship.crearMensajeDisparo(objetivo));
        String respuestaRaw = red.recibirMensaje();
        
        ResultadoDisparo res = ProtocoloBattleship.parsearResultado(respuestaRaw);
        juego.registrarResultadoAjeno(res);
        
        ui.mostrarResultado(res, true);
        
        if (res.getTipo() == TipoResultado.YA_DISPARADO) {
            ui.mostrarMensaje("Ya habías disparado ahí. Pierdes turno (regla simple).");
        }
        
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
        IGestorRed red = new GestorRedSocket();
        IInterfazUsuario ui = new InterfazConsola();
        
        BattleshipP2P programa = new BattleshipP2P(red, ui);
        programa.iniciar();
    }
}