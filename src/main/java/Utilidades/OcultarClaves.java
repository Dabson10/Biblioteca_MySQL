package Utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class OcultarClaves {
    /**
     * Esta función sirve para convertir una contraseña plana en una contraseña con Hash
     * @param clave : Clave en texto plano
     * @return Regresará la contraseña con hash.
     */
    public String ocultarContras(String clave){
        return BCrypt.hashpw(clave, BCrypt.gensalt());
    }
}
