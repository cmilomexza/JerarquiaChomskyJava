# Implementación de modelos computacionales de la Jerarquía de Chomsky

**Estudiante:** [Nombre del estudiante]  
**Asignatura:** [Nombre de la asignatura]  
**Docente:** [Nombre del docente]  
**Institución:** [Nombre de la institución]

## 1. Introducción
La Jerarquía de Chomsky organiza distintas familias de lenguajes formales según la complejidad de las reglas necesarias para describirlos y de los modelos computacionales capaces de reconocerlos. Su estudio permite comprender cómo cambia la capacidad de procesamiento cuando se pasa de una memoria finita a estructuras de memoria más amplias.

Este proyecto desarrolla un simulador académico en Java que presenta cuatro modelos: autómata finito, autómata de pila, máquina linealmente acotada y máquina de Turing. La aplicación permite introducir cadenas, observar el estado de cada modelo y conocer si la entrada pertenece al lenguaje definido.

## 2. Objetivo general
Diseñar e implementar una aplicación con interfaz gráfica que permita simular y validar el comportamiento de modelos computacionales representativos de los cuatro niveles de la Jerarquía de Chomsky.

## 3. Objetivos específicos
- Implementar un AFD para reconocer un lenguaje regular.
- Construir un autómata de pila que utilice operaciones de apilamiento y desapilamiento.
- Simular una máquina linealmente acotada con delimitadores y marcas sobre la cinta.
- Implementar una máquina de Turing que lea y escriba símbolos y mueva su cabezal.
- Separar la lógica de los modelos y la interfaz mediante clases y paquetes.
- Proporcionar ejemplos de prueba y documentación de ejecución.

## 4. Fundamentos teóricos
La Jerarquía de Chomsky clasifica los lenguajes formales en cuatro niveles. En términos generales, el nivel 3 corresponde a lenguajes regulares; el nivel 2, a lenguajes libres de contexto; el nivel 1, a lenguajes sensibles al contexto; y el nivel 0, a lenguajes reconocibles por máquinas de Turing.

Cada nivel se asocia con un modelo computacional que dispone de mecanismos de memoria diferentes. Un autómata finito utiliza un conjunto limitado de estados. Un autómata de pila incorpora una memoria LIFO. Una máquina linealmente acotada dispone de una cinta cuya región utilizable está limitada por la longitud de la entrada. Una máquina de Turing utiliza una cinta que puede extenderse y permite leer, escribir y desplazar el cabezal.

## 5. Funcionamiento de los modelos implementados

### 5.1 Nivel 3: autómata finito determinista
El AFD implementado reconoce cadenas binarias que terminan en `01`. Tiene tres estados: `q0` como inicial, `q1` como estado que representa que el último símbolo leído fue `0`, y `q2` como estado de aceptación cuando el sufijo leído es `01`. Cada símbolo provoca una transición determinada. La cadena se acepta si al terminar el procesamiento el autómata se encuentra en `q2`.

### 5.2 Nivel 2: autómata de pila
El lenguaje reconocido es `L = { aⁿbⁿ | n ≥ 0 }`. Por cada `a` se introduce una marca `A` en la pila mediante PUSH. Cuando aparecen las `b`, se elimina una marca por símbolo mediante POP. El orden de la entrada debe ser primero las `a` y después las `b`. Se acepta cuando termina la entrada y únicamente queda el marcador de fondo.

### 5.3 Nivel 1: máquina linealmente acotada
El lenguaje reconocido es `L = { aⁿbⁿcⁿ | n ≥ 1 }`. La cinta tiene un delimitador `#` a cada lado y el cabezal nunca puede atravesarlos. En cada recorrido se marca una `a` como `X`, una `b` como `Y` y una `c` como `Z`. Después, el cabezal regresa al comienzo para buscar el siguiente grupo. La máquina acepta cuando todos los símbolos han sido emparejados y rechaza si falta alguno o no se respeta el orden.

### 5.4 Nivel 0: máquina de Turing
La máquina reconoce `L = { 0ⁿ1ⁿ | n ≥ 1 }`. La cinta se administra dinámicamente y agrega celdas en blanco cuando el cabezal avanza más allá del extremo derecho disponible. Busca un `0` sin marcar y lo sustituye por `X`; después busca un `1` sin marcar y lo sustituye por `Y`. Regresa al extremo izquierdo y repite el procedimiento. Cuando ya no quedan ceros pendientes, verifica que el resto sean únicamente marcas `Y` y el símbolo blanco. La máquina acepta si todos los símbolos quedaron emparejados.

## 6. Descripción de la implementación en Java
Se utilizó Java 17 y Swing para construir una interfaz de escritorio sin dependencias externas. El proyecto se organiza por paquetes para mantener separadas la ventana, los componentes visuales y los modelos de simulación.

La interfaz utiliza pestañas para acceder a los cuatro niveles. Cada módulo ofrece un campo de entrada, ejemplos precargados y controles para iniciar, avanzar paso a paso o ejecutar toda la simulación. La información del estado, la representación de memoria y el detalle de la última operación se muestran en pantalla.

## 7. Clases principales
- `Main`: inicia la aplicación y crea la ventana en el hilo de eventos de Swing.
- `VentanaPrincipal`: construye la ventana y las cuatro pestañas.
- `PanelSimulacion`: reutiliza los controles visuales para cada modelo.
- `Simulacion`: define las operaciones comunes que debe ofrecer un simulador.
- `AutomataFinito`: implementa las transiciones del AFD.
- `AutomataPila`: administra la pila y sus operaciones PUSH/POP.
- `MaquinaLinealmenteAcotada`: administra la cinta delimitada, el cabezal y los marcadores.
- `MaquinaTuring`: implementa lectura, escritura, desplazamiento y aceptación/rechazo.

## 8. Conclusiones
El desarrollo permite observar que los modelos computacionales se diferencian por la memoria y las operaciones que tienen disponibles. El AFD resuelve un reconocimiento basado únicamente en estados; el autómata de pila puede comparar cantidades mediante una estructura LIFO; la máquina linealmente acotada utiliza una cinta limitada para marcar y comparar símbolos; y la máquina de Turing amplía estas operaciones mediante una cinta de trabajo con lectura y escritura.

La implementación también muestra la importancia de separar la lógica de simulación de la interfaz gráfica. Esta organización facilita probar cada modelo y mantener el programa. Los lenguajes seleccionados son ejemplos concretos que permiten comprobar el comportamiento de cada nivel de forma sencilla.

## 9. Verificación y pruebas
Se compiló el código fuente con compatibilidad de Java 17. También se incluye una batería de 4.045 pruebas automáticas, sin bibliotecas externas, que genera cadenas de longitud de cero a siete símbolos y compara las respuestas de los cuatro modelos con la definición matemática de cada lenguaje.
