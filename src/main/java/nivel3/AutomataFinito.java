package nivel3;

import modelos.Simulacion;

/**
 * AFD que reconoce cadenas binarias que terminan en 01.
 * Estados: q0 (inicial), q1, q2 (aceptación).
 */
public class AutomataFinito implements Simulacion {
    private String entrada = "";
    private int posicion;
    private String estado;
    private boolean terminado;
    private boolean aceptada;
    private String detalle;

    @Override
    public void iniciar(String entrada) {
        this.entrada = entrada == null ? "" : entrada;
        posicion = 0;
        estado = "q0";
        terminado = false;
        aceptada = false;
        detalle = "Estado inicial q0. Lenguaje: cadenas binarias que terminan en 01.";
        if (!this.entrada.matches("[01]*")) {
            terminado = true;
            detalle = "Entrada inválida: el alfabeto permitido es {0, 1}.";
        }
    }

    @Override
    public void avanzar() {
        if (terminado) return;
        if (posicion >= entrada.length()) {
            aceptada = estado.equals("q2");
            terminado = true;
            detalle = aceptada ? "Fin de entrada: q2 es estado de aceptación."
                               : "Fin de entrada: la cadena no termina en 01.";
            return;
        }
        char simbolo = entrada.charAt(posicion++);
        String anterior = estado;
        switch (estado) {
            case "q0" -> estado = simbolo == '0' ? "q1" : "q0";
            case "q1" -> estado = simbolo == '0' ? "q1" : "q2";
            case "q2" -> estado = simbolo == '0' ? "q1" : "q0";
            default -> throw new IllegalStateException("Estado desconocido");
        }
        detalle = "Lee '" + simbolo + "': " + anterior + " → " + estado
                + " (posición " + posicion + " de " + entrada.length() + ").";
        if (posicion == entrada.length()) {
            aceptada = estado.equals("q2");
            terminado = true;
            detalle += aceptada ? " Cadena aceptada." : " Cadena rechazada.";
        }
    }

    @Override public void ejecutar() { while (!terminado) avanzar(); }
    @Override public boolean terminado() { return terminado; }
    @Override public boolean aceptada() { return terminado && aceptada; }
    @Override public String estado() { return estado; }
    @Override public String representacion() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < entrada.length(); i++) {
            if (i == posicion - 1) b.append('[').append(entrada.charAt(i)).append(']');
            else b.append(entrada.charAt(i));
            b.append(' ');
        }
        return b.length() == 0 ? "(cadena vacía)" : b.toString();
    }
    @Override public String detalle() { return detalle; }
}
