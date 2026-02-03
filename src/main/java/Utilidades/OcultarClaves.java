package Utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class OcultarClaves {
    public String ocultarContras(String clave){
        return BCrypt.hashpw(clave, BCrypt.gensalt());
    }
}
