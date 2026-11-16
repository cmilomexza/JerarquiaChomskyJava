package interfaz;

import nivel3.AutomataFinito;
import nivel2.AutomataPila;
import nivel1.MaquinaLinealmenteAcotada;
import nivel0.MaquinaTuring;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    public VentanaPrincipal() {
        super("Simulador de la Jerarquía de Chomsky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 520);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Nivel 3 - AFD", new PanelSimulacion(
                AutomataFinito::new,
                new String[]{"01", "101", "1101"},
                new String[]{"", "0", "111", "10x"}));
        pestanas.addTab("Nivel 2 - Pila", new PanelSimulacion(
                AutomataPila::new,
                new String[]{"", "ab", "aabb", "aaabbb"},
                new String[]{"a", "abb", "aab", "ba"}));
        pestanas.addTab("Nivel 1 - LBA", new PanelSimulacion(
                MaquinaLinealmenteAcotada::new,
                new String[]{"abc", "aabbcc", "aaabbbccc"},
                new String[]{"abcc", "aabcc", "aabbc", "acb"}));
        pestanas.addTab("Nivel 0 - Turing", new PanelSimulacion(
                MaquinaTuring::new,
                new String[]{"01", "0011", "000111"},
                new String[]{"0", "011", "001", "0101"}));

        JLabel encabezado = new JLabel("Jerarquía de Chomsky - Simulador académico", SwingConstants.CENTER);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 17));
        add(encabezado, BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
        JLabel pie = new JLabel("Seleccione un nivel, escriba una cadena y ejecute la simulación.", SwingConstants.CENTER);
        pie.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        add(pie, BorderLayout.SOUTH);
    }
}
