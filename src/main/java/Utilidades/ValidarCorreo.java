package Utilidades;

import Exceptions.CorreoNoValido;

public class ValidarCorreo {
    public boolean validarCorreo(String correo){
        boolean acceso = false;
        if(correo.contains("@") && correo.endsWith(".com")){
            //Si el correo contiene un "@" y termina con ".com", entonces regresamos un true
            acceso = true;
        }else{
            throw new CorreoNoValido("Correo electronico invalido.");
        }
        return acceso;
    }

}
