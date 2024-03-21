package edu.eci.arep.lab7.arep;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import static spark.Spark.*;


public class UserService {

    private static final HashMap<String, byte[]> users = new HashMap<>();
   
    
    /**
    * Método principal que inicia la aplicación.
    * Agrega dos usuarios al sistema con sus respectivas contraseñas.
    * Configura el puerto en el que la aplicación será escuchada.
    * Habilita la seguridad HTTPS utilizando un archivo de almacén de claves.
    * Configura una ruta para manejar las solicitudes GET a /user, donde se verifica el nombre de usuario y la contraseña.
    * 
    * @param args los argumentos de la línea de comandos (no utilizado en este método)
    * @throws NoSuchAlgorithmException si el algoritmo especificado no está disponible
    */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        addUser("Juan", "123456");
        addUser("Felipe", "654321");
        port(getPort());
        secure("certificados/ecikeystore.p12", "123456", null, null);
        get("/user", (req, res) -> {
            res.type("application/json");
            boolean result = verifyPassword(req.queryParams("name"), req.queryParams("password"));
            return "{\"result\":" + result + "}";
        });
    }

     /**
     * Calcula el hash de una contraseña utilizando el algoritmo SHA-256.
     * 
     * @param password la contraseña a ser hasheada
     * @return un arreglo de bytes que representa el hash de la contraseña
     * @throws NoSuchAlgorithmException si el algoritmo especificado no está disponible
     */
    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes();
        return md.digest(bytes);
    }
    
    /**
    * Verifica si la contraseña proporcionada por un usuario coincide con el hash almacenado para ese usuario.
    * 
    * @param userName el nombre de usuario para el que se verifica la contraseña
    * @param password la contraseña proporcionada por el usuario
    * @return true si la contraseña proporcionada coincide con el hash almacenado para el usuario, false en caso contrario
    * @throws NoSuchAlgorithmException si el algoritmo especificado no está disponible
    */
    public static boolean verifyPassword(String userName, String password) throws NoSuchAlgorithmException {
        byte[] hash = users.get(userName);
        byte[] attemptedHash = hashPassword(password);
        return Arrays.equals(hash, attemptedHash);
    }

     /**
     * Obtiene el número de puerto en el que la aplicación será ejecutada.
     * Si la variable de entorno "PORT" está definida, devuelve su valor convertido a entero.
     * Si no está definida, devuelve el valor predeterminado 8088.
     * 
     * @return el número de puerto en el que la aplicación será ejecutada
     */
    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8088;
    }
    
     /**
     * Agrega un nuevo usuario al sistema con su respectiva contraseña.
     * 
     * @param name el nombre de usuario del nuevo usuario
     * @param password la contraseña del nuevo usuario
     * @throws NoSuchAlgorithmException si el algoritmo especificado no está disponible
     */
    public static void addUser(String name, String password) throws NoSuchAlgorithmException {
        users.put(name, hashPassword(password));
    }
}