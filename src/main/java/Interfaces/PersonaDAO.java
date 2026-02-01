package Interfaces;

import Entidades.Usuarios.Persona;

import java.util.List;

public interface PersonaDAO {

    public void agregarPersona(Persona datos, String clave, String rol);
    public void obtenerPersona(String ID);
    public void editarPersona(Persona datos, String nombre);
//    public List<Persona> listarPersonas();

}
