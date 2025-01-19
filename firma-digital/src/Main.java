import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Uso: java -jar Firmador.jar <rutaArchivoEntrada> <rutaArchivoSalida> <directorioClaves>");
            System.exit(1);
        }

        // Obtener los argumentos de línea de comandos
        String rutaArchivoEntrada = args[0];
        String rutaArchivoSalida = args[1];
        String directorioClaves = args[2];

        // Generar un par de claves (clave pública y privada)
        KeyPairGenerator generadorLlave = KeyPairGenerator.getInstance("RSA");
        generadorLlave.initialize(2048);
        KeyPair clavePar = generadorLlave.generateKeyPair();
        PrivateKey clavePrivada = clavePar.getPrivate();
        PublicKey clavePublica = clavePar.getPublic();

        // Guardar las claves en archivos
        ArchivoUtil.guardarClave(directorioClaves + "/clave_privada.pem", clavePrivada.getEncoded());
        ArchivoUtil.guardarClave(directorioClaves + "/clave_publica.pem", clavePublica.getEncoded());

        System.out.println("Claves generadas y guardadas en: " + directorioClaves);

        // Leer el contenido del archivo a firmar
        byte[] original = ArchivoUtil.readFile(rutaArchivoEntrada);

        // Generar la firma digital
        byte[] firmaDigital = FirmaUtil.generateSignature(original, clavePrivada);

        // Convertir la firma a Base64
        String encodedFirma = Base64.getEncoder().encodeToString(firmaDigital);

        // Incrustar la firma en el archivo de salida
        ArchivoUtil.embedSignature(rutaArchivoEntrada, rutaArchivoSalida, encodedFirma);

        System.out.println("Firma incrustada en: " + rutaArchivoSalida);
    }
}

