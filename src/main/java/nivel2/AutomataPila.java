package nivel2;

import modelos.Simulacion;
import java.util.ArrayDeque;
import java.util.Deque;

/** Autómata de pila para L = { a^n b^n | n >= 0 }. */
public class AutomataPila implements Simulacion {
    private String entrada = "";
    private int posicion;
    private final Deque<Character> pila = new ArrayDeque<>();
    private String estado;
    private boolean terminado;
    private boolean aceptada;
    private boolean faseB;
    private String detalle;

    @Override
    public void iniciar(String entrada) {
        this.entrada = entrada == null ? "" : entrada;
        posicion = 0;
        pila.clear();
        pila.push('$');
        estado = "qA";
        faseB = false;
        terminado = false;
        aceptada = false;
        detalle = "Pila inicial: $. Lenguaje: a^n b^n, n ≥ 0.";
        if (!this.entrada.matches("a*b*")) rechazar("La cadena debe contener primero a y después b.");
        else if (this.entrada.isEmpty()) {
            terminado = true; aceptada = true; estado = "qAcepta";
            detalle = "La cadena vacía pertenece al lenguaje (n = 0).";
        }
    }

    @Override
    public void avanzar() {
        if (terminado) return;
        if (posicion >= entrada.length()) {
            aceptada = pila.size() == 1 && pila.peek() == '$';
            terminado = true;
            estado = aceptada ? "qAcepta" : "qRechaza";
            detalle = aceptada ? "Entrada consumida y solo queda el marcador $. Cadena aceptada."
                               : "La pila no quedó en su configuración inicial. Cadena rechazada.";
            return;
        }
        char c = entrada.charAt(posicion++);
        if (c == 'a') {
            if (faseB) { rechazar("Se encontró una 'a' después de comenzar a desapilar."); return; }
            pila.push('A');
            estado = "qA";
            detalle = "Lee 'a': PUSH(A).";
        } else if (c == 'b') {
            faseB = true;
            if (pila.size() <= 1) { rechazar("Hay más b que a; POP no es posible."); return; }
            pila.pop();
            estado = "qB";
            detalle = "Lee 'b': POP(A).";
        }
        if (posicion == entrada.length()) {
            aceptada = pila.size() == 1 && pila.peek() == '$';
            terminado = true;
            estado = aceptada ? "qAcepta" : "qRechaza";
            detalle += aceptada ? " Pila equilibrada. Cadena aceptada."
                                : " Quedan símbolos en la pila. Cadena rechazada.";
        }
    }

    private void rechazar(String mensaje) {
        terminado = true; aceptada = false; estado = "qRechaza"; detalle = mensaje;
    }
    @Override public void ejecutar() { while (!terminado) avanzar(); }
    @Override public boolean terminado() { return terminado; }
    @Override public boolean aceptada() { return terminado && aceptada; }
    @Override public String estado() { return estado; }
    @Override public String representacion() {
        return "Base → cima: " + String.join(" ", pila.stream().map(String::valueOf).toList());
    }
    @Override public String detalle() { return detalle; }
}
