package edu.eci.arep.lab7.arep;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;


public class LoginService {
    
     /**
     * Método principal que inicia la aplicación.
     * 
     * @param args los argumentos de la línea de comandos
     * @throws NoSuchAlgorithmException si el algoritmo especificado no está disponible
     * @throws KeyStoreException si no se puede acceder al almacén de claves
     * @throws IOException si ocurre un error de entrada/salida
     * @throws KeyManagementException si ocurre un error relacionado con la gestión de claves
     * @throws CertificateException si ocurre un error relacionado con los certificados
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, CertificateException {
        staticFiles.location("/public");
        port(getPort());
        secure("certificados/ecikeystore.p12", "123456", null, null);
        configureTrustedSSLContext();
        get("/login", (req, res) -> {
            res.type("application/json");
            return readURL("name=" + req.queryParams("name") + "&password=" + req.queryParams("password"));
        });
    }

     /**
     * Obtiene el puerto en el que la aplicación será escuchada.
     * Si la variable de entorno "PORT" está definida, devuelve su valor convertido a entero.
     * Si no está definida, devuelve el valor predeterminado 8087.
     *
     * @return el número de puerto en el que la aplicación será escuchada
     */
    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8087;
    }

   
     /**
     * Configura el contexto SSL de confianza utilizando un archivo de almacén de confianza.
     * Este método carga un archivo de almacén de claves de confianza, inicializa un TrustManagerFactory
     * con el almacén de claves cargado y configura un SSLContext con los TrustManagers obtenidos del TrustManagerFactory.
     * Finalmente, establece el SSLContext predeterminado para la aplicación.
     * 
     * @throws KeyStoreException si no se puede acceder al almacén de claves
     * @throws IOException si ocurre un error de entrada/salida
     * @throws NoSuchAlgorithmException si el algoritmo especificado no está disponible
     * @throws KeyManagementException si ocurre un error relacionado con la gestión de claves
     * @throws CertificateException si ocurre un error relacionado con los certificados
     */
    private static void configureTrustedSSLContext() throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        File trustStoreFile = new File("certificados/myTrustStore.p12");
        char[] trustStorePassword = "123456".toCharArray();
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        for (TrustManager t : tmf.getTrustManagers()) System.out.println(t);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);
    }
  
     /**
     * Lee una URL haciendo una solicitud GET con una consulta proporcionada y devuelve la respuesta como una cadena.
     * 
     * @param query la cadena de consulta para la solicitud GET
     * @return la respuesta de la URL como una cadena
     * @throws IOException si ocurre un error de entrada/salida al realizar la solicitud o leer la respuesta
     */
    public static String readURL(String query) throws IOException {
        URL siteURL = new URL("https://localhost:8088/user?" + query);
        URLConnection urlConnection = siteURL.openConnection();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            if (headerName != null) System.out.print(headerName + ":");
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) System.out.print(value);
            System.out.println("");
        }
        
        System.out.println("-------message-body------");
        StringBuffer response = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        while ((inputLine = reader.readLine()) != null) response.append(inputLine);
        reader.close();
        System.out.println(response);
        System.out.println("GET DONE");
        return response.toString();
    }
}