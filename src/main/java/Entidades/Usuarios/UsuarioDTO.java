package Entidades.Usuarios;

public class UsuarioDTO {
    private String claveHash;
    private String correo;
    private String rol;

    public UsuarioDTO(String correo, String claveHash, String rol){
        this.claveHash = claveHash;
        this.correo = correo;
        this.rol = rol;
    }

    public String getClaveHash(){
        return claveHash;
    }
    public String getCorreo(){
        return correo;
    }
    public String getRol(){
        return rol;
    }

}
