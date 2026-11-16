package interfaz;

import modelos.Simulacion;
import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class PanelSimulacion extends JPanel {
    private final Supplier<Simulacion> fabrica;
    private final String[] ejemplosAceptados;
    private final String[] ejemplosRechazados;
    private Simulacion simulacion;

    private final JTextField entrada = new JTextField(22);
    private final JLabel estado = new JLabel("Estado: -");
    private final JLabel resultado = new JLabel("Resultado: pendiente");
    private final JTextArea representacion = new JTextArea(3, 48);
    private final JTextArea detalle = new JTextArea(5, 48);

    public PanelSimulacion(Supplier<Simulacion> fabrica, String[] aceptados, String[] rechazados) {
        this.fabrica = fabrica;
        this.ejemplosAceptados = aceptados;
        this.ejemplosRechazados = rechazados;
        this.simulacion = fabrica.get();
        construir();
        iniciar();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));
        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        superior.add(new JLabel("Cadena:"));
        superior.add(entrada);

        JButton iniciar = new JButton("Iniciar / reiniciar");
        JButton paso = new JButton("Avanzar paso");
        JButton ejecutar = new JButton("Ejecutar todo");
        superior.add(iniciar);
        superior.add(paso);
        superior.add(ejecutar);
        add(superior, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        JPanel ejemplos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> combo = new JComboBox<>(combinarEjemplos());
        ejemplos.add(new JLabel("Ejemplos:"));
        ejemplos.add(combo);
        JButton cargar = new JButton("Cargar ejemplo");
        ejemplos.add(cargar);
        centro.add(ejemplos);
        centro.add(estado);

        representacion.setEditable(false);
        representacion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        representacion.setLineWrap(true);
        representacion.setWrapStyleWord(true);
        centro.add(new JLabel("Representación:"));
        centro.add(new JScrollPane(representacion));

        detalle.setEditable(false);
        detalle.setLineWrap(true);
        detalle.setWrapStyleWord(true);
        centro.add(new JLabel("Detalle del último paso:"));
        centro.add(new JScrollPane(detalle));
        centro.add(resultado);
        add(centro, BorderLayout.CENTER);

        iniciar.addActionListener(e -> iniciar());

        // Una simulación terminada no se reinicia al pulsar Avanzar.
        paso.addActionListener(e -> {
            if (!simulacion.terminado()) {
                simulacion.avanzar();
                actualizar();
            }
        });

        // El reinicio queda bajo el control explícito del usuario.
        ejecutar.addActionListener(e -> {
            if (!simulacion.terminado()) {
                simulacion.ejecutar();
                actualizar();
            }
        });

        cargar.addActionListener(e -> {
            Object seleccionado = combo.getSelectedItem();
            if (seleccionado != null) {
                entrada.setText(seleccionado.toString());
                iniciar();
            }
        });
    }

    private String[] combinarEjemplos() {
        String[] todos = new String[ejemplosAceptados.length + ejemplosRechazados.length];
        System.arraycopy(ejemplosAceptados, 0, todos, 0, ejemplosAceptados.length);
        System.arraycopy(ejemplosRechazados, 0, todos, ejemplosAceptados.length, ejemplosRechazados.length);
        return todos;
    }

    private void iniciar() {
        simulacion = fabrica.get();
        simulacion.iniciar(entrada.getText());
        actualizar();
    }

    private void actualizar() {
        estado.setText("Estado: " + simulacion.estado());
        representacion.setText(simulacion.representacion());
        detalle.setText(simulacion.detalle());
        resultado.setText(simulacion.terminado()
                ? "Resultado: " + (simulacion.aceptada() ? "ACEPTADA" : "RECHAZADA")
                : "Resultado: simulación en curso");
    }
}
