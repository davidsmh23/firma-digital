import util.ArchivoUtil;
import util.FirmaUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
/**
 * Clase principal que permite firmar archivos digitalmente y comprobar la validez de firmas digitales.
 * Utiliza claves RSA generadas en el momento o almacenadas previamente.
 * También soporta una biblioteca de claves públicas para verificar firmas.
 */
public class Principal {
    /**
     * Método principal del programa.
     * Permite al usuario firmar archivos digitalmente, verificar firmas y gestionar claves públicas y privadas.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        try {
            while (true) {
                String[] opciones = {"Firmar archivo", "Verificar firma", "Salir"};
                int opcionSeleccionada = JOptionPane.showOptionDialog(null,
                        "Seleccione una opción:",
                        "Firma Digital",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]);

                if (opcionSeleccionada == 2 || opcionSeleccionada == JOptionPane.CLOSED_OPTION) {
                    JOptionPane.showMessageDialog(null, "Programa terminado.");
                    return;
                }

                if (opcionSeleccionada == 1) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Seleccione el archivo a comprobar");
                    if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) continue;
                    String rutaArchivoComprobar = fileChooser.getSelectedFile().getAbsolutePath();

                    fileChooser.setDialogTitle("Seleccione el archivo con la firma");
                    if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) continue;
                    String rutaArchivoFirma = fileChooser.getSelectedFile().getAbsolutePath();

                    fileChooser.setDialogTitle("Seleccione la biblioteca de claves públicas");
                    if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) continue;
                    String rutaBibliotecaClaves = fileChooser.getSelectedFile().getAbsolutePath();

                    byte[] contenidoArchivo = ArchivoUtil.readFile(rutaArchivoComprobar);
                    String firmaBase64 = ArchivoUtil.extraerFirmaDesdeArchivo(rutaArchivoFirma);
                    byte[] firmaDigital = Base64.getDecoder().decode(firmaBase64);
                    ArrayList<PublicKey> clavesPublicas = ArchivoUtil.cargarClavesPublicas(rutaBibliotecaClaves);

                    boolean firmaValida = false;
                    for (PublicKey clavePublica : clavesPublicas) {
                        if (FirmaUtil.verificarFirma(contenidoArchivo, firmaDigital, clavePublica)) {
                            firmaValida = true;
                            break;
                        }
                    }

                    JOptionPane.showMessageDialog(null, firmaValida ? "La firma es válida." : "La firma no es válida.");
                    continue;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccione el archivo a firmar");
                if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) continue;
                String rutaArchivoEntrada = fileChooser.getSelectedFile().getAbsolutePath();

                String rutaArchivoFirmado = "firmador/archivo_firmado.txt";
                String directorioClaves = "firmador/claves";
                File carpetaClaves = new File(directorioClaves);
                if (!carpetaClaves.exists() && !carpetaClaves.mkdirs()) {
                    throw new RuntimeException("No se pudo crear el directorio para las claves: " + directorioClaves);
                }

                KeyPairGenerator generadorLlave = KeyPairGenerator.getInstance("RSA");
                generadorLlave.initialize(2048);
                KeyPair clavePar = generadorLlave.generateKeyPair();
                PrivateKey clavePrivada = clavePar.getPrivate();
                PublicKey clavePublica = clavePar.getPublic();

                ArchivoUtil.guardarClave(directorioClaves + "/clavePrivada.key", clavePrivada.getEncoded());
                ArchivoUtil.guardarClave(directorioClaves + "/clavePublica.key", clavePublica.getEncoded());
                ArchivoUtil.agregarClavePublicaBiblioteca(directorioClaves + "/biblioteca.txt", clavePublica);

                byte[] contenidoOriginal = ArchivoUtil.readFile(rutaArchivoEntrada);
                byte[] firmaDigital = FirmaUtil.generarFirma(contenidoOriginal, clavePrivada);
                String encodedFirma = Base64.getEncoder().encodeToString(firmaDigital);
                ArchivoUtil.embedSignature(rutaArchivoEntrada, rutaArchivoFirmado, encodedFirma);

                JOptionPane.showMessageDialog(null, "Archivo firmado correctamente.\nGuardado en: " + rutaArchivoFirmado);
            }
        } catch (NoSuchFileException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidKeySpecException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar las claves.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Las claves no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
