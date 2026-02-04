package Entidades.Usuarios;

import Entidades.Ejemplar;

public class Usuario extends Persona{

    private String libroPrestado;

    public Usuario(String personaID, String nombres, String apellidos, String correo){
        super(personaID, nombres, apellidos, correo);
    }

    public void setLibroPrestado(String libroPrestado){
        if(libroPrestado != null){
            //Si es diferente a null entonces guardamos el libro prestado.
            this.libroPrestado = libroPrestado;
        }
    }
    public String getLibroPrestado(){
        return libroPrestado;
    }

    @Override
    public String mostrarDatos(){
        String prestamo = (libroPrestado.equals("Sin prestamos")) ? libroPrestado : "Con deuda" ;
        return "Usuario\n" +
                "Nombre: " + getNombres() +
                "\nApellido" + getApellidos() +
                "\nCorreo: " + getCorreo() +
                "\nID: " + getPersonaID() +
                "\n" + prestamo;
    }

}
