package battleship;

public class ProtocoloBattleship {
    public static final String LISTO = "LISTO";
    
    public static String crearMensajeDisparo(Posicion p) {
        return "DISPARAR|" + p.getFila() + "," + p.getColumna();
    }

    public static String crearMensajeResultado(ResultadoDisparo res) {
        String base = res.getTipo().toString() + "|" + res.getPosicion().toString();
        if (res.getTipo() == TipoResultado.HUNDIDO) {
            base += "|" + res.getNombreBarcoHundido();
        }
        return base;
    }

    public static Posicion parsearCoordenadas(String msg) {
        try {
            String[] partes = msg.split("\\|");
            String[] coords = partes[1].split(",");
            return new Posicion(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
        } catch (Exception e) {
            return null;
        }
    }

    public static ResultadoDisparo parsearResultado(String msg) {
        try {
            String[] partes = msg.split("\\|");
            TipoResultado tipo = TipoResultado.valueOf(partes[0]);
            String[] coords = partes[1].split(",");
            Posicion p = new Posicion(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            
            String barco = (partes.length > 2) ? partes[2] : null;
            return new ResultadoDisparo(tipo, p, barco);
        } catch (Exception e) {
            return new ResultadoDisparo(TipoResultado.INVALIDO, new Posicion(0,0));
        }
    }
    
    public static String obtenerComando(String msg) {
        if (msg == null) return "";
        return msg.split("\\|")[0];
    }
}