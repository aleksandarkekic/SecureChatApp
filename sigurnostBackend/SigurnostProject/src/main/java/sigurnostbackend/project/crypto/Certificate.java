package sigurnostbackend.project.crypto;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.time.LocalDate;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Date;
import java.time.LocalDate;

public class Certificate {
    private static final int CERTIFICATE_BITS = 2048;
    private static final String CERTIFICATE_ALGORITHM = "RSA";
    private static final String SIG_ALGORITHM = "SHA256withRSA";
    private static final String CERTIFICATE_ALIAS = "SAMOPOTPISANI_SERTIFIKAT";
    private static final String CA_PASSWORD = "sigurnost";
    public static final String CA_LOCATION = "src/main/resources/tools" + File.separator + "ca" + File.separator + "ca.jks";
    private static final String USR_PASSWORD = "sigurnost";
    private static final String CRT_PASSWORD = "sigurnost";
    private static final String USR_CRT_LOCATION = "src/main/resources/tools" + File.separator + "users";
    public static final String SERIAL = "src/main/resources/tools" + File.separator + "serial";
    public static int serialNumber = 0;
    static {
        addBouncyCastleAsSecurityProvider(); // adds the Bouncy castle provider to java security
        //Security.addProvider(new BouncyCastleProvider());
    }
    public static void createCACertificate() throws Exception {

        X509Certificate cert = null;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CERTIFICATE_ALGORITHM);
        keyPairGenerator.initialize(CERTIFICATE_BITS, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // GENERATE THE X509 CERTIFICATE
        final X500Principal issuer = new X500Principal("CN=MyCA");
        final BigInteger sn = new BigInteger(64, new SecureRandom());
        final Date from = Date.valueOf(LocalDate.now());
        final Date to = Date.valueOf(LocalDate.now().plusMonths(6));
        final X509v3CertificateBuilder v3CertGen =
                new JcaX509v3CertificateBuilder(issuer, sn, from, to, issuer, keyPair.getPublic());
        final JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
        v3CertGen.addExtension(Extension.authorityKeyIdentifier, false,
                extUtils.createAuthorityKeyIdentifier(keyPair.getPublic()));
        v3CertGen.addExtension(Extension.subjectKeyIdentifier, false,
                extUtils.createSubjectKeyIdentifier(keyPair.getPublic()));
        v3CertGen.addExtension(Extension.basicConstraints, true,
                new BasicConstraints(0));
        v3CertGen.addExtension(Extension.keyUsage, true,
                new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
        final ContentSigner signer = new JcaContentSignerBuilder(SIG_ALGORITHM)
                .build(keyPair.getPrivate());
        X509Certificate certificate = new JcaX509CertificateConverter()
                .getCertificate(v3CertGen.build(signer));
        saveCert(certificate, keyPair.getPrivate(), CERTIFICATE_ALIAS, CA_PASSWORD, CA_LOCATION);
    }

    /**
     * This method saves certificate and private key into keystore.
     *
     * @param cert First param.Represents certificate that is going to be saved.
     * @param key  Second param.Represents privateKey that is going to be saved.
     * @throws Exception
     */
    public static void saveCert(X509Certificate cert, PrivateKey key, String alias, String pass, String location) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
//        File file=new File(location);
//        file.createNewFile();
        keyStore.load(null, pass.toCharArray());//To create an empty keystore using the above load method, pass null as the InputStream argument.
        keyStore.setKeyEntry(alias, key, pass.toCharArray(), new java.security.cert.Certificate[]{cert});
        File file1 = new File(location);
        keyStore.store(new FileOutputStream(file1), pass.toCharArray());
    }

    public static void addBouncyCastleAsSecurityProvider() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static X509Certificate loadCACertificate(String path) {
        try {
            InputStream in = Files.newInputStream(Paths.get(path));
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(in, CA_PASSWORD.toCharArray());
            return (X509Certificate) keyStore.getCertificate(CERTIFICATE_ALIAS);
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Failed to load certificate from keystore: " + e.getMessage());
        }
        return null;
    }

    public static PrivateKey loadCAPrivateKey(String path) {

        try {
            InputStream in = Files.newInputStream(Paths.get(path));
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(in, CA_PASSWORD.toCharArray());
            return (PrivateKey) keyStore.getKey(CERTIFICATE_ALIAS, CA_PASSWORD.toCharArray());
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Failed to load private key from keystore: " + e.getMessage());
        }
        return null;
    }

    public static PrivateKey loadUserPrivateKey(String path, String name) {

        try {
            InputStream in = Files.newInputStream(Paths.get(path));
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(in, USR_PASSWORD.toCharArray());
            return (PrivateKey) keyStore.getKey(name, USR_PASSWORD.toCharArray());
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Failed to load private key from keystore: " + e.getMessage());
        }
        return null;
    }

    public static void createUserCert(String name) throws Exception {
        //String userCertificatePath = Configuration.USERS + File.separator + name + ".p12";
        X509Certificate caCert = null;
        PrivateKey caPrKey;
        // X509Certificate rootCertificate = loadCACertifusicate(Paths.get(CA_LOCATION));
        caCert = loadCACertificate(CA_LOCATION);
        caPrKey = loadCAPrivateKey(CA_LOCATION);
        final Date from = Date.valueOf(LocalDate.now());
        final Date to = Date.valueOf(LocalDate.now().plusMonths(6));
        X500Name owner = new X500Name("CN=" + name);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        System.out.println("keypair: "+keyPair.getPrivate());
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(new X500Name(caCert.getIssuerDN().getName()),
                BigInteger.valueOf(serialNumber), from, to, owner, keyPair.getPublic());
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature));
        // builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.dataEncipherment));
        builder.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));
        X509Certificate userCertificate = new JcaX509CertificateConverter().getCertificate(
                builder.build(new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(caPrKey)));
        serialNumber++;
        writeSerialNumber(serialNumber);
        saveCert(userCertificate, keyPair.getPrivate(), name, CRT_PASSWORD, USR_CRT_LOCATION + File.separator + name + ".jks");
        //saveX509CertificateToKeyStore(userCertificate,name,userCertificatePath,password,rootCertificate,caCert,keyPair);
        //  return userCertificate;
    }

    public static void writeSerialNumber(Integer serial) {
        PrintWriter writer1 = null;
        try {
            File file = new File(SERIAL + File.separator + "serial.txt");
            file.createNewFile();
            writer1 = new PrintWriter(file);
            writer1.write(serial.toString());
            writer1.flush();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            writer1.close();
        }
    }

    public static PublicKey getPubKeyFromPrivKey(PrivateKey key) {
        // RSAPrivateCrtKeyImpl rsaPrivateKey = (RSAPrivateCrtKeyImpl)privateKey;
        try {
            //   RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key;
            RSAPrivateCrtKey privk = (RSAPrivateCrtKey) key;

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
//            byte[] privateKeyBytes = key.getEncoded();
//
//            // Create a PKCS8EncodedKeySpec from the private key bytes
//            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
//
//            // Create a KeyFactory for RSA
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            // Generate the public key from the private key spec
//            PublicKey publicKey = keyFactory.generatePublic(privateKeySpec);
            return publicKey;

            //create a KeySpec and let the Factory due the Rest. You could also create the KeyImpl by your own.
//        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent()));
//        System.out.println(publicKey.getEncoded()); //store it - that's it
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}