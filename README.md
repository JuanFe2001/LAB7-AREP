## Escuela Colombiana de Ingeniería
### Arquitecturas Empresariales – AREP
# LAB7-AREP
#### TALLER 7: APLICACIÓN DISTRIBUIDA SEGURA EN TODOS SUS FRENTES

Desarrolle una aplicación Web segura con los siguientes requerimientos:

Debe permitir un acceso seguro desde el browser a la aplicación. Es decir debe garantizar autenticación, autorización e integridad de usuarios. Debe tener al menos dos computadores comunicacndose entre ellos y el acceso de servicios remotos debe garantizar: autenticación, autorización e integridad entre los servicios. Nadie puede invocar los servicios si no está autorizado. Explique como escalaría su arquitectura de seguridad para incorporar nuevos servicios.

## Elementos Necesarios
* Kit de desarrollo de Java (JDK) versión 11 o posterior
* Herramienta de construcción Maven

## Pasos Para Ejecucion:
- En consola nos vamos a la carpeta LAB7-AREP del proyecto
- Una vez alli compilamos el proyecto con el siguiente comando:

  ``` 
  mvn clean install
  ```
  - Luego ejecutamos el servicio con los siguientes comando:
 
  ``` 
  java -cp target/AREP-TALLER7-1.0-SNAPSHOT.jar edu.eci.arep.lab7.arep.LoginService
  ```

    ``` 
  java -cp target/AREP-TALLER7-1.0-SNAPSHOT.jar edu.eci.arep.lab7.arep.UserService
  ```

  - Una vez corriendo el servicio nos dirigimos a nuestro browser de preferencia y accedemos a la pagina web aqui:

    https://localhost:8087/login.html

  - Deberiamos ver la siguiente pagina si seguimos los pasos correctamente:

  ![image](https://github.com/JuanFe2001/LAB7-AREP/assets/123691538/d1bda9c5-840a-4f37-a770-0247fd49de61)

  ## La aplicacion cuenta con 2 clases principales las cuales son:

  **LoginService**: Proporciona funcionalidad para configurar y ejecutar un servidor seguro de inicio de sesión utilizando HTTPS, así como para realizar solicitudes HTTP GET a una URL especificada.
  **UserService**: proporciona funcionalidad para agregar usuarios, calcular y verificar contraseñas hash, así como para configurar y ejecutar un servidor seguro de autenticación de usuario utilizando HTTPS.

  ## Arquitectura:

**Componentes del diagrama**:

**Grupo de seguridad**: Un grupo de seguridad es una colección de reglas de seguridad que controlan el tráfico de red que ingresa y sale de una instancia de EC2.
**Instancia EC2**: Una instancia de Amazon Elastic Compute Cloud (EC2) es una computadora virtual en la nube.
**Usuario**: Un usuario es una persona que tiene acceso a una instancia de EC2.
**Cliente**: Un cliente es una aplicación o programa que se ejecuta en una instancia de EC2.
**Internet**: Internet es una red mundial de computadoras.
**Web Browser**: Un navegador web es una aplicación que se utiliza para acceder a sitios web.
**LoginService**: LoginService es un servicio que se ejecuta en una instancia de EC2 y que proporciona servicios de inicio de sesión para los usuarios.
**UserService**: UsersService es un servicio que se ejecuta en una instancia de EC2 y que proporciona servicios de administración de usuarios para los usuarios.
Reglas de seguridad:

El usuario solo puede acceder al LoginService a través del puerto 8087.
El usuario solo puede acceder al UsersService a través del puerto 8088.
El LoginService y el UsersService solo pueden acceder a Internet a través del puerto 443 (HTTPS).

Ambas aplicaciones emplean certificados digitales auto-firmados que se encuentran almacenados en un repositorio de claves (certificados/ecikeystore.p12) para asegurar la comunicación a través de HTTPS. Además, se emplea un almacén de confianza (certificados/myTrustStore.p12) para autenticar los certificados de los servicios remotos.

El desarrollo de la aplicación se lleva a cabo mediante el micro-framework Spark Java y se empaqueta como una aplicación ejecutable utilizando el plugin maven-shade-plugin. Este complemento permite la creación de un archivo JAR independiente que incluye todas las dependencias necesarias para ejecutar la aplicación.

## Escalabilidad:

La infraestructura de seguridad implementada en esta aplicación ha sido diseñada con la capacidad de expandirse fácilmente, lo que facilita la incorporación de nuevos servicios sin comprometer los niveles de seguridad existentes.

Para agregar un nuevo servicio a esta infraestructura, se deben seguir los siguientes pasos:

**Generar un nuevo par de certificados**: Mediante la herramienta Keytool de Java, se debe generar un nuevo conjunto de certificados (tanto la clave pública como privada) destinados al nuevo servicio. Estos certificados deben ser almacenados en el repositorio de claves compartido.

**Configurar el nuevo servicio**: Se requiere configurar el nuevo servicio para utilizar los certificados generados previamente. Esto implica cargar el repositorio de claves y habilitar la comunicación segura mediante HTTPS en el código del servicio.

**Agregar el certificado al repositorio de confianza**: El certificado correspondiente al nuevo servicio debe ser incorporado al repositorio de confianza compartido. De este modo, los demás servicios podrán confiar en el nuevo servicio durante las comunicaciones seguras.

**Actualizar la configuración de los servicios existentes**: Es necesario actualizar la configuración de los servicios existentes para que confíen en el nuevo certificado añadido al repositorio de confianza. Esto puede implicar reiniciar los servicios o recargar la configuración de seguridad durante la ejecución.

**Establecer la comunicación segura**: Una vez que todos los servicios confían mutuamente en los certificados, se puede establecer una comunicación segura entre ellos utilizando HTTPS.

## Demostracion:

**PRUEBA 1**:
* Caso exitoso:
  **Usuario**: Felipe
  **Contraseña**: 654321
  
  ![image](https://github.com/JuanFe2001/LAB7-AREP/assets/123691538/3e90a95b-81d8-49a2-aebb-e1f9f5ef3116)

  **PRUEBA 2**
  * Caso incorrecto
    **Usuario**: Felipe
    **Contraseña**: Juan

  ![image](https://github.com/JuanFe2001/LAB7-AREP/assets/123691538/85ee194e-d24b-435b-95d1-0e03101c05d6)

  **PRUEBA 3**
  * Caso exitoso
    **Usuario**: Juan
    **Contraseña**: 123456

  ![image](https://github.com/JuanFe2001/LAB7-AREP/assets/123691538/a9c9db0c-51c6-4c55-a2ce-ec1f436cf918)

  **PRUEBA 4**
  * Caso incorrecto
    **Usuario**: Juan
    **Contraseña**: Felipe

  ![image](https://github.com/JuanFe2001/LAB7-AREP/assets/123691538/cea0e39f-b8cc-4208-82e9-a978f208d0a4)

  

  




    
    
