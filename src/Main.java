import util.ArchivoUtil;
import util.FirmaUtil;

import java.io.File;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

/**
 * Clase principal que permite firmar archivos digitalmente y comprobar la validez de firmas digitales.
 * Utiliza claves RSA generadas en el momento o almacenadas previamente.
 * También soporta una biblioteca de claves públicas para verificar firmas.
 */
public class Main {

    /**
     * Método principal del programa.
     * Permite al usuario firmar archivos digitalmente, verificar firmas y gestionar claves públicas y privadas.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String rutaArchivoEntrada;
            File archivoEntrada;

            while (true) {
                System.out.println("Introduce la ruta completa del archivo a firmar (!comprobar para verificar la clave // !end para salir):");
                rutaArchivoEntrada = scanner.nextLine();

                if (rutaArchivoEntrada.equalsIgnoreCase("!end")) {
                    System.out.println("Programa terminado por el usuario.");
                    return;
                }

                if (rutaArchivoEntrada.equalsIgnoreCase("!comprobar")) {
                    System.out.println("Introduce la ruta completa del archivo a comprobar:");
                    String rutaArchivoComprobar = scanner.nextLine();
                    System.out.println("Introduce la ruta completa del archivo con la firma (introduce 1 si es firmador/archivo_firmado.txt):");
                    String rutaArchivoFirma = scanner.nextLine();
                    if (rutaArchivoFirma.equals("1")) {
                        rutaArchivoFirma = "firmador/archivo_firmado.txt";
                    }

                    System.out.println("Introduce la ruta completa de la biblioteca de claves públicas (introduce 1 si es firmador/claves/biblioteca.txt):");
                    String rutaBibliotecaClaves = scanner.nextLine();
                    if (rutaBibliotecaClaves.equals("1")) {
                        rutaBibliotecaClaves = "firmador/claves/biblioteca.txt";
                    }

                    // Leer los datos necesarios para la comprobación
                    byte[] contenidoArchivo = ArchivoUtil.readFile(rutaArchivoComprobar);
                    String firmaBase64 = ArchivoUtil.extraerFirmaDesdeArchivo(rutaArchivoFirma);
                    byte[] firmaDigital = Base64.getDecoder().decode(firmaBase64);

                    // Cargar todas las claves públicas desde la biblioteca
                    ArrayList<PublicKey> clavesPublicas = ArchivoUtil.cargarClavesPublicas(rutaBibliotecaClaves);

                    // Verificar la firma con todas las claves
                    boolean firmaValida = false;
                    for (PublicKey clavePublica : clavesPublicas) {
                        if (FirmaUtil.verificarFirma(contenidoArchivo, firmaDigital, clavePublica)) {
                            firmaValida = true;
                            break;
                        }
                    }

                    if (firmaValida) {
                        System.out.println("La firma es válida con una de las claves públicas.");
                    } else {
                        System.out.println("La firma no es válida con ninguna de las claves públicas.");
                    }
                    continue;
                }

                // Validar la existencia del archivo de entrada
                archivoEntrada = new File(rutaArchivoEntrada);
                if (archivoEntrada.exists()) {
                    break;
                } else {
                    System.err.println("El archivo especificado no existe. Por favor, inténtalo de nuevo.");
                }
            }

            // Configurar directorios y rutas para los archivos generados
            String rutaArchivoFirmado = "firmador/archivo_firmado.txt";
            String directorioClaves = "firmador/claves";

            // Crear el directorio para las claves si no existe
            File carpetaClaves = new File(directorioClaves);
            if (!carpetaClaves.exists() && !carpetaClaves.mkdirs()) {
                throw new RuntimeException("No se pudo crear el directorio para las claves: " + directorioClaves);
            }

            // Generar un nuevo par de claves (pública y privada)
            KeyPairGenerator generadorLlave = KeyPairGenerator.getInstance("RSA");
            generadorLlave.initialize(2048);
            KeyPair clavePar = generadorLlave.generateKeyPair();
            PrivateKey clavePrivada = clavePar.getPrivate();
            PublicKey clavePublica = clavePar.getPublic();

            // Guardar las claves generadas en archivos
            ArchivoUtil.guardarClave(directorioClaves + "/clavePrivada.txt", clavePrivada.getEncoded());
            ArchivoUtil.guardarClave(directorioClaves + "/clavePublica.txt", clavePublica.getEncoded());

            // Agregar la clave pública a la biblioteca de claves públicas
            ArchivoUtil.agregarClavePublicaBiblioteca(directorioClaves + "/biblioteca.txt", clavePublica);

            // Leer el contenido del archivo original y generar la firma digital
            byte[] contenidoOriginal = ArchivoUtil.readFile(rutaArchivoEntrada);
            byte[] firmaDigital = FirmaUtil.generarFirma(contenidoOriginal, clavePrivada);

            // Codificar la firma en Base64 e incrustarla en el archivo firmado
            String encodedFirma = Base64.getEncoder().encodeToString(firmaDigital);
            ArchivoUtil.embedSignature(rutaArchivoEntrada, rutaArchivoFirmado, encodedFirma);

            System.out.println("Archivo firmado correctamente.");
            System.out.println("Archivo firmado guardado en: " + rutaArchivoFirmado);
            System.out.println("Claves guardadas en: " + directorioClaves);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
