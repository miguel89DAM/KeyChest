package rubioclemente.miguelangel.keychest.apiservices;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

/**
 * Clase que genera una conexión https con el servidor API
 */
public class ConexionHttps extends OkHttpClient {
    private final OkHttpClient.Builder client;
    private SSLContext sslContext;
    public ConexionHttps() {
        client = new Builder();
    }

    /**
     * Este método devuelve un builder con las especificaciones necesarias para el uso de SSL.
     * @return
     */
    public OkHttpClient.Builder getConnection() {
        try{
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { TRUST_ALL_CERTS }, new java.security.SecureRandom());
            client.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERTS);
            /**
             * El método hostnameVerifier deshabilita el hostnameVerifier predeterminado y lo reemplaza por otro que omita la verificación del nombre del host
             */
            client.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }catch(NoSuchAlgorithmException nsa){
            System.out.println(nsa.getMessage());
        }
        catch(KeyManagementException kme){
            System.out.println(kme.getMessage());
        }
        return client;
    }

    /**
     * Generamos este método para que OkHttpClient confíe en el certificado independientemente de su naturaleza. Firmado por CA o autofirmado como es nuestro caso.
     */
    TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }
    };



}
