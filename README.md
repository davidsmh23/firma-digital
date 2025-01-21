
# Proyecto de Firma Digital de Archivos ✍️🔐

Este proyecto proporciona una solución para firmar archivos digitalmente utilizando el algoritmo RSA y para verificar la validez de firmas digitales. Los usuarios pueden generar pares de claves públicas y privadas, firmar archivos con la clave privada y verificar la firma con claves públicas almacenadas en una biblioteca. **Solo funciona con extensión.txt**

## Funcionalidades 🎯

- **Firmar archivos**: Los usuarios pueden firmar archivos utilizando un par de claves generadas de manera local (RSA con SHA-256).
- **Verificar firmas**: Los usuarios pueden verificar la validez de una firma digital en un archivo utilizando una clave pública.
- **Gestión de claves**: El sistema permite generar, guardar y administrar claves públicas y privadas. Las claves públicas pueden ser almacenadas en una biblioteca para su posterior verificación.
- **Biblioteca de claves públicas**: Se puede almacenar una colección de claves públicas para su uso en la validación de firmas.

## Requisitos ⚙️

- **Java 11 o superior** ☕.
- **Bibliotecas estándar de Java**: El proyecto utiliza `java.security`, `java.io`, y `java.nio.file` entre otras bibliotecas estándar de Java.

## Compilar y Ejecutar el Proyecto 🛠️

### Requisitos

Para compilar y ejecutar este proyecto, necesitas tener **Java 11 o superior** instalado en tu máquina. Asegúrate de que el comando `javac` y `java` estén configurados correctamente en tu variable de entorno `PATH`.

### Compilar el Proyecto 📝
> 💻 El proyecto `firma_digital.jar` ya está compilado y listo para ejecutarlo.

1. Abre una terminal en el directorio raíz del proyecto.

2. Ejecuta el siguiente comando para compilar el proyecto:
`javac -d bin src/*.java src/util/*.java`

Este comando compilará todos los archivos `.java` en el directorio src y guardará los archivos `.class` compilados en el directorio bin.

### Crear el archivo JAR 📦
Una vez que el proyecto esté compilado, puedes empaquetarlo en un archivo JAR ejecutando el siguiente comando: `jar cfm firma_digital.jar MANIFEST.MF -C bin .`

- `firma_digital.jar` es el nombre del archivo JAR que se generará.
- `Main` es la clase principal que contiene el método `main` para ejecutar el programa.
- `-C bin .` indica que los archivos compilados `.class` en el directorio `bin` deben ser incluidos en el archivo JAR.

## Ejecutar el Proyecto 🚀

1. **Ejecutar el programa**: Para ejecutar el archivo JAR, usa el siguiente comando: `java -jar firma_digital.jar`
   
2. **Firmar un archivo**:
   - Introduzca la ruta completa del archivo que desea firmar.
   - El sistema generará un par de claves públicas y privadas y guardará las claves en el directorio `/claves`.
   - El archivo firmado se guardará con la firma digital al final del archivo original.
   
3. **Verificar una firma**:
   - Introduzca la ruta del archivo a verificar, el archivo con la firma y la biblioteca de claves públicas.
   - El sistema verificará la firma utilizando las claves públicas almacenadas en la biblioteca y le indicará si la firma es válida.

## Ejemplo de Ejecución 🎥

### Firmar un archivo

```
Introduce la ruta completa del archivo a firmar (!comprobar para verificar la clave // !fin para salir):
C:/documentos/archivo.txt
Archivo firmado correctamente. ✅
Archivo firmado guardado en: firmador/archivo_firmado.txt
Claves guardadas en: firmador/claves
```

### Verificar una firma

```
Introduce la ruta completa del archivo a comprobar:
archivo.txt
Introduce la ruta completa del archivo con la firma (introduce 1 si es firmador/archivo_firmado.txt):
firmador/archivo_firmado.txt
Introduce la ruta completa de la biblioteca de claves públicas (introduce 1 si es firmador/claves/biblioteca.txt):
firmador/claves/biblioteca.txt
La firma es válida con una de las claves públicas. ✅
```
## Dependencias del Proyecto 🌟

Este proyecto utiliza las siguientes clases y paquetes para su funcionamiento:

- **`java.io`** 📂: Manejo de archivos y directorios, lectura y escritura de datos.  
- **`java.nio.file`** 🗂️: Operaciones con rutas y manejo de archivos inexistentes.  
- **`java.security`** 🔐: Generación de claves RSA, firmas digitales y verificación de autenticidad.  
- **`java.util`** 🛠️: Estructuras dinámicas, codificación en Base64 y entrada de usuario.  

### Funcionalidad 💡
- **Gestión de Archivos** 📁: Leer, escribir y validar archivos.  
- **Criptografía** 🔑: Firmar y verificar archivos digitalmente.  
- **Interacción** 🤝: Pedir rutas y mostrar resultados al usuario.    

## Estructura del Proyecto 📂

El proyecto consta de las siguientes clases principales:

- **Main.java**: Es la clase principal donde se gestiona la firma de archivos, la verificación de firmas y la gestión de claves.
- **ArchivoUtil.java**: Contiene métodos para la lectura y escritura de archivos, así como para la gestión de claves y firmas.
- **FirmaUtil.java**: Contiene métodos para generar y verificar firmas digitales utilizando el algoritmo RSA con SHA-256.

### Archivos y directorios

- **`archivo_firmado.txt`**: Archivo firmado que contiene la firma digital al final del archivo original.
- **`/claves`**: Directorio donde se almacenan las claves públicas y privadas generadas.
- **`/biblioteca.txt`**: Archivo donde se almacenan las claves públicas en formato Base64.
- 
## Notas 📌

- El proyecto está diseñado para ser ejecutado en línea de comandos, por lo que se requiere la entrada de datos por parte del usuario.
- Los archivos de claves se almacenan de manera sencilla en formato binario, y las claves públicas se almacenan en formato Base64 dentro de un archivo de texto para facilitar su gestión.
- Se utiliza el algoritmo RSA con una longitud de clave de 2048 bits para garantizar una buena seguridad en la firma digital.