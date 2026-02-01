package DAO;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    Connection conectar = null;
    String usuario = "root";
    String clave = "Dabson12";
    String baseD = "biblioteca_mix";
    String ubicacion = "localhost";
    String puerto = "3306";
    String url ="jdbc:mysql://" + ubicacion + ":" + puerto + "/" + baseD;

    public Connection conectarse(){

        try{
            //La siguiente línea accede a la carpeta external libraries y sigue el orden,
            //Abre la carpeta "com.mysql", después "cj", después "jdbc" y al final ejecuta la clase "Driver"
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Dirección formada con los datos de la base de datos y claves.
            conectar = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conexión realizada con éxito.");
        }catch(Exception e){
            System.out.println("Problemas con la conexión: " + e.getMessage());
        }
        return conectar;
    }

}
