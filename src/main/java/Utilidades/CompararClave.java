package Utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class CompararClave {

    public boolean compararClave(String clave, String claveHash){
        boolean acceso = false;
        try{
            if(BCrypt.checkpw(clave, claveHash)){
                //La contraseña plana y la hasheada es la misma
                acceso = true;
            }
        }catch(IllegalArgumentException ilegal ){
            System.out.println("Error del tipo: " + ilegal.getMessage());
        }
        return acceso;
    }
}
