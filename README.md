# Simulador de la Jerarquía de Chomsky


## Descripción
Aplicación académica desarrollada en Java 17 y Swing para explorar cuatro modelos computacionales asociados a la Jerarquía de Chomsky. Cada módulo permite escribir una cadena, cargar ejemplos, avanzar paso a paso o ejecutar la simulación completa.

## Tecnologías y requisitos
- Java JDK 17 o superior.
- Apache Maven 3.8 o superior.
- Eclipse o IntelliJ IDEA (opcional).
- No requiere base de datos ni dependencias externas.

## Modelos incluidos
1. **Nivel 3 - AFD:** reconoce cadenas binarias que terminan en `01`.
2. **Nivel 2 - Autómata de pila:** reconoce `L = { aⁿbⁿ | n ≥ 0 }`. Apila una marca por cada `a` y desapila una por cada `b`.
3. **Nivel 1 - LBA:** reconoce `L = { aⁿbⁿcⁿ | n ≥ 1 }`, usando `X`, `Y` y `Z` para marcar símbolos emparejados. La cinta está delimitada por `#`.
4. **Nivel 0 - Máquina de Turing:** reconoce `L = { 0ⁿ1ⁿ | n ≥ 1 }`, usando `X` y `Y` como marcas y blanco `□`. La cinta crece dinámicamente hacia la derecha.

## Estructura
```text
JerarquiaChomskyJava/
├── pom.xml
├── README.md
├── DOCUMENTO_ACADEMICO.md
└── src/main/java/
    ├── principal/Main.java
    ├── interfaz/
    │   ├── VentanaPrincipal.java
    │   └── PanelSimulacion.java
    ├── modelos/Simulacion.java
    ├── nivel3/AutomataFinito.java
    ├── nivel2/AutomataPila.java
    ├── nivel1/MaquinaLinealmenteAcotada.java
    └── nivel0/MaquinaTuring.java
```

## Ejecución desde Windows
1. Instale JDK 17 y Maven.
2. Abra CMD o PowerShell en la carpeta que contiene `pom.xml`.
3. Compruebe las instalaciones:
   ```bash
   java -version
   mvn -version
   ```
4. Compile:
   ```bash
   mvn clean compile
   ```
5. Ejecute la clase principal:
   ```bash
   java -cp target/classes principal.Main
   ```
   En Eclipse: **File > Import > Existing Maven Projects**, seleccione esta carpeta y luego ejecute `Main.java` como Java Application. En IntelliJ: abra la carpeta como proyecto Maven y ejecute `principal.Main`.

## Uso
- Seleccione una pestaña para entrar al nivel.
- Escriba una cadena o seleccione un ejemplo.
- Pulse **Iniciar / reiniciar**.
- Use **Avanzar paso** para observar una transición o **Ejecutar todo** para terminar la simulación.
- El estado, la representación y el resultado se actualizan en pantalla.

## Ejemplos
| Nivel | Aceptadas | Rechazadas |
|---|---|---|
| 3 | `01`, `101`, `1101` | `0`, `111`, `10x` |
| 2 | cadena vacía, `ab`, `aabb`, `aaabbb` | `a`, `abb`, `aab`, `ba` |
| 1 | `abc`, `aabbcc`, `aaabbbccc` | `abcc`, `aabcc`, `aabbc`, `acb` |
| 0 | `01`, `0011`, `000111` | `0`, `011`, `001`, `0101` |

## Alcance académico
Los lenguajes son ejemplos predeterminados para demostrar el funcionamiento de cada modelo. El AFD es determinista; el AP utiliza una pila LIFO; la LBA trabaja con una cinta acotada a la entrada; y la máquina de Turing modifica una cinta con símbolo blanco. La interfaz es deliberadamente sencilla para dar prioridad a la lógica de simulación.

## Pruebas automatizadas
Se incluye `src/test/java/pruebas/PruebasModelos.java`, una batería de 4.045 pruebas que genera cadenas de longitud de 0 a 7 y compara cada modelo con la definición matemática de su lenguaje. No utiliza bibliotecas externas.

En Windows, desde la carpeta que contiene `pom.xml`, ejecute:
```bash
mvn clean test-compile
java -cp "target/classes;target/test-classes" pruebas.PruebasModelos
```
El resultado esperado es `Todas las pruebas finalizaron correctamente.`

## Cambios técnicos de esta versión
- La LBA procesa los delimitadores como parte de su lógica y ya no rechaza las cadenas válidas al llegar al extremo derecho.
- La máquina de Turing utiliza una cinta dinámica que agrega blancos cuando el cabezal avanza hacia celdas nuevas.
- Los botones de avance y ejecución no reinician automáticamente una simulación terminada. Para comenzar otra, utilice explícitamente **Iniciar / reiniciar**.
