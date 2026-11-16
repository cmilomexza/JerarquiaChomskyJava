package pruebas;

import nivel0.MaquinaTuring;
import nivel1.MaquinaLinealmenteAcotada;
import nivel2.AutomataPila;
import nivel3.AutomataFinito;
import modelos.Simulacion;

/** Batería de pruebas ejecutable sin bibliotecas externas. */
public class PruebasModelos {
    private static int ejecutadas;
    private static int errores;

    public static void main(String[] args) {
        generar("", 7, "01", s -> probar(new AutomataFinito(), s, s.endsWith("01"), "AFD"));
        generar("", 7, "ab", s -> probar(new AutomataPila(), s,
                s.matches("a*b*") && contar(s, 'a') == contar(s, 'b'), "Pila"));
        generar("", 7, "abc", s -> probar(new MaquinaLinealmenteAcotada(), s,
                s.matches("a+b+c+") && contar(s, 'a') == contar(s, 'b')
                        && contar(s, 'b') == contar(s, 'c'), "LBA"));
        generar("", 7, "01", s -> probar(new MaquinaTuring(), s,
                s.matches("0+1+") && contar(s, '0') == contar(s, '1'), "Turing"));

        if (errores > 0) {
            throw new AssertionError("Fallaron " + errores + " de " + ejecutadas + " pruebas.");
        }
        System.out.println("Todas las pruebas finalizaron correctamente.");
        System.out.println("Pruebas ejecutadas: " + ejecutadas);
    }

    private static void probar(Simulacion modelo, String cadena, boolean esperado, String nombre) {
        modelo.iniciar(cadena);
        modelo.ejecutar();
        ejecutadas++;
        if (modelo.aceptada() != esperado) {
            errores++;
            System.err.println(nombre + " | entrada='" + cadena + "' | esperado="
                    + esperado + " | obtenido=" + modelo.aceptada()
                    + " | " + modelo.detalle());
        }
    }

    private interface CadenaCallback { void aceptar(String cadena); }

    private static void generar(String prefijo, int maximo, String alfabeto, CadenaCallback callback) {
        callback.aceptar(prefijo);
        if (prefijo.length() == maximo) return;
        for (int i = 0; i < alfabeto.length(); i++) {
            generar(prefijo + alfabeto.charAt(i), maximo, alfabeto, callback);
        }
    }

    private static int contar(String cadena, char simbolo) {
        int total = 0;
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == simbolo) total++;
        }
        return total;
    }
}
