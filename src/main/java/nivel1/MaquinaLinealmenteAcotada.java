package nivel1;

import modelos.Simulacion;

/**
 * LBA para L = { a^n b^n c^n | n >= 1 }.
 * Marca cada grupo correspondiente como X, Y y Z.
 * La cinta queda delimitada por # y el cabezal nunca cruza esos límites.
 */
public class MaquinaLinealmenteAcotada implements Simulacion {
    private char[] cinta;
    private int cabeza;
    private String estado;
    private Fase fase;
    private boolean terminado;
    private boolean aceptada;
    private String detalle;

    private enum Fase { BUSCAR_A, BUSCAR_B, BUSCAR_C, REGRESAR, VERIFICAR }

    @Override
    public void iniciar(String entrada) {
        String cadena = entrada == null ? "" : entrada;
        cinta = ("#" + cadena + "#").toCharArray();
        cabeza = 1;
        fase = Fase.BUSCAR_A;
        estado = "qBuscarA";
        terminado = false;
        aceptada = false;
        detalle = "Cinta delimitada por #. Lenguaje: aⁿbⁿcⁿ, n ≥ 1.";
        if (!cadena.matches("a+b+c+")) {
            rechazar("Entrada inválida: se requiere a+, seguida de b+ y luego c+.");
        }
    }

    @Override
    public void avanzar() {
        if (terminado) return;
        if (cabeza < 1 || cabeza >= cinta.length) {
            rechazar("El cabezal intentó salir de la región delimitada.");
            return;
        }

        char c = cinta[cabeza];
        switch (fase) {
            case BUSCAR_A -> buscarA(c);
            case BUSCAR_B -> buscarB(c);
            case BUSCAR_C -> buscarC(c);
            case REGRESAR -> regresar();
            case VERIFICAR -> verificar(c);
        }
    }

    private void buscarA(char c) {
        if (c == 'X') {
            moverDerecha();
            detalle = "Avanza sobre una a que ya fue marcada como X.";
        } else if (c == 'a') {
            cinta[cabeza] = 'X';
            fase = Fase.BUSCAR_B;
            estado = "qBuscarB";
            moverDerecha();
            detalle = "Marca a como X y busca una b pendiente.";
        } else if (c == 'Y' || c == 'Z' || c == '#') {
            fase = Fase.VERIFICAR;
            estado = "qVerificar";
            detalle = "No quedan a pendientes. Verifica que no sobren b o c.";
        } else {
            rechazar("Se encontró un símbolo inesperado al buscar una a.");
        }
    }

    private void buscarB(char c) {
        if (c == 'a' || c == 'Y') {
            moverDerecha();
            detalle = "Avanza sobre a restantes o b ya marcadas.";
        } else if (c == 'b') {
            cinta[cabeza] = 'Y';
            fase = Fase.BUSCAR_C;
            estado = "qBuscarC";
            moverDerecha();
            detalle = "Marca b como Y y busca una c pendiente.";
        } else {
            rechazar("No existe una b sin marcar para emparejar.");
        }
    }

    private void buscarC(char c) {
        if (c == 'b' || c == 'Z') {
            moverDerecha();
            detalle = "Avanza sobre b restantes o c ya marcadas.";
        } else if (c == 'c') {
            cinta[cabeza] = 'Z';
            fase = Fase.REGRESAR;
            estado = "qRegresar";
            moverIzquierda();
            detalle = "Marca c como Z. Regresa al inicio para buscar otro grupo.";
        } else {
            rechazar("No existe una c sin marcar para emparejar.");
        }
    }

    private void regresar() {
        if (cabeza > 1) {
            moverIzquierda();
            detalle = "El cabezal regresa hacia el inicio de la entrada.";
        } else {
            fase = Fase.BUSCAR_A;
            estado = "qBuscarA";
            detalle = "El cabezal llegó al inicio. Comienza un nuevo recorrido.";
        }
    }

    private void verificar(char c) {
        if (c == 'X' || c == 'Y' || c == 'Z') {
            moverDerecha();
            detalle = "Símbolo emparejado correctamente; continúa la verificación.";
        } else if (c == '#') {
            terminado = true;
            aceptada = true;
            estado = "qAcepta";
            detalle = "Todos los símbolos quedaron emparejados. Cadena aceptada.";
        } else {
            rechazar("Quedaron símbolos sin marcar: las cantidades no son iguales.");
        }
    }

    private void moverDerecha() {
        if (cabeza < cinta.length - 1) cabeza++;
    }

    private void moverIzquierda() {
        if (cabeza > 1) cabeza--;
    }

    private void rechazar(String mensaje) {
        terminado = true;
        aceptada = false;
        estado = "qRechaza";
        detalle = mensaje;
    }

    @Override
    public void ejecutar() {
        int limite = Math.max(100, cinta.length * cinta.length * 20);
        while (!terminado && limite-- > 0) avanzar();
        if (!terminado) rechazar("Se alcanzó el límite de seguridad de pasos.");
    }

    @Override public boolean terminado() { return terminado; }
    @Override public boolean aceptada() { return terminado && aceptada; }
    @Override public String estado() { return estado + " | cabezal: " + cabeza; }

    @Override
    public String representacion() {
        if (cinta == null) return "(cinta no inicializada)";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < cinta.length; i++) {
            if (i == cabeza) b.append('[').append(cinta[i]).append(']');
            else b.append(' ').append(cinta[i]).append(' ');
            if (i < cinta.length - 1) b.append(' ');
        }
        return b.toString();
    }

    @Override public String detalle() { return detalle; }
}
