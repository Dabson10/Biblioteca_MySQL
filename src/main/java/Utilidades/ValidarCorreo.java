package Utilidades;

import Exceptions.CorreoNoValido;

public class ValidarCorreo {
    /**
     * Esta función sirve para saber si el correo ingresado contiene un {@code @} y termina en {@code ".com"}
     * entonces regresara un true, si no entonces lanza una exception.
     * @param correo : El correo que se validara.
     * @return Si cumple el if regresara un {@code true} si no lanza una exception
     */
    public boolean validarCorreo(String correo){
        if(correo.contains("@") && correo.endsWith(".com")){
            //Si el correo contiene un "@" y termina con".com", entonces regresamos un true
            return true;
        }
        throw new CorreoNoValido("Correo electronico invalido.");
    }

}
