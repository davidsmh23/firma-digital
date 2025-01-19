import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArchivoUtil {

    public static byte[] readFile(String rutaArchivo) throws IOException {
        return Files.readAllBytes(Paths.get(rutaArchivo));
    }

    public static void guardarClave(String ruta, byte[] clave) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            fos.write(clave);
        }
    }

    public static void embedSignature(String rutaEntrada, String rutaSalida, String firma) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaEntrada));
             BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                writer.write(linea);
                writer.newLine();
            }

            writer.write("-----BEGIN DIGITAL SIGNATURE-----");
            writer.newLine();
            writer.write(firma);
            writer.newLine();
            writer.write("-----END DIGITAL SIGNATURE-----");
        }
    }
}
