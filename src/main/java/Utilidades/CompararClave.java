package Utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class CompararClave {
    /**
     * Esta función sirve para comparar una clave en texto plano y una con hash tipo jbCrypt
     * @param clave : Clave en texto plano.
     * @param claveHash : Clave con Hash
     * @return Si la clave es la misma entonces regresa un {@code true}, si no un {@code false}
     */
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
