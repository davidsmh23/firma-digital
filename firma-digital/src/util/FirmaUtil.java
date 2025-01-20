package util;

import java.security.PrivateKey;
import java.security.Signature;

public class FirmaUtil {

    public static byte[] generateSignature(byte[] data, PrivateKey clavePrivada) throws Exception {
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(clavePrivada);
        firma.update(data);
        return firma.sign();
    }
}
