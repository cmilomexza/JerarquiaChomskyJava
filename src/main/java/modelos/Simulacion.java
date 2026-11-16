package modelos;

public interface Simulacion {
    void iniciar(String entrada);
    void avanzar();
    void ejecutar();
    boolean terminado();
    boolean aceptada();
    String estado();
    String representacion();
    String detalle();
}
