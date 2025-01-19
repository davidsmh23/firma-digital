import java.io.*;
import java.util.Base64;

public class ArchivoUtil {

    public static byte[] readFile(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        try (FileInputStream entrada = new FileInputStream(archivo)) {
            return entrada.readAllBytes();
        }
    }

    public static void embedSignature(String rutaArchivoOriginal, String rutaArchivoFirmado, String firmaCodificada)
            throws IOException {
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivoOriginal));
             BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivoFirmado))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                escritor.write(linea);
                escritor.newLine();
            }
            escritor.write("-----BEGIN DIGITAL SIGNATURE-----");
            escritor.newLine();
            escritor.write(firmaCodificada);
            escritor.newLine();
            escritor.write("-----END DIGITAL SIGNATURE-----");
        }
    }
}