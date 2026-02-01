package DAO;

import Entidades.Usuarios.Persona;
import Interfaces.PersonaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonaDaoImpl extends Conexion implements PersonaDAO {
    @Override
    public void agregarPersona(Persona datos, String clave, String rol) {
        try{

            this.conectarse();

            PreparedStatement ps = this.conectar.prepareStatement("INSERT INTO biblioteca_mix.usuarios(personaID, nombres, apellidos, correo, clave, rol) VALUES(?,?,?,?,?,?);");
            ps.setString(1, datos.getPersonaID());
            ps.setString(2, datos.getNombres());
            ps.setString(3, datos.getApellidos());
            ps.setString(4, datos.getCorreo());
            ps.setString(5, clave);
            ps.setString(6, rol);
            ps.executeUpdate();
            System.out.println("Datos guardados correctamente");
        }catch (Exception e){
            System.out.println("Error del tipo: " + e.getMessage());
        }
    }

    @Override
    public void obtenerPersona(String ID){
        try{
            this.conectarse();
            PreparedStatement ps = this.conectar.prepareStatement("SELECT * FROM biblioteca_mix.usuarios WHERE personaID = ?;");
            ps.setString(1, ID);
            ps.executeQuery();
        }catch(Exception e){
            System.out.println("Error del tipo(Obtener persona): " + e.getMessage());
        }
    }

    @Override
    public void editarPersona(Persona datos, String nombre) {

    }
}
