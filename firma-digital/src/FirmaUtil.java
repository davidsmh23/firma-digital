import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class FirmaUtil {

    public static byte[] generateSignature(byte[] datosOriginales, PrivateKey clavePrivada) throws Exception {
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(clavePrivada);
        firma.update(datosOriginales);
        return firma.sign();
    }

    public static boolean verifySignature(String rutaArchivoFirmado, byte[] datosOriginales, String firmaCodificada,
                                          PublicKey clavePublica) throws Exception {
        byte[] firmaDigital = Base64.getDecoder().decode(firmaCodificada);

        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initVerify(clavePublica);
        firma.update(datosOriginales);
        return firma.verify(firmaDigital);
    }
}