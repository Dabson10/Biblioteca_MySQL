package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            //Se comenta esta parte para que no existan problemas con mostrar el texto continuamente.
//            System.out.println("Conexión realizada con éxito.");
        }catch(Exception e){
            System.out.println("Problemas con la conexión: " + e.getMessage());
        }
        return conectar;
    }
    public void cerrarConexion(){
        try{
            if(conectar != null){
                //Si la conexión está activa entonces la cerramos
                conectar.close();
            }
        }catch (SQLException e ){
            System.out.println("Error del tipo: " + e.getMessage());
        }
    }
}
