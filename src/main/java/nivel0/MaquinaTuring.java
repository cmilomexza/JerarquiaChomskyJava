package nivel0;

import modelos.Simulacion;
import java.util.ArrayList;
import java.util.List;

/**
 * Máquina de Turing para L = { 0^n 1^n | n >= 1 }.
 * X marca ceros emparejados y Y marca unos emparejados.
 * La cinta crece dinámicamente hacia la derecha agregando blancos.
 */
public class MaquinaTuring implements Simulacion {
    private static final char BLANCO = '_';
    private final List<Character> cinta = new ArrayList<>();
    private int cabeza;
    private String estado;
    private boolean terminado;
    private boolean aceptada;
    private String detalle;

    @Override
    public void iniciar(String entrada) {
        String cadena = entrada == null ? "" : entrada;
        cinta.clear();
        for (char c : cadena.toCharArray()) cinta.add(c);
        cinta.add(BLANCO);
        cabeza = 0;
        estado = "qBuscar0";
        terminado = false;
        aceptada = false;
        detalle = "Máquina iniciada. Alfabeto: {0, 1, X, Y, _}. Cinta extensible hacia la derecha.";
        if (!cadena.matches("0+1+")) {
            rechazar("Entrada inválida: se requiere uno o más 0 seguidos de uno o más 1.");
        }
    }

    @Override
    public void avanzar() {
        if (terminado) return;
        asegurarCelda(cabeza);
        char c = leer();
        switch (estado) {
            case "qBuscar0" -> buscarCero(c);
            case "qBuscar1" -> buscarUno(c);
            case "qVolver" -> volver(c);
            case "qVerificar" -> verificar(c);
            default -> rechazar("Estado desconocido.");
        }
    }

    private void buscarCero(char c) {
        if (c == 'X') {
            moverDerecha();
            detalle = "Avanza sobre un cero ya marcado como X.";
        } else if (c == '0') {
            escribir('X');
            estado = "qBuscar1";
            moverDerecha();
            detalle = "Marca un 0 como X y busca un 1 pendiente.";
        } else if (c == 'Y' || c == BLANCO) {
            estado = "qVerificar";
            detalle = "No quedan ceros pendientes. Verifica los unos restantes.";
        } else {
            rechazar("Símbolo inesperado mientras se buscaba un cero.");
        }
    }

    private void buscarUno(char c) {
        if (c == '0' || c == 'Y') {
            moverDerecha();
            detalle = "Avanza buscando un 1 sin marcar.";
        } else if (c == '1') {
            escribir('Y');
            estado = "qVolver";
            moverIzquierda();
            detalle = "Marca un 1 como Y y regresa al extremo izquierdo.";
        } else {
            rechazar("No existe un 1 disponible para emparejar con el cero seleccionado.");
        }
    }

    private void volver(char c) {
        if (cabeza > 0) moverIzquierda();
        if (cabeza == 0) {
            estado = "qBuscar0";
            detalle = "Regresó al inicio. Busca el siguiente cero sin marcar.";
        } else {
            detalle = "El cabezal continúa regresando al extremo izquierdo.";
        }
    }

    private void verificar(char c) {
        if (c == 'Y') {
            moverDerecha();
            detalle = "Verifica un 1 emparejado (Y) y continúa.";
        } else if (c == BLANCO) {
            terminado = true;
            aceptada = true;
            estado = "qAcepta";
            detalle = "Todos los ceros y unos quedaron emparejados. Cadena aceptada.";
        } else {
            rechazar("Quedó un símbolo sin emparejar.");
        }
    }

    private char leer() {
        asegurarCelda(cabeza);
        return cinta.get(cabeza);
    }

    private void escribir(char c) {
        asegurarCelda(cabeza);
        cinta.set(cabeza, c);
    }

    private void moverDerecha() {
        cabeza++;
        asegurarCelda(cabeza);
    }

    private void moverIzquierda() {
        if (cabeza > 0) cabeza--;
    }

    private void asegurarCelda(int indice) {
        while (cinta.size() <= indice) cinta.add(BLANCO);
    }

    private void rechazar(String mensaje) {
        terminado = true;
        aceptada = false;
        estado = "qRechaza";
        detalle = mensaje;
    }

    @Override
    public void ejecutar() {
        int limite = Math.max(100, (cinta.size() + 1) * (cinta.size() + 1) * 20);
        while (!terminado && limite-- > 0) avanzar();
        if (!terminado) rechazar("Se alcanzó el límite de seguridad de pasos.");
    }

    @Override public boolean terminado() { return terminado; }
    @Override public boolean aceptada() { return terminado && aceptada; }
    @Override public String estado() { return estado + " | cabezal: " + cabeza; }

    @Override
    public String representacion() {
        if (cinta.isEmpty()) return "(cinta vacía)";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < cinta.size(); i++) {
            char c = cinta.get(i);
            String visible = c == BLANCO ? "□" : String.valueOf(c);
            if (i == cabeza) b.append('[').append(visible).append(']');
            else b.append(' ').append(visible).append(' ');
            if (i < cinta.size() - 1) b.append(' ');
        }
        return b.toString();
    }

    @Override public String detalle() { return detalle; }
}
