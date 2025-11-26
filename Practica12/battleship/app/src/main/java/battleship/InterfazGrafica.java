package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class InterfazGrafica implements IInterfazUsuario {
    private JFrame frame;
    private JTextArea areaMensajes;
    private JPanel panelPropio;
    private JPanel panelEnemigo;
    private JButton[][] botonesEnemigos;
    private JLabel[][] etiquetasPropias;
    
    private CountDownLatch latch;
    private Posicion ultimaPosicionSeleccionada;

    public InterfazGrafica() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        frame = new JFrame("Battleship P2P - Modo Gráfico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        areaMensajes = new JTextArea(3, 50);
        areaMensajes.setEditable(false);
        frame.add(new JScrollPane(areaMensajes), BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        
        panelPropio = new JPanel(new GridLayout(10, 10));
        panelPropio.setBorder(BorderFactory.createTitledBorder("TU FLOTA"));
        etiquetasPropias = new JLabel[10][10];

        panelEnemigo = new JPanel(new GridLayout(10, 10));
        panelEnemigo.setBorder(BorderFactory.createTitledBorder("RADAR ENEMIGO (DISPARA AQUÍ)"));
        botonesEnemigos = new JButton[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JLabel lbl = new JLabel("", SwingConstants.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                etiquetasPropias[i][j] = lbl;
                panelPropio.add(lbl);

                JButton btn = new JButton();
                int f = i; int c = j;
                btn.addActionListener(e -> procesarClic(f, c));
                btn.setEnabled(false);
                botonesEnemigos[i][j] = btn;
                panelEnemigo.add(btn);
            }
        }

        panelCentral.add(panelPropio);
        panelCentral.add(panelEnemigo);
        frame.add(panelCentral, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void procesarClic(int f, int c) {
        if (latch != null) {
            ultimaPosicionSeleccionada = new Posicion(f, c);
            latch.countDown();
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaMensajes.append(mensaje + "\n");
            areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
        });
    }

    @Override
    public void mostrarError(String error) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    @Override
    public String leerLinea(String prompt) {
        // Usamos un diálogo modal para pedir texto (IP, Modo, etc)
        return JOptionPane.showInputDialog(frame, prompt);
    }

    @Override
    public Posicion leerCoordenada() {
        // Habilitar botones y esperar clic
        SwingUtilities.invokeLater(() -> setBotonesEnemigosHabilitados(true));
        
        latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            return null;
        }
        
        SwingUtilities.invokeLater(() -> setBotonesEnemigosHabilitados(false));
        return ultimaPosicionSeleccionada;
    }

    private void setBotonesEnemigosHabilitados(boolean habilitar) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (botonesEnemigos[i][j].getText().isEmpty()) {
                    botonesEnemigos[i][j].setEnabled(habilitar);
                }
            }
        }
    }

    @Override
    public void mostrarTableros(Tablero propio, Tablero enemigo) {
        SwingUtilities.invokeLater(() -> {
            actualizarGridPropio(propio);
            actualizarGridEnemigo(enemigo);
        });
    }

    private void actualizarGridPropio(Tablero t) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                char c = t.getCelda(new Posicion(i, j)).getRepresentacion(false);
                JLabel lbl = etiquetasPropias[i][j];
                colorearComponente(lbl, c, true);
            }
        }
    }

    private void actualizarGridEnemigo(Tablero t) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                char c = t.getCelda(new Posicion(i, j)).getRepresentacion(true);
                JButton btn = botonesEnemigos[i][j];
                
                if (c != '?' && c != '~') {
                    btn.setText(String.valueOf(c));
                    colorearComponente(btn, c, false);
                }
            }
        }
    }

    private void colorearComponente(JComponent comp, char c, boolean esPropio) {
        switch (c) {
            case '~': comp.setBackground(new Color(173, 216, 230)); break;
            case 'X': comp.setBackground(Color.RED); break;
            case 'O': comp.setBackground(Color.WHITE); break;
            default:
                if (esPropio) comp.setBackground(Color.GRAY); 
                else comp.setBackground(new Color(173, 216, 230));
        }
    }

    @Override
    public void mostrarResultado(ResultadoDisparo res, boolean esMio) {
        String texto = esMio ? "Tú disparaste: " : "Enemigo disparó: ";
        mostrarMensaje(texto + res.getTipo());
    }
}