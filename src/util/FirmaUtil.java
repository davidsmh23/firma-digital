package util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * Clase utilitaria para la generación y verificación de firmas digitales.
 * Utiliza el algoritmo RSA con SHA-256 para garantizar la integridad y autenticidad de los datos.
 */
public class FirmaUtil {

    /**
     * Genera una firma digital para los datos proporcionados utilizando una clave privada.
     *
     * @param data Datos en formato de arreglo de bytes que se desea firmar.
     * @param clavePrivada Clave privada utilizada para generar la firma digital.
     * @return Firma digital generada como un arreglo de bytes.
     * @throws Exception Si ocurre algún error durante el proceso de firma.
     */
    public static byte[] generarFirma(byte[] data, PrivateKey clavePrivada) throws Exception {
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(clavePrivada);
        firma.update(data);
        return firma.sign();
    }

    /**
     * Verifica la validez de una firma digital contra los datos originales y una clave pública.
     *
     * @param data Datos originales en formato de arreglo de bytes.
     * @param firmaDigital Firma digital en formato de arreglo de bytes.
     * @param clavePublica Clave pública utilizada para verificar la firma.
     * @return {@code true} si la firma es válida, {@code false} en caso contrario.
     * @throws Exception Si ocurre algún error durante el proceso de verificación.
     */
    public static boolean verificarFirma(byte[] data, byte[] firmaDigital, PublicKey clavePublica) throws Exception {
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initVerify(clavePublica);
        firma.update(data);
        return firma.verify(firmaDigital);
    }
}
