Ejercicio 6 - Polimorfismo via Interfaces 

Manuel Rio - 16639

Sistema central que maneja las integraciones de un e-commerce con APIs externas
pasarelas de pago, logistica y certificadores FEL usando interfaces.

Estructura

 src - codigo fuente (.java)
 bin - archivos compilados (.class)
 src/modelo - integraciones, interfaces y el registro (unica lista polimorfica)
 src/controlador - logica del sistema (checkout, reintegros, documentos)
 src/vista - vista de consola y vista grafica
 src/Main.java - driver program
