import excepciones.ArchivoNoValido;
import excepciones.DirectorioClavesNoValido;
import util.ArchivoUtil;
import util.FirmaUtil;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Pedir al usuario la ruta del archivo a firmar
            System.out.println("Introduce la ruta completa del archivo a firmar:");
            String rutaArchivoEntrada = scanner.nextLine();

            // Obtener el directorio del archivo de entrada
            File archivoEntrada = new File(rutaArchivoEntrada);
            if (!archivoEntrada.exists()) {
                throw new ArchivoNoValido("El archivo especificado no existe.");
            }

            // Rutas de salida
            String directorioBase = archivoEntrada.getParent();
            String rutaArchivoFirmado = directorioBase + "/archivo_firmado.txt";
            String directorioClaves = directorioBase + "/claves";

            // Crear el directorio para las claves si no existe
            File carpetaClaves = new File(directorioClaves);
            System.out.println("Intentando crear el directorio: " + directorioClaves);
            if (!carpetaClaves.exists() && !carpetaClaves.mkdirs()) {
                throw new DirectorioClavesNoValido("No se pudo crear el directorio para las claves en: " + directorioClaves);
            }

            // Generar un par de claves (clave pública y privada)
            KeyPairGenerator generadorLlave = KeyPairGenerator.getInstance("RSA");
            generadorLlave.initialize(2048);
            KeyPair clavePar = generadorLlave.generateKeyPair();
            PrivateKey clavePrivada = clavePar.getPrivate();
            PublicKey clavePublica = clavePar.getPublic();

            // Guardar las claves en el directorio de claves
            ArchivoUtil.guardarClave(directorioClaves + "/clavePrivada.key", clavePrivada.getEncoded());
            ArchivoUtil.guardarClave(directorioClaves + "/clavePublica.key", clavePublica.getEncoded());

            // Leer el contenido del archivo original
            byte[] contenidoOriginal = ArchivoUtil.readFile(rutaArchivoEntrada);

            // Generar la firma digital
            byte[] firmaDigital = FirmaUtil.generateSignature(contenidoOriginal, clavePrivada);

            // Convertir la firma a Base64 para incrustarla en el archivo
            String encodedFirma = Base64.getEncoder().encodeToString(firmaDigital);

            // Incrustar la firma en el archivo de salida
            ArchivoUtil.embedSignature(rutaArchivoEntrada, rutaArchivoFirmado, encodedFirma);

            System.out.println("Archivo firmado correctamente.");
            System.out.println("Archivo firmado guardado en: " + rutaArchivoFirmado);
            System.out.println("Claves guardadas en: " + directorioClaves);

        }catch (ArchivoNoValido e) {
            System.err.println(e.getMessage());
        } catch (DirectorioClavesNoValido e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



