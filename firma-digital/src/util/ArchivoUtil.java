package util;

import java.io.*;
import java.nio.file.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Clase utilitaria para operaciones relacionadas con archivos y claves.
 * Proporciona métodos para leer, guardar y gestionar claves y firmas digitales.
 */
public class ArchivoUtil {

    /**
     * Lee el contenido de un archivo y lo devuelve como un arreglo de bytes.
     *
     * @param rutaArchivo Ruta del archivo a leer.
     * @return Contenido del archivo en forma de arreglo de bytes.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static byte[] readFile(String rutaArchivo) throws IOException {
        return Files.readAllBytes(Paths.get(rutaArchivo));
    }

    /**
     * Guarda una clave en un archivo en formato binario.
     *
     * @param ruta Ruta donde se guardará la clave.
     * @param clave Arreglo de bytes que representa la clave.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void guardarClave(String ruta, byte[] clave) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            fos.write(clave);
        }
    }

    /**
     * Agrega una clave pública a una biblioteca de claves públicas, separándolas con "---".
     *
     * @param ruta Ruta del archivo de la biblioteca de claves públicas.
     * @param clavePublica Clave pública a agregar.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public static void agregarClavePublicaBiblioteca(String ruta, PublicKey clavePublica) throws IOException {
        String claveBase64 = Base64.getEncoder().encodeToString(clavePublica.getEncoded());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta, true))) {
            writer.write("---");
            writer.newLine();
            writer.write(claveBase64);
            writer.newLine();
        }
    }

    /**
     * Extrae la firma digital de un archivo firmado.
     * Busca una sección delimitada por "-----INICIO FIRMA DIGITAL-----" y "-----FIN FIRMA DIGITAL-----".
     *
     * @param rutaArchivoFirmado Ruta del archivo firmado.
     * @return Firma digital en formato Base64.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static String extraerFirmaDesdeArchivo(String rutaArchivoFirmado) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoFirmado))) {
            String linea;
            StringBuilder firma = new StringBuilder();
            boolean inicioFirma = false;

            while ((linea = reader.readLine()) != null) {
                if (linea.equals("-----INICIO FIRMA DIGITAL-----")) {
                    inicioFirma = true;
                    continue;
                }
                if (linea.equals("-----FIN FIRMA DIGITAL-----")) {
                    break;
                }
                if (inicioFirma) {
                    firma.append(linea);
                }
            }
            return firma.toString();
        }
    }

    /**
     * Incrusta una firma digital en un archivo, generando un nuevo archivo de salida.
     * La firma se agrega al final del contenido del archivo de entrada.
     *
     * @param rutaEntrada Ruta del archivo original.
     * @param rutaSalida Ruta del archivo firmado de salida.
     * @param firma Firma digital en formato Base64.
     * @throws IOException Si ocurre un error al leer o escribir archivos.
     */
    public static void embedSignature(String rutaEntrada, String rutaSalida, String firma) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaEntrada));
             BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                writer.write(linea);
                writer.newLine();
            }

            writer.write("-----INICIO FIRMA DIGITAL-----");
            writer.newLine();
            writer.write(firma);
            writer.newLine();
            writer.write("-----FIN FIRMA DIGITAL-----");
        }
    }

    /**
     * Carga todas las claves públicas de una biblioteca de claves.
     * Las claves están separadas por "---" en el archivo.
     *
     * @param rutaBiblioteca Ruta del archivo de la biblioteca de claves públicas.
     * @return Lista de claves públicas extraídas del archivo.
     * @throws Exception Si ocurre un error al leer el archivo o al procesar las claves.
     */
    public static ArrayList<PublicKey> cargarClavesPublicas(String rutaBiblioteca) throws Exception {
        ArrayList<PublicKey> clavesPublicas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaBiblioteca))) {
            String linea;
            StringBuilder claveActual = new StringBuilder();

            while ((linea = reader.readLine()) != null) {
                if (linea.equals("---")) {
                    if (claveActual.length() > 0) {
                        // Convertir la clave Base64 a PublicKey
                        byte[] claveBytes = Base64.getDecoder().decode(claveActual.toString());
                        PublicKey clavePublica = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(claveBytes));
                        clavesPublicas.add(clavePublica);
                        claveActual.setLength(0); // Limpiar el StringBuilder para la siguiente clave
                    }
                } else {
                    claveActual.append(linea);
                }
            }

            // Agregar la última clave si no está vacía
            if (claveActual.length() > 0) {
                byte[] claveBytes = Base64.getDecoder().decode(claveActual.toString());
                PublicKey clavePublica = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(claveBytes));
                clavesPublicas.add(clavePublica);
            }
        }
        return clavesPublicas;
    }
}
