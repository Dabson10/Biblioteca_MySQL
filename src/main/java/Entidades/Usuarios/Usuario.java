package Entidades.Usuarios;

import Entidades.Ejemplar;

public class Usuario extends Persona{

    private Ejemplar libroPrestado;

    public Usuario(String personaID, String nombres, String apellidos, String correo){
        super(personaID, nombres, apellidos, correo);
    }

    public void setLibroPrestado(Ejemplar libroPrestado){
        if(libroPrestado != null){
            //Si es diferente a null entonces guardamos el libro prestado.
            this.libroPrestado = libroPrestado;
        }
    }

    @Override
    public String mostrarDatos(){
        return "Usuario\n" +
                "Nombre: " + getNombres() +
                "\nApellido" + getApellidos() +
                "\nCorreo: " + getCorreo() +
                "\nID: " + getPersonaID() +
                "\nLibro prestado: " + libroPrestado.mostrarDatos();
    }

}
