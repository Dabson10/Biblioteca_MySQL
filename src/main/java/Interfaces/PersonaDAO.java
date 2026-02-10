package Interfaces;

import Entidades.Usuarios.Persona;
import Entidades.Usuarios.UsuarioDTO;

import java.util.List;

public interface PersonaDAO {

    public void agregarPersona(String personaID, String nombres, String apellidos, String correo, String clave, String rol, String prestamo);
    public Persona obtenerPersona(String ID);
    public Persona correoPersona(String correo);
    public void editarPersona(Persona datos, String nombre);
    public boolean correoExistente(String correo);
    public String obtenerUltimo(String prefijo);
    public UsuarioDTO validarCredenciales(String correo);
//    public List<Persona> listarPersonas();

}
