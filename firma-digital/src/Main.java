import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws Exception {
        // Ruta del archivo original
        String rutaArchivo = "archivosFirmados/archivo.txt";

        // Generar un par de claves (clave pública y privada)
        KeyPairGenerator generadorLlave = KeyPairGenerator.getInstance("RSA");
        generadorLlave.initialize(2048);
        KeyPair clavePar = generadorLlave.generateKeyPair();
        PrivateKey clavePrivada = clavePar.getPrivate();
        PublicKey clavePublica = clavePar.getPublic();

        // Leer el contenido original del archivo
        byte[] original = ArchivoUtil.readFile(rutaArchivo);

        // Generar la firma digital
        byte[] firmaDigital = FirmaUtil.generateSignature(original, clavePrivada);

        // Convertir la firma a Base64 para incrustarla en el archivo
        String encodedFirma = Base64.getEncoder().encodeToString(firmaDigital);

        // Incrustar la firma al final del archivo sin modificar el contenido original
        String rutaArchivoFirmado = "archivosFirmados/archivo_firmado.txt";
        ArchivoUtil.embedSignature(rutaArchivo, rutaArchivoFirmado, encodedFirma);

        System.out.println("Firma incrustada en: " + rutaArchivoFirmado);

        // Verificar la firma incrustada
        boolean esValida = FirmaUtil.verifySignature(rutaArchivoFirmado, original, encodedFirma, clavePublica);

        if (esValida) {
            System.out.println("La firma es válida.");
        } else {
            System.out.println("La firma NO es válida.");
        }
    }
}

